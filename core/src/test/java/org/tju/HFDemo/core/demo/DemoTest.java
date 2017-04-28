package org.tju.HFDemo.core.demo;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tju.HFDemo.common.config.Config;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.role.Organization;
import org.tju.HFDemo.core.role.TestOrganization;
import org.tju.HFDemo.core.role.TestUser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by shaohan.yin on 26/04/2017.
 */
public class DemoTest extends AbstractTest {

    private Config config = Config.DEFAULT;

    private HFClient client;
    private HFCAClient caClient;
    private TestUser admin;
    private TestOrganization organization;

    @Before
    public void setUp() {
        organization = new TestOrganization();
        organization.setMSPID("Org0MSP");
        organization.addOrderer("orderer.example.com", "grpc://localhost:7050");
        organization.addPeer("peer0.org1.example.com", "grpc://localhost:7051");
        organization.addPeer("peer1.org1.example.com", "grpc://localhost:8051");
    }

    @Test
    @Ignore
    public void demoTest() {
        try {
            // set up client
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            caClient = new HFCAClient(config.getString("ca.location"), null);
            caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            // user registration
            admin = new TestUser("admin");
            admin.setMSPID("Org0MSP");
            admin.setEnrollment(caClient.enroll("admin", "adminpw"));

            // user context
            client.setUserContext(admin);

            // new chain
            Chain demo = constructChain("mychannel", client, organization);

            // run chain
            runChain(client, demo, organization);

        } catch (Exception e) {
            logger.error("[Client Test Fail]", e);
            fail();
        }
    }

    private Chain constructChain(String chainName, HFClient client, Organization organization) throws InvalidArgumentException, TransactionException {
        logger.info("[construct chain]name:{}, org:{}", chainName, organization.getMSPID());

        // set order for client
        Orderer orderer = client.newOrderer("orderer.example.com", organization.getOrderer("orderer.example.com"));

        // configurations
        ChainConfiguration chainConfiguration = null;
        try {
            chainConfiguration = new ChainConfiguration(new File("src/test/e2e/channel.tx"));
        } catch (IOException e) {
            logger.error("[construct chain][look for chain configuration]", e);
            fail();
        }

        // new chain
        logger.info("[construct chain][new chain]{}", chainName);
        Chain chain = client.newChain(chainName, orderer, chainConfiguration);
        organization.getPeersName().stream().forEach((peerName) -> {
            try {
                chain.joinPeer(client.newPeer(peerName, organization.getPeer(peerName), null));
            } catch (Exception e) {
                logger.error("[construct chain][join peer][fail]peer:{}",peerName, e);
            }
        });
        chain.initialize();

        logger.info("[construct chain][success]name:{}", chainName);
        return chain;
    }

