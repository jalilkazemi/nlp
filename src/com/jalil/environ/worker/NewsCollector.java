package com.jalil.environ.worker;

import java.util.Set;
import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.fetch.RssFetcher;
import com.jalil.environ.repository.PostDao;
import com.jalil.environ.repository.RssFeedDao;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;

public class NewsCollector {
	
	private final RssFetcher rssFetcher;
	private final PostFetcher postFetcher;
	private final RssFeedDao rssDao;
	private final PostDao postDao;
	
	public NewsCollector(RssFetcher rssFetcher, PostFetcher postFetcher,
						RssFeedDao rssDao, PostDao postDao) {
		this.rssFetcher = rssFetcher;
		this.postFetcher = postFetcher;
		this.rssDao = rssDao;
		this.postDao = postDao;
	}
	
	public void collect() {
		try {
	        Set<String> rssPages = rssDao.restoreRssPages();
	        for (String rssPage : rssPages) {
	        	RssFeed rss = rssFetcher.fetch(rssPage);
	        	rssDao.storeRssFeed(rss);
	        	for (Item item : rss.getChannel().getItems()) {
	        		if (postDao.restorePost(item).isEmpty()) {
	        			postDao.storePost(item, postFetcher.fetch(item.getLink()));
	        		}
	        	}
	        }
        } catch (Exception e) {
	        System.err.println("Failed to collect news: ");
	        e.printStackTrace();
        }
	}
}
