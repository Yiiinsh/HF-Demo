package org.tju.HFDemo.core.manager.ca;

import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.tju.HFDemo.core.exception.HFDRuntimeException;
import org.tju.HFDemo.core.exception.InvalidEnrollException;
import org.tju.HFDemo.core.manager.AbstractManager;
import org.tju.HFDemo.core.role.User;
import org.tju.HFDemo.core.utils.SecretUtil;

import java.net.MalformedURLException;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public class DefaultCAManager extends AbstractManager implements CAManager {
    private HFCAClient hfcaClient;

    public DefaultCAManager() {
        try {
            hfcaClient = new HFCAClient(config.getCALocation(), null);
            hfcaClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        } catch (MalformedURLException e) {
            logger.error("[DefaultCAManager][fail]", e);
        }
    }

    public DefaultCAManager(HFCAClient hfcaClient) {
        this.hfcaClient = hfcaClient;
    }

    @Override
    public User enroll(String userName, String passwd) {
        User user = User.newDefaultUser(userName);
        user.setAffiliation(config.getUserAffiliation());
        user.setMSPID(config.getUserMSPID());
        try {
            user.setEnrollment(hfcaClient.enroll(userName, SecretUtil.secretHash(passwd)));
        } catch (Exception e) {
            logger.error("[enroll][fail]", e);
            throw new InvalidEnrollException(String.format("Fail to enroll user:%s with password:%s", userName, passwd));
        }
        return user;
    }

    @Override
    public void register(String userName, String passwd, User registar) {
        try {
            RegistrationRequest rr = new RegistrationRequest(userName, config.getUserAffiliation());
            rr.setSecret(SecretUtil.secretHash(passwd));
            rr.setMaxEnrollments(0);
            hfcaClient.register(rr, registar);
        } catch (Exception e) {
            logger.error("[register][fail]UserName:{}, Registar:{}", userName, registar.getName());
            throw new HFDRuntimeException(String.format("Fail to register user:%s, registar:%s", userName, registar.getName()));
        }

    }
}
