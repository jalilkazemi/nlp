package com.jalil.environ.repository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;


public class Migrator {

	private final File migrationDirectory;
	private final Connection con;
	
	public Migrator(File migrationDirectory, Connection con) {
		this.migrationDirectory = migrationDirectory;
		this.con = con;
	}
	
	public void applyUpdates() throws Exception {
	}
}
