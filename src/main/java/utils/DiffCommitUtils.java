package utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhen
 * @date 2022/7/11 5:51 下午
 * @Email 1351170669@qq.com
 */
public class DiffCommitUtils {

    public static List<DiffEntry> diffCommitEntrys(RevCommit newCommit, RevCommit oldCommit, Git git) {
        ObjectReader reader = git.getRepository().newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        ObjectId oldTree = newCommit.getTree(); // equals newCommit.getTree()
        try {
            oldTreeIter.reset(reader, oldTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        ObjectId newTree = oldCommit.getTree(); // equals oldCommit.getTree()
        try {
            newTreeIter.reset(reader, newTree);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DiffFormatter df = new DiffFormatter(new ByteArrayOutputStream()); // use NullOutputStream.INSTANCE if you don't need the diff output
        df.setRepository(git.getRepository());
        List<DiffEntry> entries = null;
        try {
            entries = df.scan(oldTreeIter, newTreeIter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<DiffEntry> list = new ArrayList<>();
        for (DiffEntry entry : entries) {
            list.add(entry);
        }
        return list;
        //PLAN A
        //https://stackoverflow.com/questions/27361538/how-to-show-changes-between-commits-with-jgit
        //PLAN B
        //https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowChangedFilesBetweenCommits.java
    }


    //文件差异
    public static int getDiffCount(DiffEntry diffEntry, Git git) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(byteArrayOutputStream);
        df.setDiffComparator(RawTextComparator.WS_IGNORE_TRAILING);
        df.setRepository(git.getRepository());
        FileHeader fileHeader = null;
        try {
            fileHeader = df.toFileHeader(diffEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<HunkHeader> hunkHeaders = (List<HunkHeader>) fileHeader.getHunks();
        int addSize = 0;
        int subSize = 0;
        for (HunkHeader hunkHeader : hunkHeaders) {
            EditList editList = hunkHeader.toEditList();
            for (Edit edit : editList) {
                subSize += edit.getEndA() - edit.getBeginA();
                addSize += edit.getEndB() - edit.getBeginB();
            }
        }
        byteArrayOutputStream.reset();
        return addSize + subSize;
    }

    //收集某个commit的diffEntryList代码差异行数
    public static int getSumCommitChangeLines(RevCommit commit,Git git) {
        List<DiffEntry> diffEntryList = DiffCommitUtils.diffCommitEntrys(commit, commit.getParent(0), git);
        int t = 0;
        for (DiffEntry diffEntry : diffEntryList) {
            t += DiffCommitUtils.getDiffCount(diffEntry, git);
        }
        return t;
    }

}

