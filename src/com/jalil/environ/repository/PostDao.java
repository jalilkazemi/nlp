package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.builder.ItemBuilder;

public class PostDao {
	
	private final static String INSERT_POST = "INSERT OR IGNORE INTO posts(item_pk, body, meta, fetched_datetime) " +
										"SELECT id, ?, ?, ? FROM items WHERE link = ?";
	private final static String SELECT_POST = "SELECT body, meta, fetched_datetime FROM posts JOIN items ON item_pk = items.id WHERE link = ?";
	
	private final static String SELECT_UNPARSED_POSTS = "SELECT title, link, description, body, meta, fetched_datetime "
			+ " FROM items JOIN posts ON items.id = item_pk "
			+ " LEFT JOIN parsed_posts ON posts.id = parsed_post_pk "
			+ " WHERE parsed_post_pk IS NULL";

	private final Connection con;
	
	public PostDao(Connection con) {
		this.con = con;
	}
	
	public void storePost(Post post) throws SQLException {
		Item item = post.getItem();
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
				posts.add(new PostBuilder().item(item).
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
	
	public List<Post> restoreUnparsedPosts() throws SQLException {
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(SELECT_UNPARSED_POSTS);
			List<Post> posts = new LinkedList<Post>();
			while (rs.next()) {
				Item item = new ItemBuilder().
						title(rs.getString("title")).
						link(rs.getString("link")).
						description(rs.getString("description")).
						build();
				posts.add(new PostBuilder().item(item).
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
