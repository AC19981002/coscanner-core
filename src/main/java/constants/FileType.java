package constants;

/**
 * @author chenzhen
 * @date 2022/7/5 10:27 上午
 * @Email 1351170669@qq.com
 */
//TODO
// a set of prefix and suffix
// like .java .properties or .class...

public enum FileType {

    JAVA(".java"),
    PROPERTY(".properties"),
    ClASS(".class");

    private final String keyword;

    private FileType(String keyword) {
        this.keyword = keyword;
    }

}
