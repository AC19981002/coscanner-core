package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenzhen
 * 2022/7/4
 * email:1351170669@qq.com
 */
public class FileUtils {

    //Java 11 写法 文件大小不能超过2G

    public static String readFile(String fileName) {
        String content = null;
        try {
            content = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeToFile(String filePath, String content) {
        try {
            Files.writeString(Paths.get(filePath), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO explore files by keyword
    public static List<File> exploreFile(String dirPath, String fileType) {
        return exploreAllFile(dirPath)
                .stream()
                .filter(file -> file.getName().substring(file.getName().lastIndexOf(".")+1).equals(fileType))
                .collect(Collectors.toList());
    }

    public static List<File> exploreAllFile(String dirPath) {
        List<File> fileList = new ArrayList<>();
        dfsGetAllFiles(new File(dirPath), fileList);
        return fileList;
    }

    private static List<File> dfsGetAllFiles(File file, List<File> fileList) {
        if (file.isFile()) {
            fileList.add(file);
            return fileList;
        } else if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.isFile()) {
                    fileList.add(listFile);
                } else {
                    dfsGetAllFiles(listFile, fileList);
                }
            }
        }
        return fileList;
    }


}
