package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.jalil.environ.rss.Channel;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;

public class RssFeedDao {
	
	private final static String INSERT_RSS = "INSERT OR IGNORE INTO rss_pages(link) values(?)";
	private final static String SELECT_RSS = "SELECT link FROM rss_pages";
	private final static String INSERT_CHANNEL = "INSERT OR IGNORE INTO channels(title, link, description, language) " +
										   "VALUES(?, ?, ?, ?)";
	private static String INSERT_ITEM = "INSERT OR IGNORE INTO items(channel_pk, title, link, description) " +
										"SELECT id, ?, ?, ? FROM channels WHERE link = ?";

	private final Connection con;
	
	public RssFeedDao(Connection con) {
		this.con = con;
	}
	
	public Set<String> restoreRssPages() throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SELECT_RSS);
		Set<String> rssPages = new HashSet<String>();
		while (rs.next()) {
			rssPages.add(rs.getString("link"));
		}
		return rssPages;
	}
	
	public void addRssPage(String rssLink) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_RSS);
		stmt.setString(1, rssLink);
		stmt.executeUpdate();
	}
	
	public void storeRssFeed(final RssFeed rssFeed) throws SQLException {
		new SafeBatch(con) {

			@Override
            public void run() throws SQLException {
				storeChannel(rssFeed.getChannel());
				storeItems(rssFeed.getChannel().getLink(), rssFeed.getChannel().getItems());
            }}.commit();
	}
	
	private void storeChannel(Channel channel) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_CHANNEL);
		stmt.setString(1, channel.getTitle());
		stmt.setString(2, channel.getLink());
		String description = channel.getDescription();
		if (description == null) {
			stmt.setNull(3, Types.VARCHAR);
		} else {
			stmt.setString(3, description);			
		}
		String language = channel.getLanguage();
		if (language == null) {
			stmt.setNull(4, Types.CHAR);
		} else {
			stmt.setString(4, channel.getLanguage());
		}
		
		stmt.executeUpdate();
	}
	
	private void storeItems(String channelLink, Set<Item> items) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_ITEM);
		for (Item item : items) {
			stmt.setString(1, item.getTitle());
			stmt.setString(2, item.getLink());
			String description = item.getDescription();
			if (description == null) {
				stmt.setNull(3, Types.VARCHAR);
			} else {
				stmt.setString(3, description);			
			}
			stmt.setString(4, channelLink);
			stmt.addBatch();
		}
		int[] rowCounts = stmt.executeBatch();
		int totalRowCount = sum(rowCounts);
		System.out.println("RssFeedDao: persited " + totalRowCount + " items");
	}
	
	private int sum(int[] values) {
		int sum = 0;
		for (int v : values) {
			sum += v;
		}
		return sum;
	}
}
