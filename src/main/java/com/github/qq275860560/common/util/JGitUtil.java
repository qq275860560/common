package com.github.qq275860560.common.util;

import java.io.File;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.HttpConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.github.qq275860560.common.filter.ExceptionFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class JGitUtil {
	 
	private JGitUtil() {
	}

	public static Git getGit(String uri, CredentialsProvider credentialsProvider, String localDir) throws Exception {
		Git git = null;
		if (new File(localDir).exists()) {
			git = Git.open(new File(localDir));
		} else {
			git = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setURI(uri)
					.setDirectory(new File(localDir)).call();
		}
		git.getRepository().getConfig().setInt(HttpConfig.HTTP, null, HttpConfig.POST_BUFFER_KEY, 512 * 1024 * 1024);
		return git;
	}

	public static CredentialsProvider getCredentialsProvider(String username, String password) {
		return new UsernamePasswordCredentialsProvider(username, password);
	}

	public static Repository getRepository(Git git) {
		return git.getRepository();
	}

	public static void pull(Git git, CredentialsProvider credentialsProvider) throws Exception {
		git.pull().setRemote("origin").setCredentialsProvider(credentialsProvider).call();

	}

	public static void push(Git git, CredentialsProvider credentialsProvider, String filepattern, String message)
			throws Exception {

		git.add().addFilepattern(filepattern).call();
		git.add().setUpdate(true);
		git.commit().setMessage(message).call();
		git.push().setCredentialsProvider(credentialsProvider).call();

	}

	public static void pushAll(CredentialsProvider credentialsProvider, Git git) throws GitAPIException, Exception {
		List<DiffEntry> diffEntries = git.diff().call();
		for (DiffEntry diffEntry : diffEntries) {
			switch (diffEntry.getChangeType()) {
			case ADD:
			case COPY:
			case RENAME:
			case MODIFY:
				log.info("提交新增修改文件=" + diffEntry.getNewPath());
				push(git, credentialsProvider, diffEntry.getNewPath(), "提交文件");
				break;
			case DELETE:
				log.info("提交删除文件=" + diffEntry.getOldPath());
				push(git, credentialsProvider, diffEntry.getOldPath(), "删除文件");
				break;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * String uri = "http://132.122.237.227:3000/jclijs/git_test.git"; String
		 * username = "jclijs"; String password = "123456"; String localDir =
		 * "D:/tmp/git_test";
		 */
		String uri = "http://132.122.237.68:83/root/test2.git";
		String username = "root";
		String password = "12345678";
		String localDir = "D:/tmp/test2";

		CredentialsProvider credentialsProvider = getCredentialsProvider(username, password);
		Git git = getGit(uri, credentialsProvider, localDir);
		pull(git, credentialsProvider);
		// push(git, credentialsProvider, ".", "提交文件");
		pushAll(credentialsProvider, git);

	}

}
