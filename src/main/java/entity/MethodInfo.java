package entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author chenzhen
 * 2022/7/6
 * email:1351170669@qq.com
 */
@Data
@ToString
public class MethodInfo {
    /**
     * 方法的md5
     */
    public String md5;
    /**
     * 方法名
     */
    public String methodName;
    /**
     * 方法参数
     */
    public String parameters;
}

