package com.jalil.environ;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


public class Configuration {
	private Configuration() {}
	
	public static final String databaseName = "src/resources/main.db";
	public static final String testDatabaseName = "src/resources/test.db";
	public static final File migrationDirectory = new File("src/resources/migration");
	
	static {
		if (!migrationDirectory.exists()) {
			new FileNotFoundException("Migration directory is not found.").printStackTrace();
			System.exit(1);
		}
	}
}
