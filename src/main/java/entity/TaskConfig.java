package entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenzhen
 * 2022/7/4
 * email:1351170669@qq.com
 */

@Data
@AllArgsConstructor
public class TaskConfig {

    @SerializedName("repoURL")
    private String repoUrl = "";
    @SerializedName("commitStart")
    private String commitStrat = "";
    @SerializedName("commitEnd")
    private String commitEnd = "";
    @SerializedName("taskCreator")
    private String taskCreator = "";

    private void TaskConfig(){}

}
