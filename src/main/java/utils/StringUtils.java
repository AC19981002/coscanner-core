package utils;

/**
 * @author chenzhen
 * @date 2022/7/7 11:10 上午
 * @Email 1351170669@qq.com
 */
public class StringUtils {
    /**
     * @Params eg: https://gitee.com/ac9999/demo.git
     * @Description:通过url拿到pro_name,初始化本地仓库路径
     * @return
     */
    public static String getGitProjectName(String url){
        String[] temp = url.split("/|\\.");
        return temp[temp.length-2];
    }
}
