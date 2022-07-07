import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;

import entity.GitAdapter;
import framework.PropertityMannager;

/**
 * @author chenzhen
 * @date 2022/7/7 2:09 下午
 * @Email 1351170669@qq.com
 */
@RunWith(JUnit4.class)
public class GitAdapterTest {

    private static final Logger log = Logger.getLogger(GitAdapterTest.class);

    private static final PropertityMannager properties = new PropertityMannager();

    @Test
    public void gitInitTest() throws GitAPIException, IOException {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo","master");
        gitAdapter.initGit();
    }

    @Test
    public void gitCheckoutAndPullTest() throws GitAPIException, IOException {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo","master");
        gitAdapter.initGit();
        gitAdapter.checkOutAndPull("dev");
    }

    @After
    public void delete(){
        try {
            Thread.sleep(10000);
            org.apache.commons.io.FileUtils.forceDelete(new File("src/main/resources/repo"));
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }


}
