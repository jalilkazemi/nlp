package com.jalil.environ.repository.test;

import static com.jalil.environ.Configuration.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.List;

public class FakeSQLiteJDBC {
	
	private static Connection con = null;
	
	static {
	    try {
	        Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection("jdbc:sqlite:" + testDatabaseName);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
	
	public static Connection getConnection() {
		return con;
	}
	
	public static void cleanUp() throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table'");
        List<String> tables = new LinkedList<String>();
        while (rs.next()) {
        	tables.add(rs.getString("name"));
        }
        for (String table : tables) {
        	stmt.executeUpdate("DROP TABLE " + table);	        	
        }
	}
}
