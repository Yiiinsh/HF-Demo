package org.tju.HFDemo.core.demo;

import com.google.common.hash.Hashing;
import org.junit.Test;
import org.tju.HFDemo.core.AbstractTest;
import org.tju.HFDemo.core.utils.SecretUtil;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Created by shaohan.yin on 04/05/2017.
 */
public class HasingTest extends AbstractTest {
    @Test
    public void hasingTest() {
        String expected = Hashing.sha256().hashString("123456", StandardCharsets.UTF_8).toString();
        String target = SecretUtil.secretHash("123456");
        assertEquals(expected, target);
    }
}
