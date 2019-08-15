package com.backwardsnode.essentialcommands.event;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.Plugin;

public class PlayerEventHandler implements Listener {

	private final Plugin plugin;

	public PlayerEventHandler() {
		this.plugin = Plugin.plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		Statement statement = null;
		try {
			String dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
			String name = event.getPlayer().getName();
			statement = plugin.playerDB.createStatement();
			String cmd = "INSERT INTO players (uuid, lastUsername, lastJoined) VALUES ('"
					+ event.getPlayer().getUniqueId().toString() + "', '" + name + "', '" + dt
					+ "') ON CONFLICT(uuid) DO UPDATE SET lastUsername='" + name + "', lastJoined='" + dt + "';";
			statement.execute(cmd);
		} catch (SQLException e) {
			Bukkit.getLogger().warning("SQLite: Could not update table");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		plugin.playerReg.playerHomes.loadHomes(event.getPlayer());
		if (plugin.playerReg.flightPlayers.getAllPlayers().contains(event.getPlayer().getUniqueId().toString())) {
			event.getPlayer().setAllowFlight(true);
			event.getPlayer().sendMessage(ChatColor.AQUA + "[EC] Enabled flight");
		}
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (plugin.playerReg.vanishedPlayers.isPlayerAdded(onlinePlayer)) {
				event.getPlayer().hidePlayer(plugin, onlinePlayer);
			} else {
				event.getPlayer().showPlayer(plugin, onlinePlayer);
			}
		}
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		Statement statement = null;
		try {
			String dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date());
			String name = event.getPlayer().getName();
			String pos = WorldUtil.stringFromLocation(event.getPlayer().getLocation());
			statement = plugin.playerDB.createStatement();
			String cmd = "INSERT INTO players (uuid, lastUsername, lastPos, lastJoined, lastPlayed) VALUES ('"
					+ event.getPlayer().getUniqueId().toString() + "', '" + name + "', '" + pos + "', '" + dt + "', '"
					+ dt + "') ON CONFLICT(uuid) DO UPDATE SET lastPos='" + pos + "', lastPlayed='" + dt + "';";
			statement.execute(cmd);
		} catch (SQLException e) {
			Bukkit.getLogger().warning("SQLite: Could not update table");
			e.printStackTrace();
		} finally {
			try {
				if (!statement.isClosed()) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (plugin.playerReg.godModePlayers.getAllPlayers().contains(player.getUniqueId().toString())) {
				e.setCancelled(true);
			}
		}
	}
}
