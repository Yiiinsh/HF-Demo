package org.tju.HFDemo.common.config;

import org.junit.Test;
import org.tju.HFDemo.common.AbstractTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shaohan.yin on 17/04/2017.
 */
public class DefaultConfigTest extends AbstractTest {
    private Config config = Config.DEFAULT;

    private static final String DEFAULT_TEST_KEY = "default.test";
    private static final String DEFAULT_TEST_VALUE = "default.value";
    private static final String DEFAULT_TEST_LIST_KEY = "default.test.list";

    @Test
    public void defaultConfigTest() {
        assertNotNull(config);
        assertEquals(DEFAULT_TEST_VALUE, config.getString(DEFAULT_TEST_KEY));
    }

    @Test
    public void listConfigTest() {
        List<String> res = config.getList(String.class, DEFAULT_TEST_LIST_KEY);
        assertEquals(3, res.size());
        assertEquals("a", res.get(0));
        assertEquals("b", res.get(1));
        assertEquals("c", res.get(2));
    }
}
