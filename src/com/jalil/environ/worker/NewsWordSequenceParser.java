package com.jalil.environ.worker;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import com.jalil.environ.html.Post;
import com.jalil.environ.langmodel.TrigramParser;
import com.jalil.environ.langmodel.Word;
import com.jalil.environ.langmodel.estimate.ReverseSequenceCounter;
import com.jalil.environ.repository.LanguageDao;
import com.jalil.environ.repository.PostDao;

public class NewsWordSequenceParser {

	private PostDao postDao;
	private LanguageDao languageDao;
	
	public NewsWordSequenceParser(PostDao postDao, LanguageDao languageDao) {
		this.postDao = postDao;
		this.languageDao = languageDao;
	}
	
	public void parse() {
		List<Post> posts = null;
		try {
			posts = postDao.restoreUnparsedPosts();
		} catch (SQLException e) {
	        System.err.println("Failed to restore posts: ");
	        e.printStackTrace();
			return;
		}
		
		String periods = null;
		try {
			periods = languageDao.getSentenceDelimiters();
		} catch (SQLException e) {
	        System.err.println("Failed to restore sentence delimiters: ");
	        e.printStackTrace();
			return;			
		}
		
		String delimiters = null;
		try {
			delimiters = languageDao.getWordDelimiters();
		} catch (SQLException e) {
	        System.err.println("Failed to restore word delimiters: ");
	        e.printStackTrace();
			return;			
		}

		int postCount = 0;
		for (Post post : posts) {
			final ReverseSequenceCounter<Word> sequenceCounter = new ReverseSequenceCounter<Word>();
			int numSentences = 0;
			StringTokenizer sentenceTokenizer = new StringTokenizer(post.getBody(), periods);
			while (sentenceTokenizer.hasMoreTokens()) {
				numSentences++;
				new TrigramParser(sentenceTokenizer.nextToken(), delimiters) {

					@Override
                    public Object iteration(Word u, Word v, Word w) {
						sequenceCounter.increment(u, v, w);
						return null;
                    }}.parse();
			}
			
			try {
				languageDao.incrementWordSequence(post, numSentences, sequenceCounter);
				postCount++;
	        } catch (SQLException e) {
		        System.err.println("Failed to store parsed sentences of post: " + post.getItem().getLink());
	            e.printStackTrace();
	        }			
		}
		
		System.out.println("NewsSentenceParser: parsed " + postCount + " posts");
	}
}
