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
	
	@BeforeClass
	public static void start() {
		con = FakeSQLiteJDBC.getConnection();
	}
	
	@Before
	public void init() {
		testMigrationDirectory.mkdirs();
		migrator = new Migrator(testMigrationDirectory, con);		
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
