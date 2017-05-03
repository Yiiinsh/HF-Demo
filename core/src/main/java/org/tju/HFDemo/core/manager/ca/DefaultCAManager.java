package org.tju.HFDemo.core.manager.ca;

import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.tju.HFDemo.core.exception.ComponentSetupException;
import org.tju.HFDemo.core.exception.HFDRuntimeException;
import org.tju.HFDemo.core.exception.InvalidEnrollException;
import org.tju.HFDemo.core.manager.AbstractManager;
import org.tju.HFDemo.core.role.User;

import java.net.MalformedURLException;

/**
 * Created by shaohan.yin on 30/04/2017.
 */
public class DefaultCAManager extends AbstractManager implements CAManager {
    private HFCAClient hfcaClient;

    public DefaultCAManager() throws ComponentSetupException {
        try {
            hfcaClient = new HFCAClient(getConfig("ca.location"), null);
        } catch (MalformedURLException e) {
            logger.error("[DefaultCAManager][fail]", e);
            throw new ComponentSetupException("Fail to setup CAManager");
        }
    }

    public DefaultCAManager(HFCAClient hfcaClient) {
        this.hfcaClient = hfcaClient;
    }

    @Override
    public User enroll(String userName, String passwd) {
        User user = User.newDefaultUser(userName);
        // TODO: set up user info
        user.setAffiliation(getConfig("user.affiliation"));
        user.setMSPID(getConfig("user.mspid"));
        try {
            user.setEnrollment(hfcaClient.enroll(userName, passwd));
        } catch (Exception e) {
            logger.error("[enroll][fail]", e);
            throw new InvalidEnrollException(String.format("Fail to enroll user:%s with password:%s", userName, passwd));
        }
        return user;
    }

    @Override
    public void register(String userName, String passwd, User registar) {
        // TODO: set up config
        try {
            RegistrationRequest rr = new RegistrationRequest(userName, getConfig("affiliation"));
            rr.setSecret(passwd);
            rr.setMaxEnrollments(0);
            hfcaClient.register(rr, registar);
        } catch (Exception e) {
            logger.error("[register][fail]UserName:{}, Registar:{}", userName, registar.getName());
            throw new HFDRuntimeException(String.format("Fail to register user:%s, registar:%s", userName, registar.getName()));
        }

    }
}
