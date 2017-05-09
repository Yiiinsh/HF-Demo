package org.tju.HFDemo.core.config;

import org.tju.HFDemo.common.config.Config;
import org.tju.HFDemo.common.config.DefaultConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by shaohan.yin on 28/04/2017.
 */
public class DefaultHFDConfig extends DefaultConfig implements HFDConfig {
    private Config config = Config.DEFAULT;

    private static final String CA_LOCATION_KEY = "ca.location";
    private static final String ORDERER_NAME_KEY = "orderer.name";
    private static final String ORDERER_LOCATION_KEY = "orderer.location";
    private static final String USER_MSPID_KEY = "user.mspid";
    private static final String USER_AFFILIATION_KEY = "user.affiliation";
    private static final String CHAIN_NAMES_KEY = "chain.names";
    private static final String PEER_NAMES_KEY = "peer.names";
    private static final String USER_ADMIN_NAME = "user.admin.name";
    private static final String USER_ADMIN_SECRET = "user.admin.secret";

    @Override
    public String getCALocation() {
        return config.getString(CA_LOCATION_KEY, "http://localhost:7054");
    }

    @Override
    public String getOrdererName() {
        return config.getString(ORDERER_NAME_KEY, "orderer.example.com");
    }

    @Override
    public String getOrdererLocation() {
        return config.getString(ORDERER_LOCATION_KEY, "grpc://localhost:7050");
    }

    @Override
    public List<String> getPeerNames() {
        return config.getList(String.class, PEER_NAMES_KEY, new LinkedList<>());
    }

    @Override
    public String getPeerLocation(String peerName) {
        return config.getString(peerName + ".location", "");
    }

    @Override
    public String getPeerEventHub(String peerName) {
        return config.getString(peerName + ".eventhub.location", "");
    }

    @Override
    public String getUserMSPID() {
        return config.getString(USER_MSPID_KEY, "Org0MSP");
    }

    @Override
    public String getUserAffiliation() {
        return config.getString(USER_AFFILIATION_KEY, "org1");
    }

    @Override
    public List<String> getChainNames() {
        return config.getList(String.class, CHAIN_NAMES_KEY, new LinkedList<>());
    }

    @Override
    public String getChainCodeName(String chainName) {
        return config.getString(chainName + ".chaincode.name", "mycc");
    }

    @Override
    public String getChainCodeVersion(String chainName) {
        return config.getString(chainName + ".version", "1");
    }

    @Override
    public String getChainCodePath(String chainName) {
        return config.getString(chainName + ".chaincode.path", "");
    }

    @Override
    public String getAdminName() {
        return config.getString(USER_ADMIN_NAME, "123456");
    }

    @Override
    public String getAdminSecret() {
        return config.getString(USER_ADMIN_SECRET, "123456");
    }
}
