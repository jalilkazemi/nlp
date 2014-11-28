package com.jalil.environ.worker;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.fetch.RssFetcher;
import com.jalil.environ.helper.Pair;
import com.jalil.environ.html.Post;
import com.jalil.environ.repository.PostDao;
import com.jalil.environ.repository.RssFeedDao;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;
import com.jalil.environ.server.Bus;

public class NewsCollector {
	
	private final Bus databaseBus;
	private final Bus networkBus;
	private final RssFetcher rssFetcher;
	private final PostFetcher postFetcher;
	private final RssFeedDao rssDao;
	private final PostDao postDao;
	
	public NewsCollector(Bus databaseBus, Bus networkBus, 
						RssFetcher rssFetcher, PostFetcher postFetcher,
						RssFeedDao rssDao, PostDao postDao) {
		this.databaseBus = databaseBus;
		this.networkBus = networkBus;
		this.rssFetcher = rssFetcher;
		this.postFetcher = postFetcher;
		this.rssDao = rssDao;
		this.postDao = postDao;
	}
	
	public void collect() {
		Set<String> rssPages = null;
		try {
	        rssPages = rssDao.restoreRssPages();
        } catch (Exception e) {
	        System.err.println("Failed to restore rss pages: ");
	        e.printStackTrace();
	        return;
        }
        List<Future<RssFeed>> rssFutures = submitRssDownloads(rssPages);
	}
	
	private List<Future<RssFeed>> submitRssDownloads(Set<String> rssPages) {
        List<Future<RssFeed>> rssFutures = new LinkedList<Future<RssFeed>>();
        for (final String rssPage : rssPages) {
        	rssFutures.add(networkBus.submit(new Callable<RssFeed>() {

				@Override
                public RssFeed call() throws Exception {
                    return rssFetcher.fetch(rssPage);
				}}));
        }
        return rssFutures;
	}
}
