package com.jalil.environ.repository;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import com.jalil.environ.rss.Channel;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;

public class RssFeedDao {
	
	public void storeRssFeed(final Connection con, final RssFeed rssFeed) throws SQLException {
		new SafeBatch(con) {

			@Override
            public void run() throws SQLException {
				storeChannel(con, rssFeed.getChannel());
				storeItems(con, rssFeed.getChannel().getLink(), rssFeed.getItems());
				con.commit();	            
            }}.execute();
	}
	
	private void storeChannel(Connection con, Channel channel) throws SQLException {
	}
	
	private void storeItems(Connection con, URL channelLink, Iterator<Item> items) throws SQLException {
	}
}
