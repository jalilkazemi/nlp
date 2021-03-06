package com.jalil.environ.worker;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.fetch.RssFetcher;
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
        List<Future<Set<Item>>> itemsFutures = submitRssStorages(rssFutures);
        List<Future<Post>> itemPosts = submitPostDownloads(itemsFutures);
        List<Future<Boolean>> postFutures = submitPostStorages(itemPosts);
        for (Future<Boolean> postFuture : postFutures) {
        	try {
        		postFuture.get();
        	} catch (Exception e) {
    	        System.err.println("Failed to store post: ");
    	        e.printStackTrace();
        	}
        }
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
	
	private List<Future<Set<Item>>> submitRssStorages(List<Future<RssFeed>> rssFutures) {
        List<Future<Set<Item>>> itemsFutures = new LinkedList<Future<Set<Item>>>(); 
        for (Future<RssFeed> rssFuture : rssFutures) {
        	try {
	        	final RssFeed rss = rssFuture.get();
	        	itemsFutures.add(databaseBus.submit(new Callable<Set<Item>>() {
	
					@Override
	                public Set<Item> call() throws Exception {
			        	rssDao.storeRssFeed(rss);
			        	Set<Item> itemsForDownload = new HashSet<Item>();
			        	for (Item item : rss.getChannel().getItems()) {
			        		if (postDao.restorePost(item).isEmpty()) {
			        			itemsForDownload.add(item);
			        		}
			        	}
	                    return itemsForDownload;
					}}));
        	} catch (Exception e) {
    	        System.err.println("Failed to fetch rss page: ");
    	        e.printStackTrace();      		
        	}
        }
        return itemsFutures;
	}
	
	private List<Future<Post>> submitPostDownloads(List<Future<Set<Item>>> itemsFutures) {
        List<Future<Post>> itemPosts = new LinkedList<Future<Post>>();
        for (Future<Set<Item>> itemsFuture : itemsFutures) {
        	try {
	        	Set<Item> items = itemsFuture.get();
	        	for (final Item item : items) {
		        	itemPosts.add(networkBus.submit(new Callable<Post>() {
	
						@Override
	                    public Post call() throws Exception {
							return postFetcher.fetch(item);
						}}));
	        	}
        	} catch (Exception e) {
       	        System.err.println("Failed to store rss items: ");
    	        e.printStackTrace();      		
        	}
        }
        return itemPosts;
	}

	private List<Future<Boolean>> submitPostStorages(List<Future<Post>> postFutures) {
		List<Future<Boolean>> nullFutures = new LinkedList<Future<Boolean>>();
		for (Future<Post> postFuture : postFutures) {
			try {
	        	final Post post = postFuture.get();
	        	nullFutures.add(databaseBus.submit(new Callable<Boolean>() {
	
					@Override
	                public Boolean call() throws Exception {
						postDao.storePost(post);
						return true;
	                }}));
			} catch (Exception e) {
       	        System.err.println("Failed to fetch posts: ");
    	        e.printStackTrace();      		
        	}
        }    
		return nullFutures;
    }
}
