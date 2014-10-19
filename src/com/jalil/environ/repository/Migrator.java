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
	
	private String migrationPrefixFormat = "yyyy-MM-dd hh-mm-ss";
	private static String CREATE_META_DATA = "CREATE TABLE IF NOT EXISTS migration_meta_data( " + 
	 										 " id INTEGER PRIMARY KEY NOT NULL, " + 
	 										 " migration_datetime DATETIME NOT NULL)";

	public Migrator(File migrationDirectory, Connection con) {
		this.migrationDirectory = migrationDirectory;
		this.con = con;
	}
	
	public void applyUpdates() throws Exception {
		initializeMigrationMetaData();
	}
	
	private void initializeMigrationMetaData() throws Exception {
		Statement createStmt = con.createStatement();
		try {
			createStmt.executeUpdate(CREATE_META_DATA);	
		} finally { createStmt.close(); }
	}
	
}
