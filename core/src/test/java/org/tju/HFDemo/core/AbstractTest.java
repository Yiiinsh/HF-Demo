package org.tju.HFDemo.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tju.HFDemo.common.config.Config;

/**
 * Created by shaohan.yin on 18/04/2017.
 */
public class AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Config config = Config.DEFAULT;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void abstractSetUp() {
        logger.info(remarkableString("[Begin Test][{}]"), testName.getMethodName());
    }

    @After
    public void abstractTearDown() {
        logger.info(remarkableString("[End   Test][{}]"), testName.getMethodName());
    }

    protected String remarkableString(String msg) {
        return String.format("--------------------------%s--------------------------\r\n", msg);
    }
}
