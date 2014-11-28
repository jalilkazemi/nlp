package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;
import com.jalil.environ.rss.Item;

public class PostDao {
	
	private final static String INSERT_POST = "INSERT OR IGNORE INTO posts(item_pk, body, meta, fetched_datetime) " +
										"SELECT id, ?, ?, ? FROM items WHERE link = ?";
	private final static String SELECT_POST = "SELECT body, meta, fetched_datetime FROM posts JOIN items ON item_pk = items.id WHERE link = ?";
	
	private final Connection con;
	
	public PostDao(Connection con) {
		this.con = con;
	}
	
	public void storePost(Item item, Post post) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(INSERT_POST);
		try {
			stmt.setString(1, post.getBody());
			if (post.getMeta() == null) {
				stmt.setNull(2, Types.VARCHAR);
			} else {
				stmt.setString(2, post.getMeta());
			}
			stmt.setDate(3, new Date(post.getFetchedTime().getTime()));
			stmt.setString(4, item.getLink());
			int rowCount = stmt.executeUpdate();
			if (rowCount == 0) {
				System.out.println("PostDao: failed to persist the post in " + item.getLink());
			} else {
				System.out.println("PostDao: persisted a post");
			}
		} finally {
			stmt.close();
		}
	}

	public Set<Post> restorePost(Item item) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SELECT_POST);
		try {
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
		} finally {
			stmt.close();
		}
	}
}
