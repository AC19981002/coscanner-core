import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
/**
 * @author chenzhen
 * @date 2022/7/2 7:58 下午
 * @Email 1351170669@qq.com
 */

@RunWith(JUnit4.class)
public class DemoTest {

    private static final Logger log = Logger.getLogger(DemoTest.class);
    @Test
    public void log4jTest(){
        log.debug("test demo");
    }
}
