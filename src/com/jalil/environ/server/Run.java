package com.jalil.environ.server;

import java.sql.Connection;

import static com.jalil.environ.Configuration.*;
import com.jalil.environ.repository.Migrator;
import com.jalil.environ.repository.SQLiteJDBC;

public class Run {
	
	public static void main(String[] args) {
		Connection con = SQLiteJDBC.getConnection();
		Migrator migrator = new Migrator(migrationDirectory, con);
		try {
			migrator.applyUpdates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
