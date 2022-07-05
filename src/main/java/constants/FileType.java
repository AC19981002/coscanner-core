package constants;

import lombok.Data;

/**
 * @author chenzhen
 * @date 2022/7/5 10:27 上午
 * @Email 1351170669@qq.com
 */
//TODO
// a set of prefix and suffix
// like .java .properties or .class...
public enum FileType {

    JAVA("java"),
    PROPERTY("properties"),
    ClASS("class");

    private  String fileType;

    private FileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return this.fileType;
    }

}
