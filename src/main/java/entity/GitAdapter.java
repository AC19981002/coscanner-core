package entity;

import framework.PropertityMannager;
import lombok.Getter;
import lombok.Setter;
import utils.StringUtils;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chenzhen
 * 2022/7/6
 * email:1351170669@qq.com
 */

@Setter
@Getter
public class GitAdapter {
    private static final Logger logger = Logger.getLogger(GitAdapter.class);

    private String FEATURE = "dev";
    private static final String MASTER_BRANCH = "master";

    // 远程仓库路径  用的就是.git
    private String remotePath;

    // 本地仓库路径  包含了项目工程名projectName
    private String localPath;

    private String localGitPath;

    private Git git;

    private Repository repository;

    private Ref branchRef;

    public String branchName;
    //  Git授权
    private static UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider;

    private static final String username ="1351170669@qq.com";

    private static final String password ="17314547ad6cc99fec2ec0597ccecad3";
    /**
     * 构造函数：没有传分支信息则默认拉取master代码
     * @param remotePath
     * @param localPath
     */
    public GitAdapter(String remotePath, String localPath) {
        this(remotePath,localPath,MASTER_BRANCH);
    }

    public GitAdapter(String remotePath, String localPath, String branchName) {
        this.remotePath = remotePath;
        this.localPath = localPath+"/"+StringUtils.getGitProjectName(remotePath);
        this.branchName = branchName;
        localGitPath = this.localPath+"/.git";
        // 鉴权账户密码可用自己gitHub的账户密码，或者是设置token
        usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
    }

    /**
     * 使用Git时需要先初始化git
     * 默认初始化的时候会自动拉取 @branchName 的最新代码
     * @return
     */
    public Git initGit() {
        File file = new File(localPath);
        logger.info("文件路径"+localPath);
        // 如果文件存在 说明已经拉取过代码,则拉取最新代码
        if(file.exists()) {
            try {
                git = Git.open(new File(localPath));
//                判断是否是最新代码 判断是否是最新代码好像耗时更久？？！
//                TMD屁事真多，直接拉！！！
//                boolean isLatest = checkBranchNewVersion(git.getRepository().exactRef(FEATURE+branchName));
//                if (isLatest==true) {
//                    logger.info("the local version is latest, need not pull it");
//                } else {
//                     拉取最新的提交
                    git.pull().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
                    logger.info("pull success");
//                }
            } catch (CanceledException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                logger.info("pull failure");
                e.printStackTrace();
            } catch (IOException e ) {
                logger.info("pull failure");
                e.printStackTrace();
            }
        }
        // 文件不存在，说明未拉取过代码 则拉取最新代码
        else {
            try {
                git = Git.cloneRepository()
                        .setCredentialsProvider(usernamePasswordCredentialsProvider)
                        .setURI(remotePath)
                        .setBranch(branchName)
                        .setDirectory(new File(localPath))
                        .call();
                // 拉取最新的提交
                git.pull().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
                logger.info("down success");
            } catch (GitAPIException e) {
                logger.error("远程仓库下载异常");
                e.printStackTrace();
            }
        }
        repository = git.getRepository();
        return git;
    }

    /**
     * 获取ref信息
     * @return
     * @throws IOException
     */
    public Ref getBranchRef() throws IOException {
        return getBranchRef(this.branchName);
    }

