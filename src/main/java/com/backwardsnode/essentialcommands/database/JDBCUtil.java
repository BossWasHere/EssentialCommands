package com.backwardsnode.essentialcommands.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

public class JDBCUtil {

	public static Connection openConnection(File file) {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:" + file.getAbsolutePath();
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			Bukkit.getLogger().severe("[EC] Cannot load local player database");
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void generateColumnMapping(Map<String, String> existing, String name, String affinityType, boolean notNull, boolean unique) throws IllegalArgumentException {
		if (existing == null) {
			existing = new HashMap<String, String>();
		}
		switch (affinityType.toLowerCase()) {
		case "integer":
		case "numeric":
		case "real":
		case "text":
		case "blob":
		case "boolean":
		case "none":
			break;
		default:
			throw new IllegalArgumentException("No SQLite affinity type " + affinityType);
		}
		existing.put(name, affinityType + (notNull ? " NOT NULL" : "") + (unique ? " UNIQUE" : "") + (notNull && affinityType.equalsIgnoreCase("boolean") ? " DEFAULT (FALSE)" : ""));
	}
	
	public static boolean createTable(Connection conn, String name, String primaryKey, String jointUnique, Map<String, String> columns) {
		String cmd = "CREATE TABLE IF NOT EXISTS " + name + " (" + primaryKey + " integer PRIMARY KEY";
		for (String heading : columns.keySet()) {
			cmd += ", " + heading + " " + columns.get(heading);
		}
		if (jointUnique != null) {
			cmd += ", UNIQUE(" + jointUnique + ") ON CONFLICT REPLACE" ;
		}
		cmd += ");";
		Statement statement = null;
		try {
			statement = conn.createStatement();
			statement.execute(cmd);
		} catch (SQLException e) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static List<String> runSafeStringListQuery(Connection conn, String query, String column) {
		List<String> result = new ArrayList<String>();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				result.add(rs.getString(column));
			}
		} catch (SQLException e) {
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static Map<String, String> runSafeStringMapQuery(Connection conn, String query, String keyColumn, String valueColumn) {
		Map<String, String> result = new HashMap<String, String>();
		Statement statement = null;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				result.put(rs.getString(keyColumn), rs.getString(valueColumn));
			}
		} catch (SQLException e) {
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean isTable(Connection conn, String table) {
		String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'";
		Statement statement = null;
		boolean isTable = false;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			isTable = rs.getString("name").contentEquals(table);
		} catch (SQLException e) {
			return false;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return isTable;
	}
}
