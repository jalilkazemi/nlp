package com.jalil.environ.repository.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jalil.environ.html.Post;
import com.jalil.environ.html.PostBuilder;

import static com.jalil.environ.langmodel.Word.newWord;

import com.jalil.environ.langmodel.Word;
import com.jalil.environ.langmodel.estimate.ReverseSequenceCounter;
import com.jalil.environ.repository.LanguageDao;
import com.jalil.environ.repository.Migrator;
import com.jalil.environ.repository.PostDao;
import com.jalil.environ.repository.RssFeedDao;
import com.jalil.environ.rss.Item;
import com.jalil.environ.rss.RssFeed;
import com.jalil.environ.rss.builder.ChannelBuilder;
import com.jalil.environ.rss.builder.ItemBuilder;

import static com.jalil.environ.Configuration.migrationDirectory;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class LanguageDaoTest {

	private static Connection con = null;
	private Migrator migrator = null;
	private static LanguageDao languageDao = null;
	private static RssFeedDao rssDao = null;
	private static PostDao postDao = null;

	private List<Character> delimList = Lists.newArrayList(' ', ',', '\"', '\'', '(', ')', '[', ']', '/', '\n');
	private List<Character> anotherDelimList = Lists.newArrayList('~');
	private List<Character> periodList = Lists.newArrayList('.', ';', '?', '!', ':');
	private List<Character> anotherPeriodList = Lists.newArrayList('`');
	
	private Item item;
	private Item anotherItem;
	private Post post;
	private Post anotherPost;
	private RssFeed rssFeed;
	
	@BeforeClass
	public static void start() {
		con = FakeSQLiteJDBC.getConnection();
		languageDao = new LanguageDao(con);
		rssDao = new RssFeedDao(con);
		postDao = new PostDao(con);
	}
	
	@Before
	public void init() throws Exception {
		migrator = new Migrator(migrationDirectory, con);		
		migrator.applyUpdates();
		
		item = new ItemBuilder().
				title("item").
				link("www.item.com").
				build();
		anotherItem = new ItemBuilder().
				title("item2").
				link("www.item2.com").
				build();

		post = new PostBuilder().
				item(item).
				body("post body").
				fetchedTime(new Date()).
				build();		
		anotherPost = new PostBuilder().
				item(anotherItem).
				body("post body").
				fetchedTime(new Date()).
				build();

		rssFeed = new RssFeed(
				new ChannelBuilder().
				title("channel").
				link("www.channel.com").
				items(Sets.newHashSet(item, anotherItem)).
				build());
	}
	
	@Test
	public void testWordDelimiterStorage() throws Exception {
		languageDao.setWordDelimiters(delimList);
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT symbol FROM delimiters WHERE is_period = 0");
			while (rs.next()) {
				String delim = rs.getString(1);
				assertEquals(1, delim.length());
				assertEquals(true, delimList.remove((Object) delim.charAt(0)));
			}
			assertEquals(0, delimList.size());
		} finally {
			stmt.close();
		}
	}

	@Test
	public void testWordDelimiterRestore() throws Exception {
		languageDao.setWordDelimiters(delimList);
		String delimiters = languageDao.getWordDelimiters();
		for (char delim : delimiters.toCharArray()) {
			assertEquals(true, delimList.remove((Object) delim));
		}
		assertEquals(0, delimList.size());
	}

	@Test
	public void testSentenceDelimiterStorage() throws Exception {
		languageDao.setSentenceDelimiters(periodList);
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT symbol FROM delimiters WHERE is_period = 1");
			while (rs.next()) {
				String period = rs.getString(1);
				assertEquals(1, period.length());
				assertEquals(true, periodList.remove((Object) period.charAt(0)));
			}
			assertEquals(0, periodList.size());
		} finally {
			stmt.close();
		}
	}

	@Test
	public void testSentenceDelimiterRestore() throws Exception {
		languageDao.setSentenceDelimiters(periodList);
		String periods = languageDao.getSentenceDelimiters();
		for (char period : periods.toCharArray()) {
			assertEquals(true, periodList.remove((Object) period));
		}
		assertEquals(0, periodList.size());
	}
	
	@Test
	public void testDelimiterDoubleStorage() throws SQLException {
		languageDao.setWordDelimiters(delimList);
		languageDao.setWordDelimiters(anotherDelimList);
		String delimiters = languageDao.getWordDelimiters();
		assertEquals(anotherDelimList.size(), delimiters.length());

		languageDao.setSentenceDelimiters(periodList);
		languageDao.setSentenceDelimiters(anotherPeriodList);
		String periods = languageDao.getSentenceDelimiters();
		assertEquals(anotherPeriodList.size(), periods.length());
	}

	@Test
	public void testIncrementWordSequence() throws SQLException {
		populateRssFeedTables();
		
		ReverseSequenceCounter<Word> counter = new ReverseSequenceCounter<Word>();
		Word a = newWord("a");
		Word b = newWord("b");
		Word c = newWord("c");
		counter.increment(a, b);
		counter.increment(a, c);
		counter.increment(a);
		
		languageDao.incrementWordSequence(post, 0, counter);
		
		counter = new ReverseSequenceCounter<Word>();
		counter.increment(b, a, c);

		languageDao.incrementWordSequence(anotherPost, 0, counter);

		Set<String> words = Sets.newHashSet(a.toString(), b.toString(), c.toString());
		Map<Integer, String> id2Word = Maps.newHashMap();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, word FROM words");
		while (rs.next()) {
			assertEquals(true, words.remove(rs.getString("word")));
			id2Word.put(rs.getInt("id"), rs.getString("word"));
		}
		assertEquals(0, words.size());
		
		Map<String, Integer> uniCount = Maps.newHashMap();
		uniCount.put(a.toString(), 1);
		uniCount.put(b.toString(), 1);
		uniCount.put(c.toString(), 2);
		rs = stmt.executeQuery("SELECT word_pk, count FROM unigrams");
		while (rs.next()) {
			String uni = id2Word.get(rs.getInt("word_pk"));
			assertEquals((int) uniCount.get(uni), rs.getInt("count"));
			uniCount.remove(uni);
		}
				
		Map<List<String>, Integer> biCount = Maps.newHashMap();
		biCount.put(Lists.newArrayList("a", "b"), 1);
		biCount.put(Lists.newArrayList("a", "c"), 2);
		rs = stmt.executeQuery("SELECT word1_pk, word2_pk, count FROM bigrams");
		while (rs.next()) {
			String v = id2Word.get(rs.getInt("word1_pk"));
			String w = id2Word.get(rs.getInt("word2_pk"));
			List<String> bi = Lists.newArrayList(v, w);
			assertEquals((int) biCount.get(bi), rs.getInt("count"));
			biCount.remove(bi);
		}

		Map<List<String>, Integer> triCount = Maps.newHashMap();
		triCount.put(Lists.newArrayList("b", "a", "c"), 1);
		rs = stmt.executeQuery("SELECT word1_pk, word2_pk, word3_pk, count FROM trigrams");
		while (rs.next()) {
			String u = id2Word.get(rs.getInt("word1_pk"));
			String v = id2Word.get(rs.getInt("word2_pk"));
			String w = id2Word.get(rs.getInt("word3_pk"));
			List<String> tri = Lists.newArrayList(u, v, w);
			assertEquals((int) triCount.get(tri), rs.getInt("count"));
			triCount.remove(tri);
		}
	}
	
	private void populateRssFeedTables() throws SQLException {
		rssDao.storeRssFeed(rssFeed);
		postDao.storePost(post);
		postDao.storePost(anotherPost);		
	}
	
	@After
	public void cleanUp() throws SQLException {
		FakeSQLiteJDBC.cleanUp();
	}
	
	@AfterClass
	public static void terminate() throws SQLException {
		con.close();
	}
}
