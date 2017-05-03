package org.tju.HFDemo.core.demo;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tju.HFDemo.common.config.Config;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.role.TestUser;

import java.util.Collection;

import static org.junit.Assert.fail;

/**
 * Created by shaohan.yin on 29/04/2017.
 */
@Ignore
public class HFSDKTest extends AbstractTest {
    private Config config = Config.DEFAULT;

    private final static String TEST_CA_LOCATION_KEY = "test.ca.location";
    private final static String TEST_ORDERER_NAME_KEY = "test.orderer.name";
    private final static String TEST_ORDERER_LOCATION_KEY = "test.orderer.location";
    private final static String TEST_PEERS_NAME_KEY = "test.peers.name";
    private final static String TEST_PEERS_LOCATION_PREFIX = "test.";
    private final static String TEST_PEERS_LOCATION_SUFFIX = ".location";
    private final static String TEST_EVENTHUB_SUFFIX = ".eventhub.location";
    private final static String TEST_PEERS_ENDORSERS_NAME_KEY = "test.peers.endorsers.name";
    private final static String TEST_QUERY_PEER_NAME_KEY = "test.query.peer.name";

    private final static String TEST_USER_MSPID_KEY = "test.user.mspid";
    private final static String TEST_USER_ADMIN_NAME_KEY = "test.user.admin.name";
    private final static String TEST_USER_ADMIN_PASSWORD_KEY = "test.user.admin.password";
    private final static String TEST_USER_USERA_NAME_KEY = "test.user.usera.name";
    private final static String TEST_USER_USERA_PASSWORD_KEY = "test.user.usera.password";
    private final static String TEST_USER_USERA_AFFILIATION_KEY = "test.user.usera.affiliation";

    private final static String TEST_CHAIN_NAME_KEY = "test.chain.name";
    private final static String TEST_CHAINCODE_NAME_KEY = "test.chaincode.name";
    private final static String TEST_CHAINCODE_VERSION_KEY = "test.chaincode.version";
    private final static String TEST_CHAINCODE_PATH_KEY = "test.chaincode.path";

    private HFClient client;
    private HFCAClient caClient;
    private TestUser admin;
    private TestUser userA;
    private TestUser userB;
    private ChainCodeID chainCodeID;

    @Before
    public void setUp() {
        try {
            logger.debug("[setUp][initialize client]");
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            caClient = new HFCAClient(getConfig(TEST_CA_LOCATION_KEY), null);
            caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

            logger.debug("[setUp][admin user]");
            admin = new TestUser(getConfig(TEST_USER_ADMIN_NAME_KEY));
            admin.setMSPID(getConfig(TEST_USER_MSPID_KEY));
            admin.setEnrollment(caClient.enroll(getConfig(TEST_USER_ADMIN_NAME_KEY), getConfig(TEST_USER_ADMIN_PASSWORD_KEY)));

            logger.debug("[setUp][register userA]");
            RegistrationRequest rr = new RegistrationRequest(getConfig(TEST_USER_USERA_NAME_KEY), getConfig(TEST_USER_USERA_AFFILIATION_KEY));
            rr.setMaxEnrollments(0);
            rr.setSecret(getConfig(TEST_USER_USERA_PASSWORD_KEY));;
            caClient.register(rr, admin);
            userA = new TestUser(getConfig(TEST_USER_USERA_NAME_KEY));
            userA.setMSPID(getConfig(TEST_USER_MSPID_KEY));
            userA.setEnrollment(caClient.enroll(getConfig(TEST_USER_USERA_NAME_KEY), getConfig(TEST_USER_USERA_PASSWORD_KEY)));

            logger.debug("[queryTest][setUserContext]admin");
            client.setUserContext(admin);

            logger.debug("[queryTest][createChainConfig]");
            Chain chain = client.newChain(getConfig(TEST_CHAIN_NAME_KEY));
            chain.addOrderer(client.newOrderer(getConfig(TEST_ORDERER_NAME_KEY), getConfig(TEST_ORDERER_LOCATION_KEY)));
            config.getList(String.class, TEST_PEERS_NAME_KEY).stream().forEach((peer) -> {
                try {
                    chain.addPeer(client.newPeer(peer, getConfig(TEST_PEERS_LOCATION_PREFIX + peer + TEST_PEERS_LOCATION_SUFFIX)));
                    // TODO: event hub for async call
//                    chain.addEventHub(client.newEventHub(peer, getConfig(TEST_PEERS_LOCATION_PREFIX + peer + TEST_EVENTHUB_SUFFIX)));
                } catch (Exception e) {
                    logger.error("[queryTest][add peer][fail]Peer:{}", peer, e);
                    fail();
                }
            });
            chain.initialize();

            logger.debug("[queryTest][createChainCodeID");
            chainCodeID = ChainCodeID.newBuilder()
                    .setName(getConfig(TEST_CHAINCODE_NAME_KEY))
                    .setVersion(getConfig(TEST_CHAINCODE_VERSION_KEY))
                    .setPath(getConfig(TEST_CHAINCODE_PATH_KEY))
                    .build();

        } catch (Exception e) {
            logger.error("[setUp][failed]", e);
            fail();
        }
    }

