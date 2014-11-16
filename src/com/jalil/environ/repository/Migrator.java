package com.jalil.environ.repository;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.google.common.base.Charsets;
import com.google.common.io.Files;


public class Migrator {

	private final File migrationDirectory;
	private final Connection con;
	
	private String migrationPrefixFormat = "yyyy-MM-dd-hh-mm-ss";
	private DateFormat sdf = new SimpleDateFormat(migrationPrefixFormat);
	private static String DEFAULT_DELIMITER = ";";
	private static String CREATE_META_DATA = "CREATE TABLE IF NOT EXISTS migration_meta_data( " + 
	 										 " id INTEGER PRIMARY KEY NOT NULL, " + 
	 										 " migration_datetime DATETIME NOT NULL)";
	private static String INSERT_META_DATA = "INSERT INTO migration_meta_data(migration_datetime) VALUES(?)";
	private static String SELECT_META_DATA = "SELECT MAX(migration_datetime) FROM migration_meta_data";

	public Migrator(File migrationDirectory, Connection con) {
		this.migrationDirectory = migrationDirectory;
		this.con = con;
	}
	
	public void applyUpdates() throws Exception {
		initializeMigrationMetaData();
		final Map<Date, String> updates = findUpdates();
		if (updates.size() == 0) 
			return;
		new SafeBatch(con) {

			@Override
            public void run() throws SQLException {
				Statement stmt = con.createStatement();
				try {
					PreparedStatement preparedStmt = con.prepareStatement(INSERT_META_DATA);
					try {
						for (Map.Entry<Date, String> updateEntry : updates.entrySet()) {
							applyUpdate(stmt, preparedStmt, updateEntry.getKey(), updateEntry.getValue());				
						}				
						stmt.executeBatch();
						preparedStmt.executeBatch();
					} finally { preparedStmt.close(); }
				} finally { stmt.close(); }	            
            }}.commit();
	}
	
	private Map<Date, String> findUpdates() throws Exception {
		Map<Date, String> dateMigrationMap = new TreeMap<Date, String>();
		Date lastMigrationDate = null;
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(SELECT_META_DATA);
			rs.next();
			lastMigrationDate = rs.getDate(1);
			if (lastMigrationDate == null) {
				lastMigrationDate = new Date(sdf.parse("1970-01-01-00-00-00").getTime());				
			}
		} finally { stmt.close(); }
		File[] migrations = migrationDirectory.listFiles();
		for (File migration : migrations) {
			String prefix = migration.getName().substring(0, migrationPrefixFormat.length());
			Date migrationDate = new Date(sdf.parse(prefix).getTime());
			if (migrationDate.after(lastMigrationDate)) {
				String migrationContent = Files.toString(migration, Charsets.UTF_8);
				dateMigrationMap.put(migrationDate, migrationContent);				
			}
		}
		return dateMigrationMap;
	}
	
	private void applyUpdate(Statement stmt, PreparedStatement metaStmt, Date updateDate, String updateContent) throws SQLException {
		StringTokenizer tokenizer = new StringTokenizer(updateContent, DEFAULT_DELIMITER);
		while (tokenizer.hasMoreTokens()) {
			String singleUpdate = tokenizer.nextToken().trim();
			if (!singleUpdate.isEmpty()) {
				stmt.addBatch(singleUpdate);				
			}
		}
		metaStmt.setDate(1, updateDate);
		metaStmt.addBatch();
	}
	
	private void initializeMigrationMetaData() throws Exception {
		Statement createStmt = con.createStatement();
		try {
			createStmt.executeUpdate(CREATE_META_DATA);	
		} finally { createStmt.close(); }
	}
	
}
