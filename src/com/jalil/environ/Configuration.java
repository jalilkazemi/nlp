package com.jalil.environ;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


public class Configuration {
	private Configuration() {}
	
	public static final String databaseName = "resources/main.db";
	public static final String testDatabaseName = "resources/test.db";
	public static final File migrationDirectory = new File("migrations");
	public static final File testMigrationDirectory = new File("test_migrations");
	
	static {
		if (!migrationDirectory.exists()) {
			new FileNotFoundException("Migration directory is not found.").printStackTrace();
			System.exit(1);
		}
	}
}
