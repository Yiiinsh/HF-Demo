package org.tju.HFDemo.core.role;

import com.google.common.collect.ImmutableSet;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by shaohan.yin on 24/04/2017.
 */
public class TestOrganization implements Organization {

    private HFCAClient caClient;
    private User admin;
    private String mspid;
    private Map<String, String> peers = new HashMap<>();
    private Map<String, String> orderers = new HashMap<>();

    @Override
    public HFCAClient getCAClient() {
        return caClient;
    }

    @Override
    public void setCAClient(HFCAClient client) {
        this.caClient = client;
    }

    @Override
    public User getAdmin() {
        return admin;
    }

    @Override
    public void setAdmin(User user) {
        this.admin = user;
    }

    @Override
    public String getMSPID() {
        return mspid;
    }

    @Override
    public void setMSPID(String mspid) {
        this.mspid = mspid;
    }

    @Override
    public Set<String> getPeers() {
        return ImmutableSet.copyOf(peers.values());
    }

    @Override
    public Set<String> getPeersName() {
        return ImmutableSet.copyOf(peers.keySet());
    }

    @Override
    public String getPeer(String name) {
        return peers.get(name);
    }

    @Override
    public void addPeer(String name, String location) {
        peers.put(name, location);
    }

    @Override
    public Set<String> getOrderers() {
        return ImmutableSet.copyOf(peers.values());
    }

    @Override
    public Set<String> getOrderersName() { return ImmutableSet.copyOf(peers.keySet()); }

    @Override
    public String getOrderer(String name) {
        return orderers.get(name);
    }

    @Override
    public void addOrderer(String name, String location) {
        orderers.put(name, location);
    }
}
