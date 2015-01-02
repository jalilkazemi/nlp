package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.jalil.environ.html.Post;
import com.jalil.environ.langmodel.Word;

import static com.jalil.environ.langmodel.Word.START;

import com.jalil.environ.langmodel.estimate.ReverseSequenceCounter;

public class LanguageDao {

	private final static String SELECT_WORD_DELIMITER = "SELECT symbol FROM delimiters WHERE is_period = 0";
	private final static String INSERT_WORD_DELIMITER = "INSERT INTO delimiters(symbol, is_period) VALUES(?, 0)";
	private final static String CLEAN_WORD_DELIMITERS = "DELETE FROM delimiters WHERE is_period = 0";

	private final static String SELECT_SENTENCE_DELIMITER = "SELECT symbol FROM delimiters WHERE is_period = 1";
	private final static String INSERT_SENTENCE_DELIMITER = "INSERT INTO delimiters(symbol, is_period) VALUES(?, 1)";
	private final static String CLEAN_SENTENCE_DELIMITERS = "DELETE FROM delimiters WHERE is_period = 1";

	private final Connection con;

	public LanguageDao(Connection con) {
		this.con = con;
	}
	
	public String getWordDelimiters() throws SQLException { 
		return getDelimiters(SELECT_WORD_DELIMITER); 
	}
	
	public String getSentenceDelimiters() throws SQLException { 
		return getDelimiters(SELECT_SENTENCE_DELIMITER); 
	}
	

	private String getDelimiters(String query) throws SQLException {
		Statement stmt = con.createStatement();
		try {
			StringBuffer delimiters = new StringBuffer();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				delimiters.append(rs.getString(1).charAt(0));
			}
			return delimiters.toString();
		} finally {
			stmt.close();
		}
	}
	
	public void setWordDelimiters(final List<Character> delimiters) throws SQLException {
		setDelimiters(delimiters, CLEAN_WORD_DELIMITERS, INSERT_WORD_DELIMITER);
	}
	
	public void setSentenceDelimiters(final List<Character> delimiters) throws SQLException {
		setDelimiters(delimiters, CLEAN_SENTENCE_DELIMITERS, INSERT_SENTENCE_DELIMITER);
	}
	
	private void setDelimiters(final List<Character> delimiters, 
			final String cleanQuery, final String insertQuery) throws SQLException {
		new SafeBatch(con) {
			
			@Override
			public void run() throws SQLException {
				Statement cleanStmt = con.createStatement();
				try {
					cleanStmt.executeUpdate(cleanQuery);
				} finally {
					cleanStmt.close();
				}
				
				PreparedStatement insertStmt = con.prepareStatement(insertQuery);
				try {
					for (char delimiter : delimiters) {
						insertStmt.setString(1, String.valueOf(delimiter));
						insertStmt.executeUpdate();
					}
				} finally {
					insertStmt.close();
				}
			}
		}.commit();
	}
}