    private void runChain(HFClient client, Chain chain, Organization organization) {
        logger.info("[run chain]name:{}", chain.getName());

        final ChainCodeID chainCodeID = ChainCodeID.newBuilder()
                .setName("chaincode_example02")
                .setVersion("1")
                .setPath("chaincode_example02")
                .build();

        // install chaincode
        logger.info("[run chain][create install proposal]");
        InstallProposalRequest request = client.newInstallProposalRequest();
        request.setChaincodeID(chainCodeID);
        request.setChaincodeSourceLocation(new File("src/test/e2e/chaincodes/go"));
        request.setChaincodeVersion("1");

        logger.info("[run chain][send install proposal]");
        try {
            handleResponses(chain.sendInstallProposal(request));
        } catch (Exception e) {
            logger.error("[run chain][fail]", e);
            fail();
        }

        // instantiate chaincode
        InstantiateProposalRequest instantiateProposalRequest = client.newInstantiationProposalRequest();
        instantiateProposalRequest.setChaincodeID(chainCodeID);
        instantiateProposalRequest.setFcn("init");
        instantiateProposalRequest.setArgs(new String[]{"a", "500", "b", "200"});

        // TODO: set endorsement policy
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();

        logger.info("[run chain][send instantiate proposal]");
        Collection<ProposalResponse> proposalResponses = null;
        try {
            proposalResponses = chain.sendInstantiationProposal(instantiateProposalRequest);
            handleResponses(proposalResponses);
        } catch (Exception e) {
            logger.error("[run chain][fail]", e);
            fail();
        }

        // send transaction to orderer
        logger.info("[run chain][send transaction to orderer]");
        try {
            chain.sendTransaction(proposalResponses, chain.getOrderers()).thenApply(transactionEvent -> {
                assertTrue(transactionEvent.isValid());
                logger.info("[run chain][send instantiate proposal][success]TxID:{}", transactionEvent.getTransactionID());

                try {
                    logger.info("[run chain][send transaction proposal][start]");
                    TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
                    // change user
//                    client.setUserContext(user);
                    transactionProposalRequest.setChaincodeID(chainCodeID);
                    transactionProposalRequest.setFcn("invoke");
                    transactionProposalRequest.setArgs(new String[] {"move", "a", "b", "100"});
                    logger.info("[run chain][send transaction proposal]sending transactionProposal to all peers with arguments: move(a,b,100)");
                    Collection<ProposalResponse> responses = chain.sendTransactionProposal(transactionProposalRequest, chain.getPeers());
                    handleResponses(responses);

                    logger.info("[run chain][send transaction proposal to orderer]");
                    return chain.sendTransaction(responses).get();

                } catch (Exception e) {
                    logger.error("[run chain][fail]", e);
                    fail();
                }

                return null;
            }).thenApply(transactionEvent -> {
                assertTrue(transactionEvent.isValid());
                logger.info("[run chain][send transaction proposal][success]TxID:{}", transactionEvent.getTransactionID());

                // send query proposal
                QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
                queryByChaincodeRequest.setArgs(new String[] {"query", "b"});
                queryByChaincodeRequest.setFcn("invoke");
                queryByChaincodeRequest.setChaincodeID(chainCodeID);

                try {
                    Collection<ProposalResponse> queryProposals = chain.queryByChaincode(queryByChaincodeRequest, chain.getPeers());
                    handleResponses(queryProposals);

                    queryProposals.stream().forEach((response -> {
                        logger.info("[run chain][send transaction proposal]Response:{}", response.getProposalResponse().getResponse().getPayload().toStringUtf8());
                    }));
                } catch (Exception e) {
                    logger.error("[run chain][fail]", e);
                }

                return null;
            }).get();
        } catch (Exception e) {
            logger.error("[run chain][fail]", e);
            fail();
        }
    }

    private void handleResponses(Collection<ProposalResponse> responses) {
        responses.stream().forEach((response) -> {
            if(response.getStatus().equals(ProposalResponse.Status.SUCCESS)) {
                logger.info("[run chain][proposal][success]TxID:{}, Peer:{}",
                        response.getTransactionID(), response.getPeer().getName());
            } else {
                logger.info("[run chain][proposal][fail]TxID:{}, Peer:{}",
                        response.getTransactionID(), response.getPeer().getName());
            }
        });
    }

    @Test
    public void queryTest() throws InvalidArgumentException, EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, MalformedURLException, CryptoException, ProposalException, TransactionException {
        // set up client
        client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        caClient = new HFCAClient(config.getString("ca.location"), null);
        caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

        // user registration
        admin = new TestUser("admin");
        admin.setMSPID("Org0MSP");
        admin.setEnrollment(caClient.enroll("admin", "adminpw"));

        // user cont ext
        client.setUserContext(admin);

        // query demo
        Chain chain = client.newChain("mychannel");
        chain.addOrderer(client.newOrderer("orderer.example.com", "grpc://localhost:7050"));
        chain.addPeer(client.newPeer("peer0.org1.example.com", "grpc://localhost:7051"));
//        chain.addPeer(client.newPeer("peer1.org1.example.com", "grpc://localhost:8051"));
        chain.initialize();

        // send query proposal
        ChainCodeID chainCodeID = ChainCodeID.newBuilder()
                .setName("mycc")
                .setVersion("1.0")
                .setPath("github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02")
                .build();
        QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
        queryByChaincodeRequest.setChaincodeID(chainCodeID);
        queryByChaincodeRequest.setArgs(new String[] {"a"});
        queryByChaincodeRequest.setFcn("query");

        try {
            Collection<ProposalResponse> queryProposals = chain.queryByChaincode(queryByChaincodeRequest, chain.getPeers());
            handleResponses(queryProposals);

            queryProposals.stream().forEach((response -> {
                logger.info("[run chain][send transaction proposal]Response:{}", response.getProposalResponse().getResponse().getPayload().toStringUtf8());
            }));
        } catch (Exception e) {
            logger.error("[run chain][fail]", e);
        }
    }
}
