import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shaohan.yin on 17/04/2017.
 */
public class AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        logger.info(remarkableString("[Begin Test][{}]"), testName.getMethodName());
    }

    @After
    public void tearDown() {
        logger.info(remarkableString("[End   Test][{}]"), testName.getMethodName());
    }

    protected String remarkableString(String msg) {
        return String.format("--------------------------%s--------------------------\r\n", msg);
    }
}
