import com.google.gson.reflect.TypeToken;
import entity.TaskConfig;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.FileUtils;
import utils.GsonUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void gsonTest(){
        List<TaskConfig> list = new ArrayList<>();
       list =  GsonUtils.gson.fromJson(FileUtils.readFile("src/main/resources/mocktask.json"),new TypeToken<List<TaskConfig>>(){}.getType());
        for (TaskConfig taskConfig : list) {
            System.out.println(taskConfig.toString());
        }
        list.add(new TaskConfig("3","3","3","3"));
        System.out.println(GsonUtils.gson.toJson(list));
    }
}
