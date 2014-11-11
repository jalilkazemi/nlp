package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import com.jalil.environ.rss.Channel;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;

public class RssFeedDao {
	
	private static String INSERT_CHANNEL = "INSERT OR IGNORE INTO channels(title, link, description, language) " +
										   "VALUES(?, ?, ?, ?)";
	private static String INSERT_ITEM = "INSERT OR IGNORE INTO items(channel_pk, title, link, description) " +
										"SELECT id, ?, ?, ? FROM channels WHERE link = ?";

	public void storeRssFeed(final Connection con, final RssFeed rssFeed) throws SQLException {
		new SafeBatch(con) {

			@Override
            public void run() throws SQLException {
				storeChannel(con, rssFeed.getChannel());
				storeItems(con, rssFeed.getChannel().getLink(), rssFeed.getChannel().getItems());
            }}.commit();
	}
	
	private void storeChannel(Connection con, Channel channel) throws SQLException {
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
	
	private void storeItems(Connection con, String channelLink, Iterator<Item> items) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_ITEM);
		while (items.hasNext()) {
			Item item = items.next();
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
		stmt.executeBatch();
	}
}
