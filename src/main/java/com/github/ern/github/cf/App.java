package com.github.ern.github.cf;


import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestCommitDetail;
import org.kohsuke.github.PagedIterable;

/**
 * Commit Finder
 * 
 * Finds all commits associated with all merged PR's
 * and logs them to a log.
 */
public class App {

	private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {
        logger.info( "Github lost commit finder start." );
        
        GithubRepo repo = new GithubRepo();
        
        List<GHPullRequest> closedPRs = repo.getClosedPullRequests();
        List<GHPullRequest> mergedPRs = new ArrayList<>();
        
        logger.info("Total Closed PRs: " + closedPRs.size());
        for (GHPullRequest pr : closedPRs) {
			try {
				if (pr.isMerged()) {
					mergedPRs.add(pr);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
        
        logger.info("Total Merged PRs: " + mergedPRs.size());
        
        for (GHPullRequest pr : mergedPRs) {
        	PagedIterable<GHPullRequestCommitDetail> details = pr.listCommits();
        	for (GHPullRequestCommitDetail detail : details) {
        		logger.info("PR:" + pr.getNumber() + " SHA:" + detail.getSha());
			}
		}
        
        logger.info( "Github lost commit finder end." );
    }
}
