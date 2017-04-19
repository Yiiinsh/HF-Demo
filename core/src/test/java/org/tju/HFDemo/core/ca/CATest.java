package org.tju.HFDemo.core.ca;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.role.TestUser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by shaohan.yin on 18/04/2017.
 */
@Ignore
public class CATest extends AbstractTest {
    public static final String TEST_KEY_CA_LOCATION = "ca.location";
    public static final String TEST_PREDEFINED_ADMIN = "admin";
    public static final String TEST_PREDEFINED_ADMIN_PASSWD = "adminpw";
    public static final String TEST_UNDEFINED_USER = "undefined";
    public static final String TEST_UNDEFINED_USER_PASSWD = "undefinedpw";

    private HFClient client;
    private HFCAClient caClient;
    private TestUser admin;


    @Before
    public void setUp() throws Exception {
        client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        caClient = new HFCAClient(config.getString(TEST_KEY_CA_LOCATION), null);
        client.setMemberServices(caClient);

        admin = new TestUser("admin");
        admin.setEnrollment(caClient.enroll(TEST_PREDEFINED_ADMIN, TEST_PREDEFINED_ADMIN_PASSWD));
    }

    @Test
    public void enrollSuccessTest() {
        try {
            caClient.enroll(TEST_PREDEFINED_ADMIN, TEST_PREDEFINED_ADMIN_PASSWD);
        } catch (Exception e) {
            logger.error("[enroll fail]", e);
            fail();
        }
    }

    @Test
    public void enrollFailTest() {
        try {
            caClient.enroll(TEST_UNDEFINED_USER, TEST_UNDEFINED_USER_PASSWD);
            fail();
        } catch (Exception e) {
            logger.error("[expected enroll fail]", e);
            assertTrue(e instanceof EnrollmentException);
        }
    }

    @Test
    public void registerSuccessTest() {
        try {
            RegistrationRequest rr = new RegistrationRequest("test-user", "tju.student");
            rr.setMaxEnrollments(1);
            rr.setSecret("test-userpw");
            caClient.register(rr, admin);

            caClient.revoke(admin, caClient.enroll("test-user", "test-userpw"), -1);

        } catch (Exception e) {
            logger.error("[register fail]", e);
            fail();
        }
    }

    @Test
    public void registerFailTest() {
        try {
            caClient.register(new RegistrationRequest(TEST_PREDEFINED_ADMIN, "tju.student"), admin);
            fail();
        } catch (Exception e) {
            logger.error("[expected register fail]", e);
        }
    }

}
