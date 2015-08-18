package com.github.ern.github.cf;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttpConnector;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

public class GithubRepo {
	private static final Logger logger = LogManager.getLogger(GithubRepo.class);
	private static GHRepository repository;

	public GithubRepo() {
		init();
	}
	
	private static void init() {
		File cacheDirectory = new File("/tmp/github.cache");
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
		Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024); // 10MB cache
		
		try {
			GitHub github = GitHubBuilder.fromCredentials()
					.withConnector(
							new OkHttpConnector(
									new OkUrlFactory(
											new OkHttpClient().setCache(cache)))).build();
			repository = github.getOrganization("sakaiproject").getRepository("sakai");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
	}
	
	public List<GHPullRequest> getClosedPullRequests() {
        try {
			return getRepository().getPullRequests(GHIssueState.CLOSED);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
        return Collections.emptyList();
	}

	public static GHRepository getRepository() {
		if (repository == null) {
			logger.error("Github Rpository is not initialized.");
			System.exit(1);
		}
		return repository;
	}
}
