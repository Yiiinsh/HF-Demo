package org.tju.HFDemo.common.config;

import org.tju.HFDemo.common.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by shaohan.yin on 17/04/2017.
 */
public class DefaultConfigTest extends AbstractTest {
    private Config config = Config.DEFAULT;

    private static final String DEFAULT_TEST_KEY = "default.test";
    private static final String DEFAULT_TEST_VALUE = "default.value";

    @Test
    public void defaultConfigTest() {
        assertNotNull(config);
        assertEquals(DEFAULT_TEST_VALUE, config.getString(DEFAULT_TEST_KEY));
    }
}
