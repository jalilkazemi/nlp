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

	private final static String UPDATE_SENTENCE_COUNT = "UPDATE sentence_count SET count = count + ?";
	
	private final static String INSERT_WORD = "INSERT OR IGNORE INTO words(word) VALUES(?)";

	private final static String INSERT_UNIGRAM = "INSERT OR IGNORE INTO unigrams(word_pk, count) "
			+ " SELECT id, 0 FROM words WHERE word = ?";
	private final static String UPDATE_UNIGRAM = "UPDATE unigrams SET count = count + ? "
			+ " WHERE word_pk IN (SELECT id FROM words WHERE word = ?)";

	private final static String INSERT_BIGRAM = "INSERT OR IGNORE INTO bigrams(word1_pk, word2_pk, count) " 
			+ " SELECT w1.id, w2.id, 0 FROM words w1, words w2 WHERE w1.word = ? AND w2.word = ?";
	private final static String UPDATE_BIGRAM = "UPDATE bigrams SET count = count + ? "
			+ " WHERE word1_pk IN (SELECT id FROM words WHERE word = ?) "
			+ " AND   word2_pk IN (SELECT id FROM words WHERE word = ?)";

	private final static String INSERT_TRIGRAM = "INSERT OR IGNORE INTO trigrams(word1_pk, word2_pk, word3_pk, count) "
			+ " SELECT w1.id, w2.id, w3.id, 0 FROM words w1, words w2, words w3 WHERE w1.word = ? AND w2.word = ? AND w3.word = ?";
	private final static String UPDATE_TRIGRAM = "UPDATE trigrams SET count = count + ? "
			+ " WHERE word1_pk IN (SELECT id FROM words WHERE word = ?) "
			+ " AND   word2_pk IN (SELECT id FROM words WHERE word = ?) "
			+ " AND   word3_pk IN (SELECT id FROM words WHERE word = ?) ";

	private final static String INSERT_PARSED_POST = "INSERT INTO parsed_posts(parsed_post_pk) "
			+ " SELECT posts.id FROM posts JOIN items ON items.id = item_pk WHERE link = ?";

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

	public void incrementWordSequence(final Post post, final int numSentences,
            final ReverseSequenceCounter<Word> sequenceCounter) throws SQLException {
		new SafeBatch(con) {

			@Override
            public void run() throws SQLException {
				PreparedStatement sentenceStmt = con.prepareStatement(UPDATE_SENTENCE_COUNT);
				try {
					sentenceStmt.setInt(1, numSentences);
					sentenceStmt.executeUpdate();
				} finally {
					sentenceStmt.close();
				}
				
				PreparedStatement wordStmt = con.prepareStatement(INSERT_WORD);
				try {
					wordStmt.setString(1, START.toString());
					wordStmt.executeUpdate();
					
					for (Word word : sequenceCounter.lastItemSet()) {
						wordStmt.setString(1, word.toString());
						wordStmt.executeUpdate();
					}
				} finally {
					wordStmt.close();
				}

				int numSuccessfulStorage = 0;
				int numFailedStorage = 0;

				PreparedStatement uniInsert = null;
				PreparedStatement uniUpdate = null;
				PreparedStatement biInsert = null;
				PreparedStatement biUpdate = null;
				PreparedStatement triInsert = null;
				PreparedStatement triUpdate = null;
				try {
					uniInsert = con.prepareStatement(INSERT_UNIGRAM);
					uniUpdate = con.prepareStatement(UPDATE_UNIGRAM);
					biInsert = con.prepareStatement(INSERT_BIGRAM);
					biUpdate = con.prepareStatement(UPDATE_BIGRAM);
					triInsert = con.prepareStatement(INSERT_TRIGRAM);
					triUpdate = con.prepareStatement(UPDATE_TRIGRAM);
					
					Iterator<Entry<List<Word>, Integer>> sequenceIter = sequenceCounter.sequenceCountIterator();
					while (sequenceIter.hasNext()) {
						Entry<List<Word>, Integer> entry = sequenceIter.next();
						boolean result;
						switch (entry.getKey().size()) {
						case 1:
							result = increment(uniInsert, uniUpdate, entry.getValue(), entry.getKey());
							break;
						case 2:
							result = increment(biInsert, biUpdate, entry.getValue(), entry.getKey());
							break;
						case 3:
							result = increment(triInsert, triUpdate, entry.getValue(), entry.getKey());
							break;
						default:
							throw new RuntimeException("The sequence size exceeds 3.");
						}
						if (result) {
							numSuccessfulStorage++;
						} else {
							numFailedStorage++;
						}
					}
				} finally {
					closeAllSafely(uniInsert, uniUpdate, biInsert, biUpdate, triInsert, triUpdate);
				}
				
				PreparedStatement postStmt = con.prepareStatement(INSERT_PARSED_POST);
				try {
					postStmt.setString(1, post.getItem().getLink());
					postStmt.executeUpdate();
				} finally {
					postStmt.close();
				}
				
				System.out.println("INFO: Stored "+ numSuccessfulStorage + " word sequences from " + post.getItem().getLink());
				if (numFailedStorage > 0) {
					System.out.println("WARN: Failed "+ numFailedStorage + " word sequences from " + post.getItem().getLink());
				}
			}
		}.commit();	    
    }
	
	private boolean increment(PreparedStatement insert,
            PreparedStatement update, int count, List<Word> sequence) throws SQLException {
		int size = sequence.size();
		for (int i = 0; i < size; i++) {
			insert.setString(i + 1, sequence.get(i).toString());			
		}
		insert.executeUpdate();
		
		update.setInt(1, count);
		for (int i = 0; i < size; i++) {
			update.setString(i + 2, sequence.get(i).toString());			
		}
		return update.executeUpdate() == 1;
    }

	private void closeAllSafely(Statement... stmts) throws SQLException {
		SQLException e = null;
		for (Statement stmt : stmts) {
			try {
				if (stmt != null) stmt.close();				
			} catch (SQLException f) {
				e = f;
			}
		}
		if (e != null)
			throw e;
    }
}
