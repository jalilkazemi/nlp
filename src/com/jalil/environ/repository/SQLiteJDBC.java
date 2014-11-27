package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.jalil.environ.Configuration.*;

public class SQLiteJDBC {
	private static Connection con = null;
	
	static {
	    try {
	        Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
	    } catch ( Exception e ) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
	
	public static Connection getConnection() {
		return con;
	}

	public static void closeConnection() {
		try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}
