package com.jalil.environ.server;

import java.sql.Connection;

import javax.xml.bind.JAXBException;

import static com.jalil.environ.Configuration.*;

import com.jalil.environ.fetch.PostFetcher;
import com.jalil.environ.fetch.RssFetcher;
import com.jalil.environ.repository.Migrator;
import com.jalil.environ.repository.PostDao;
import com.jalil.environ.repository.RssFeedDao;
import com.jalil.environ.repository.SQLiteJDBC;
import com.jalil.environ.worker.NewsCollector;

public class Run {
	
	public static void main(String[] args) throws JAXBException {
		Connection con = SQLiteJDBC.getConnection();
		try {
			Migrator migrator = new Migrator(migrationDirectory, con);
			
			RssFeedDao rssDao = new RssFeedDao(con);
			PostDao postDao = new PostDao(con);
			RssFetcher rssFetcher = new RssFetcher();
			PostFetcher postFetcher = new PostFetcher();
			
			NewsCollector newsCollector = new NewsCollector(rssFetcher, postFetcher, rssDao, postDao);
			
			try {
				migrator.applyUpdates();
				newsCollector.collect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			SQLiteJDBC.closeConnection(); 
		}
	}
}
