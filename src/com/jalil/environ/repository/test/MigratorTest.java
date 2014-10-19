package com.jalil.environ.repository.test;

import java.io.File;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.jalil.environ.repository.Migrator;
import static com.jalil.environ.Configuration.*;

@RunWith(JUnit4.class)
public class MigratorTest {

	private static Connection con = null;
	private Migrator migrator = null;
	
	private String update1 = "CREATE TABLE temp(name TEXT)";
	private String update2 = "CREATE TABLE tmp(id INTEGER); DROP TABLE temp;";
	
	private String update1Title = "2014-10-17 22-46-00_create_temp.sql";
	private String update2Title = "2014-10-17 22-47-00_create_tmp_drop_temp.sql";
	
	@BeforeClass
	public static void start() {
		con = FakeSQLiteJDBC.getConnection();
	}
	
	@Before
	public void init() {
		testMigrationDirectory.mkdirs();
		migrator = new Migrator(testMigrationDirectory, con);		
	}
	
	@Test
	public void testInitialization() throws Exception {		
		migrator.applyUpdates();
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND name = 'migration_meta_data'");
			assertTrue(rs.next());
		} finally { stmt.close(); }
	}
	
	@Test
	public void testFirstMigration() throws Exception {	
		Files.asCharSink(new File(testMigrationDirectory + "/" + update1Title), Charsets.UTF_8).writeFrom(new StringReader(update1));
		migrator.applyUpdates();
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND name = 'temp'");
			assertTrue(rs.next());
		} finally { stmt.close(); }
	}
	
	@Test
	public void testNewMigration() throws Exception {	
		Files.asCharSink(new File(testMigrationDirectory + "/" + update1Title), Charsets.UTF_8).writeFrom(new StringReader(update1));
		Files.asCharSink(new File(testMigrationDirectory + "/" + update2Title), Charsets.UTF_8).writeFrom(new StringReader(update2));
		migrator.applyUpdates();
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND name = 'tmp'");
			assertTrue(rs.next());
			rs = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND name = 'temp'");
			assertTrue(!rs.next());
		} finally { stmt.close(); }
	}
	
	@After
	public void cleanUp() {
		for (File file : testMigrationDirectory.listFiles()) {
			file.delete();
		}
		testMigrationDirectory.delete();
	}
	
	@AfterClass
	public static void terminate() throws SQLException {
		con.close();
	}
}
