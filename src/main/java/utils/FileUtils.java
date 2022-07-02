package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author chenzhen
 * 2022/7/4
 * email:1351170669@qq.com
 */
public class FileUtils {

    public static String readFile(String fileName) {
        String content = null;
        try {
            content = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
