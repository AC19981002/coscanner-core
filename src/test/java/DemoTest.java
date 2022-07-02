import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

    @Test
    public void propertiesTest(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/configs.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties.toString());
        properties.forEach((key, value) -> {
            System.out.println(key + ": "+ value.toString());
        });
        System.out.println(properties.get("git.account.username").toString());
        System.out.println(properties.get("git.account.token").toString());
    }
}
