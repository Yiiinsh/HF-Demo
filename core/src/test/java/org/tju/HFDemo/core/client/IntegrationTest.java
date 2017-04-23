package org.tju.HFDemo.core.client;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.junit.Test;
import org.tju.HFDemo.common.config.Config;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.role.Organization;
import org.tju.HFDemo.core.role.TestOrganization;
import org.tju.HFDemo.core.role.TestUser;

import java.io.File;
import java.util.Collection;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by shaohan.yin on 23/04/2017.
 */
public class IntegrationTest extends AbstractTest {
    private Config config = Config.DEFAULT;

    private static final String TEST_ADMIN_NAME = "admin";
    private static final String TEST_ADMIN_PASSWORD = "adminpw";
    private static final String TEST_USER_NAME = "user";
    private static final String TEST_USER_PASSWORD = "userpw";
    private static final String TEST_USER_AFFILIATION = "tju.student";
    private static final String TEST_ORDERER_NAME = "orderer";
    private static final String TEST_ORDERER_ADDRESS_KEY = "test.orderer.address";

    private static final String TEST_CHAINCODE_NAME = "demo_cc_go";
    private static final String TEST_CHAINCODE_PATH = "src/test/env/chaincode/gocc/demo/src/github.com/example_cc";
    private static final String TEST_CHAINCODE_VERSION = "0.0.1";

    private static final String TEST_CHAIN_NAME = "demo";


    private HFClient client;
    private HFCAClient caClient;
    private TestUser admin;
    private TestUser user;
    private TestOrganization organization;

    @Test
    public void clientIntegrationTest() {
        try {
            // set up client
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            caClient = new HFCAClient(config.getString("ca.location"), new Properties());
            client.setMemberServices(caClient);

            // user registration
            admin = new TestUser(TEST_ADMIN_NAME);
            admin.setEnrollment(caClient.enroll(TEST_USER_NAME, TEST_ADMIN_PASSWORD));
            user = new TestUser(TEST_USER_NAME);
            RegistrationRequest rr = new RegistrationRequest(TEST_USER_NAME, TEST_USER_AFFILIATION);
            rr.setMaxEnrollments(1);
            rr.setSecret(TEST_USER_PASSWORD);
            caClient.register(rr, admin);
            user.setEnrollment(caClient.enroll(TEST_USER_NAME, TEST_USER_PASSWORD));

            // new chain
            Chain demo = constructChain(TEST_CHAIN_NAME, client, organization);

            // run chain
            runChain(client, demo, organization);

            // revoke user enrollments
            caClient.revoke(admin, user.getEnrollment(), 0);
            caClient.revoke(admin, admin.getEnrollment(), 0);

        } catch (Exception e) {
            logger.error("[Client Test Fail]", e);
            fail();
        }
    }

    private Chain constructChain(String chainName, HFClient client, Organization organization) throws InvalidArgumentException, TransactionException {
        logger.info("[construct chain]name:{}, org:{}", chainName, organization);

        // set order for client
        Orderer orderer = client.newOrderer(TEST_ORDERER_NAME, config.getString(TEST_ORDERER_ADDRESS_KEY));

        // configurations
        // TODO: set up chain configuration
        ChainConfiguration chainConfiguration = new ChainConfiguration();

        // user context
        client.setUserContext(admin);

        // new chain
        logger.info("[construct chain][new chain]{}", chainName);
        Chain chain = client.newChain(chainName);
        chain.addOrderer(orderer);
        organization.getPeersName().stream().forEach((peerName) -> {
            try {
                chain.joinPeer(client.newPeer(peerName, organization.getPeer(peerName), new Properties()));
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
                .setName(TEST_CHAINCODE_NAME)
                .setVersion(TEST_CHAINCODE_VERSION)
                .setPath(TEST_CHAINCODE_PATH)
                .build();

        // install chaincode
        logger.info("[run chain][create install proposal]");
        InstallProposalRequest request = client.newInstallProposalRequest();
        request.setChaincodeID(chainCodeID);
        request.setChaincodeSourceLocation(new File(TEST_CHAINCODE_PATH));
        request.setChaincodeVersion(TEST_CHAINCODE_VERSION);

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
                    client.setUserContext(user);
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
                logger.info("[]");
            } else {
                logger.info("[run chain][proposal][fail]TxID:{}, Peer:{}",
                        response.getTransactionID(), response.getPeer().getName());
            }
        });
    }

}
