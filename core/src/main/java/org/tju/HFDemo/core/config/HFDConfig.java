package org.tju.HFDemo.core.config;

import org.tju.HFDemo.common.config.Config;

import java.util.List;

/**
 * Created by shaohan.yin on 28/04/2017.
 */
public interface HFDConfig extends Config {
    HFDConfig DEFAULT = new DefaultHFDConfig();

    String getCALocation();
    String getOrdererName();
    String getOrdererLocation();
    List<String> getPeerNames();
    String getPeerLocation(String peerName);
    String getPeerEventHub(String peerName);
    String getUserMSPID();
    String getUserAffiliation();
    List<String> getChainNames();
    String getChainCodeName(String chainName);
    String getChainCodeVersion(String chainName);
    String getChainCodePath(String chainName);
    String getAdminName();
    String getAdminSecret();

}
