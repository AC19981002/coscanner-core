package framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import constants.FileType;
import utils.FileUtils;


/**
 * @author chenzhen
 * @date 2022/7/5 10:56 上午
 * @Email 1351170669@qq.com
 */
//TODO 把所有的properties持久化到Mannager方便管理
public class PropertityMannager {
    public HashMap<String, String> properties = new HashMap<String, String>();
    private static final String dirPath = "src/main/resources";

    public PropertityMannager() {
        for (File file : FileUtils.exploreFile(dirPath, FileType.PROPERTY.getFileType())) {
            Properties properties = new Properties();
            try {
                FileInputStream in = new FileInputStream(file);
                properties.load(in);
                in.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            properties.forEach((key, value) -> this.properties.put((String) key, (String)value));
        }
    }

}
