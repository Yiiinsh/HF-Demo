package org.tju.HFDemo.core.role;

import org.hyperledger.fabric_ca.sdk.HFCAClient;

import java.util.Set;

/**
 * Created by shaohan.yin on 24/04/2017.
 */
public interface Organization {

    HFCAClient getCAClient();
    void setCAClient(HFCAClient client);

    User getAdmin();
    void setAdmin(User user);

    String getMSPID();
    void setMSPID(String mspid);

    Set<String> getPeers();
    Set<String> getPeersName();
    String getPeer(String name);
    void addPeer(String name, String location);

    Set<String> getOrderers();
    Set<String> getOrderersName();
    String getOrderer(String name);
    void addOrderer(String name, String location);

}
