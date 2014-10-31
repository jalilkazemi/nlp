package com.jalil.environ.repository;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SafeBatch {
	
	private Connection con;
	
	public SafeBatch(Connection con) {
		this.con = con;
	}
	
	public abstract void run() throws SQLException;
	
	public void commit() throws SQLException {
		boolean originalAutoCommit = con.getAutoCommit();
		try {
			con.setAutoCommit(false);
			run();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally { con.setAutoCommit(originalAutoCommit); }
	}
}
