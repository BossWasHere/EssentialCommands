package com.backwardsnode.essentialcommands.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;

public class PlayerHomeRegister {

	private Map<String, PlayerHomeRegister.HomeSet> homeData;
	private final Plugin plugin;
	
	public PlayerHomeRegister(Plugin plugin) {
		this.plugin = plugin;
		homeData = new HashMap<String, PlayerHomeRegister.HomeSet>();
	}
	
	public PlayerHomeRegister.HomeSet getPlayerHomes(Player player) {
		return homeData.get(player.getUniqueId().toString());
	}
	
	public void loadHomes(Player player) {
		String uuid = player.getUniqueId().toString();
		if (!homeData.containsKey(uuid)) {
			queryDatabase(uuid);
		}
	}
	
	private void queryDatabase(String uuid) {
		HomeSet homeSet = new HomeSet(uuid);
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = plugin.playerDB.createStatement();
			rs = statement.executeQuery("SELECT homeName,pos FROM homes WHERE uuid='" + uuid + "' ORDER BY id ASC;");
			if (!rs.isClosed()) {
				do {
					homeSet.addHomeUnsafe(rs.getString("homeName"), rs.getString("pos"));
				} while (rs.next());
			}
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (rs != null) { 
					rs.close();
				}
			} catch (SQLException e) {
				plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
				e.printStackTrace();
			}
		}
		homeData.put(uuid, homeSet);
	}
	
	public void saveHomeData() {
		for (String uuid : homeData.keySet()) {
			for (PlayerHomeRegister.HomeLocation hl : homeData.get(uuid).homes) {
				addHome(uuid, hl.homeName, hl.pos);
			}
		}
		
	}
	
	private void addHome(String uuid, String homeName, String pos) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = plugin.playerDB.createStatement();
			statement.execute("INSERT OR REPLACE INTO homes(uuid,homeName,pos) VALUES('" + uuid + "','" + homeName + "','" + pos +  "')");
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (rs != null) { 
					rs.close();
				}
			} catch (SQLException e) {
				plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private boolean doesHomeExist(String homeName) {
		Statement statement = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			statement = plugin.playerDB.createStatement();
			rs = statement.executeQuery("SELECT EXISTS(SELECT 1 FROM homes WHERE homeName='" + homeName + "');");
			exists = rs.getBoolean(1);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (rs != null) { 
					rs.close();
				}
			} catch (SQLException e) {
				plugin.getLogger().log(Level.SEVERE, LocalizedRegistry.get().getLocalizedString("sql.err"));
				e.printStackTrace();
			}
		}
		return exists;
	}
	
	public static class HomeSet {
		
		private final String uuid;
		private List<PlayerHomeRegister.HomeLocation> homes;
		
		public HomeSet(String uuid) {
			this.uuid = uuid;
			homes = new ArrayList<PlayerHomeRegister.HomeLocation>();
		}
		
		public void addHomeUnsafe(String homeName, String pos) {
			homes.add(new HomeLocation(homeName, pos));
		}
		
		public void setHome(String homeName, String pos) {
			for (PlayerHomeRegister.HomeLocation hl : homes) {
				if (hl.homeName.equals(homeName)) {
					hl.pos = pos;
					return;
				}
			}
			homes.add(new PlayerHomeRegister.HomeLocation(homeName, pos));
		}
		
		public boolean removeHome(String homeName) {
			for (int i = 0; i < homes.size(); i++) {
				if (homes.get(i).homeName.equals(homeName)) {
					homes.remove(i);
					return true;
				}
			}
			return false;
		}
		
		public String getUUID() {
			return uuid;
		}
		
		public String getDefaultHomePosition() {
			String home = getHomePositionByName("home");
			return home == null ? getHome(0) : home;
		}
		
		public String getHome(int index) {
			try {
				return homes.get(index).pos;
			} catch (IndexOutOfBoundsException e) {
				return null;
			}
		}
		
		public String getHomePositionByName(String homeName) {
			for (PlayerHomeRegister.HomeLocation hl : homes) {
				if (hl.homeName.equals(homeName)) {
					return hl.pos;
				}
			}
			return null;
		}
		
		public boolean isHome(String homeName) {
			return getHomePositionByName(homeName) != null;
		}
		
		public Map<String, Boolean> getHomesAsMap() {
			Map<String, Boolean> homeMap = new HashMap<String, Boolean>();
			for (PlayerHomeRegister.HomeLocation hl : homes) {
				homeMap.put(hl.homeName, hl.isValidLocation());
			}
			return homeMap;
		}
	}
	
	private static class HomeLocation {
		
		private final String homeName;
		private String pos;
		
		private HomeLocation(String homeName, String pos) {
			this.homeName = homeName;
			this.pos = pos;
		}
		
		private boolean isValidLocation() {
			return pos.split(",").length == 6;
		}
	}
}
