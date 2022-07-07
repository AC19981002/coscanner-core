import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    private static final PropertityMannager properties = PropertityMannager.getInstance();

    @Test
    public void gitRefListTest() {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo", "master");
        gitAdapter.initGit();
        List<Ref> call = null;
        try {
            call = gitAdapter.getGit().branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        call.forEach((ref)->{
            System.out.println("Branch:  "  + ref.getName() +"   "
                    + ref.getObjectId().getName());
        });
    }

    @Test
    public void gitRefTest() {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo", "master");
        gitAdapter.initGit();
        try {
            Ref ref = gitAdapter.getGit().getRepository().exactRef("refs/heads/dev");
            System.out.println(ref.toString());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void gitGetCommitListTest() {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo", "master");
        gitAdapter.initGit();
        List<RevCommit> baseCommit = gitAdapter.getCommitList();
        gitAdapter.checkOutAndPull("dev");
        List<RevCommit> headCommit = gitAdapter.getCommitList();
        System.out.println("-----");
    }

    @Test
    public void gitInitTest() throws GitAPIException, IOException {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo", "master");
        gitAdapter.initGit();
    }

    @Test
    public void gitCheckoutAndPullTest() throws GitAPIException, IOException {
        GitAdapter gitAdapter = new GitAdapter("https://gitee.com/ac9999/demo.git", "src/main/resources/repo", "master");
        gitAdapter.initGit();
        gitAdapter.checkOutAndPull("dev");
    }

    public void delete() {
        try {
            Thread.sleep(10000);
            org.apache.commons.io.FileUtils.forceDelete(new File("src/main/resources/repo"));
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }


}
