package constants;

/**
 * @author chenzhen
 * @date 2022/7/5 10:24 上午
 * @Email 1351170669@qq.com
 */

//TODO collect GramFeatures from rpc ,restful api and mq;
public class GramFeatures {
    enum Annotation{

        CONTROLLER("@controller");

        private String value;
        private Annotation(String value) {
            this.value = value;
        }
    }
}
