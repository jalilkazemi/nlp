package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;
import com.jalil.environ.rss.Item;

public class PostDao {
	
	private final static String INSERT_POST = "INSERT OR IGNORE INTO posts(item_pk, body, meta, fetched_datetime) " +
										"SELECT id, ?, ?, ? FROM items WHERE link = ?";
	private final static String SELECT_POST = "SELECT body, meta, fetched_datetime FROM posts JOIN items ON item_pk = items.id WHERE link = ?";
	
	public void storePost(Connection con, Item item, Post post) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_POST);
		stmt.setString(1, post.getBody());
		stmt.setString(2, post.getMeta());
		stmt.setDate(3, post.getFetchedTime());
		stmt.setString(4, item.getLink());
		stmt.executeUpdate();
	}

	public Set<Post> restorePost(Connection con, Item item) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SELECT_POST);
		stmt.setString(1, item.getLink());
		ResultSet rs = stmt.executeQuery();
		Set<Post> posts = new HashSet<Post>();
		while (rs.next()) {
			posts.add(new PostBuilder().
					body(rs.getString("body")).
					meta(rs.getString("meta")).
					fetchedTime(rs.getDate("fetched_datetime")).
					build());
		}
		return posts;
	}
}