    @Test
    public void queryTest() {
        try {
            client.setUserContext(userA);

            logger.debug("[queryTest][send query proposal]");
            QueryByChaincodeRequest queryByChaincodeRequest = client.newQueryProposalRequest();
            queryByChaincodeRequest.setChaincodeID(chainCodeID);
            queryByChaincodeRequest.setArgs(new String[] {"a"});
            queryByChaincodeRequest.setFcn("query");

            Collection<ProposalResponse> queryProposalResponses = client.getChain(getConfig(TEST_CHAIN_NAME_KEY))
                    .queryByChaincode(queryByChaincodeRequest);
            handleResponses(queryProposalResponses);
            queryProposalResponses.stream().forEach(response -> {
                logger.info("[queryTest][send transaction proposal]Response:{}",
                        response.getProposalResponse().getResponse().getPayload().toStringUtf8());
            });

        } catch (Exception e) {
            logger.error("[queryTest][fail]", e);
            fail();
        }
    }

    @Test
    public void invokeTest() {
        try {
            client.setUserContext(userA);

            logger.debug("[invokeTest][send transaction proposal]");
            TransactionProposalRequest transactionProposalRequest = client.newTransactionProposalRequest();
            transactionProposalRequest.setChaincodeID(chainCodeID);
            transactionProposalRequest.setFcn("invoke");
            transactionProposalRequest.setArgs(new String[]{"a", "b", "10"});

            Collection<ProposalResponse> transactionResponses = client.getChain(getConfig(TEST_CHAIN_NAME_KEY))
                    .sendTransactionProposal(transactionProposalRequest);
            handleResponses(transactionResponses);

            logger.debug("[invokeTest][send transaction to orderer");
            // TODO: async get fail because of eventhub setup
//            BlockEvent.TransactionEvent res = client.getChain(getConfig(TEST_CHAIN_NAME_KEY)).sendTransaction(transactionResponses).get();
//            assertTrue(res.isValid());
            client.getChain(getConfig(TEST_CHAIN_NAME_KEY)).sendTransaction(transactionResponses);
//            logger.info("[invokeTest][send transaction proposal][success]TxID:{}", res.getTransactionID());

            queryTest();
        } catch (Exception e) {
            logger.error("[invokeTest][fail]", e);
            fail();
        }
    }

    private String getConfig(String key) {
        return null == key? "" : config.getString(key);
    }

    private void handleResponses(Collection<ProposalResponse> responses) {
        responses.stream().forEach((response) -> {
            if(response.getStatus().equals(ProposalResponse.Status.SUCCESS)) {
                logger.info("[proposal][success]TxID:{}, Peer:{}",
                        response.getTransactionID(), response.getPeer().getName());
            } else {
                logger.info("[proposal][fail]TxID:{}, Peer:{}",
                        response.getTransactionID(), response.getPeer().getName());
            }
        });
    }
}
