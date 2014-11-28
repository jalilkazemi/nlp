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
		try {
			Connection con = SQLiteJDBC.getConnection();
			Migrator migrator = new Migrator(migrationDirectory, con);
			
			RssFeedDao rssDao = new RssFeedDao(con);
			PostDao postDao = new PostDao(con);
			RssFetcher rssFetcher = new RssFetcher();
			PostFetcher postFetcher = new PostFetcher();
			Bus databaseBus = Bus.getDatabaseBus();
			Bus networkBus = Bus.getNetworkBus();
			
			NewsCollector newsCollector = new NewsCollector(databaseBus, networkBus,
					rssFetcher, postFetcher, rssDao, postDao);
			
			try {
				migrator.applyUpdates();
				newsCollector.collect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			SQLiteJDBC.closeConnection();
			Bus.closeAllBuses();
		}
	}
}