    /**
     * 根据branch 获取ref信息
     * @param branchName
     * @return
     */
    public Ref getBranchRef(String branchName) {
        try {
            return this.branchRef = git.getRepository().exactRef(branchName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定分支的指定文件内容
     * @param branchName        分支名称
     * @param javaPath          文件路径
     * @return  java类
     * @throws IOException
     */
    public String getBranchSpecificFileContent(String branchName, String javaPath) throws IOException {
        Ref branch = repository.exactRef( FEATURE + branchName);
        ObjectId objId = branch.getObjectId();
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseTree(objId);
        return  getFileContent(javaPath,tree,walk);
    }

    /**
     * 获取指定分支指定的指定文件内容
     * @param javaPath      件路径
     * @param tree          git RevTree
     * @param walk          git RevWalk
     * @return  java类
     * @throws IOException
     */
    private String getFileContent(String javaPath,RevTree tree,RevWalk walk) throws IOException {
        TreeWalk treeWalk = TreeWalk.forPath(repository, javaPath, tree);
        ObjectId blobId = treeWalk.getObjectId(0);
        ObjectLoader loader = repository.open(blobId);
        byte[] bytes = loader.getBytes();
        walk.dispose();
        return new String(bytes);
    }

    /**
     * 分析分支树结构信息
     * @param localRef      本地分支
     * @return
     * @throws IOException
     */
    public AbstractTreeIterator prepareTreeParser(Ref localRef) throws IOException {
        return prepareTreeParser(localRef.getObjectId().getName());
    }

    /**
     * 通过CommitId获取分支树结构信息
     * 此方法是为了兼容
     * 被比较的分支（一般为master分支）的commitId已知的情况下，无需在获取Ref直接通过commitId获取分支树结构
     * @param commitId
     * @return
     * @throws IOException
     */
    public AbstractTreeIterator prepareTreeParser(String commitId) throws IOException {
        RevWalk walk = new RevWalk(repository);
        RevCommit revCommit = walk.parseCommit(repository.resolve(commitId));
        RevTree tree = walk.parseTree(revCommit.getTree().getId());
        CanonicalTreeParser treeParser = new CanonicalTreeParser();
        ObjectReader reader = repository.newObjectReader();
        treeParser.reset(reader, tree.getId());
        walk.dispose();
        return treeParser;
    }

    /**
     * 切换分支
     * @param branchName    分支名称
     * @throws GitAPIException GitAPIException
     */
    public void checkOut(String branchName) throws GitAPIException {
        //  切换分支
        git.checkout().setCreateBranch(false).setName(branchName).call();
    }

    /**
     * 更新分支代码
     * @param branchName    分支名称
     * @throws GitAPIException GitAPIException
     */
    public Ref checkOutAndPull(String branchName) {
        // 1. 获取此分支的Ref信息
        Ref branchRef = getBranchRef(branchName);
        boolean isCreateBranch = branchRef == null;
        // 2. 如果Ref不为null，则校验Ref是否为最新，最新直接返回，不为最新则重新拉取分支信息
        try {
            if (!isCreateBranch && checkBranchNewVersion(branchRef)) {
                return branchRef;
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        //  3. 切换分支
        try {
            git.checkout().setCreateBranch(isCreateBranch).setName(branchName)
                    .setStartPoint( "origin/"+branchName)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        //  4. 拉取最新代码
        try {
            git.pull().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        branchRef = getBranchRef(branchName);
        return branchRef;
    }


    /**
     * 切换到目标分支的版本中（暂时不用）
     * @param versionCommit
     * @throws IOException
     * @throws GitAPIException
     */
    public void checkoutFromVersionCommit(String versionCommit) throws IOException, GitAPIException {
        RevWalk walk = new RevWalk(repository);
        ObjectId versionId = repository.resolve(versionCommit);
        RevCommit verCommit = walk.parseCommit(versionId);
        git.checkout().setCreateBranch(false).setName(versionCommit).setStartPoint(verCommit).call();
        git.branchDelete().setBranchNames(versionCommit);

        Collection<Ref> remoteRefs = git.lsRemote().setHeads(true).setCredentialsProvider(usernamePasswordCredentialsProvider).call();
        String ref = git.getRepository().getRefDatabase().getRefs().toString();
        System.out.println(ref);
    }

    /**
     * 判断本地分支是否是最新版本。目前不考虑分支在远程仓库不存在，本地存在
     * 此方法有点耗时，可以考虑直接拉取最新版本
     * @param localRef  本地分支
     * @return  boolean
     * @throws GitAPIException GitAPIException
     */

    private boolean checkBranchNewVersion(Ref localRef) throws GitAPIException {
        String localRefName = localRef.getName(); //  refs/heads/master 分支名
        String localRefObjectId = localRef.getObjectId().getName(); //commit_id
        //  获取远程所有分支
        Collection<Ref> remoteRefs = git.lsRemote().setCredentialsProvider(usernamePasswordCredentialsProvider).setHeads(true).call();
        for (Ref remoteRef : remoteRefs) {
            String remoteRefName = remoteRef.getName();
            String remoteRefObjectId = remoteRef.getObjectId().getName();
            if (remoteRefName.equals(localRefName)) {
                if (remoteRefObjectId.equals(localRefObjectId)) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    /**
     * 获取当前分支的所有提交
     * @return
     * @throws IOException
     * @throws GitAPIException
     */

    public List<RevCommit> getCommitList() {
        List<RevCommit> commitList = new ArrayList<>();
        Iterable<RevCommit> commits = null;
        try {
            commits = git.log().all().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        RevWalk walk = new RevWalk(repository);
        int flag =0;
        for(RevCommit commit:commits) {
            commitList.add(commit);
        }
        return commitList;
    }

    public List<Ref> getRefList() {
        if(git!=null){
            try {
                return git.branchList().call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
        logger.debug("init git first.");
        return null;
    }

}

