package com.backwardsnode.essentialcommands;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.backwardsnode.backwardsapi.CooldownManager;
import com.backwardsnode.backwardsapi.command.ICommandConfigurable;
import com.backwardsnode.backwardsapi.command.PluginCommandHelp;
import com.backwardsnode.essentialcommands.command.CommandBack;
import com.backwardsnode.essentialcommands.command.CommandBreak;
import com.backwardsnode.essentialcommands.command.CommandExtinguish;
import com.backwardsnode.essentialcommands.command.CommandFeed;
import com.backwardsnode.essentialcommands.command.CommandFire;
import com.backwardsnode.essentialcommands.command.CommandFly;
import com.backwardsnode.essentialcommands.command.CommandGamemodeAdventure;
import com.backwardsnode.essentialcommands.command.CommandGamemodeCreative;
import com.backwardsnode.essentialcommands.command.CommandGamemodeSpectator;
import com.backwardsnode.essentialcommands.command.CommandGamemodeSurvival;
import com.backwardsnode.essentialcommands.command.CommandGive;
import com.backwardsnode.essentialcommands.command.CommandGodmode;
import com.backwardsnode.essentialcommands.command.CommandHeal;
import com.backwardsnode.essentialcommands.command.CommandHome;
import com.backwardsnode.essentialcommands.command.CommandInvsee;
import com.backwardsnode.essentialcommands.command.CommandPing;
import com.backwardsnode.essentialcommands.command.CommandPosition;
import com.backwardsnode.essentialcommands.command.CommandRandomTP;
import com.backwardsnode.essentialcommands.command.CommandRemoveHome;
import com.backwardsnode.essentialcommands.command.CommandRunAs;
import com.backwardsnode.essentialcommands.command.CommandSetHome;
import com.backwardsnode.essentialcommands.command.CommandSpawn;
import com.backwardsnode.essentialcommands.command.CommandSun;
import com.backwardsnode.essentialcommands.command.CommandTPTo;
import com.backwardsnode.essentialcommands.command.CommandThunder;
import com.backwardsnode.essentialcommands.command.CommandVanish;
import com.backwardsnode.essentialcommands.command.CommandWorkbench;
import com.backwardsnode.essentialcommands.command.CommandWorldSpawn;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;
import com.backwardsnode.essentialcommands.database.JDBCUtil;
import com.backwardsnode.essentialcommands.event.PlayerEventHandler;
import com.backwardsnode.essentialcommands.player.PlayerRegister;

public class Plugin extends JavaPlugin {
	
	private EssentialCommand[] _commands;
	private FileConfiguration config;
	
	protected PlayerEventHandler _playerEventHandler;
	
	public static Plugin plugin;
	public Connection playerDB;
	public PlayerRegister playerReg;
	public Map<String, String> backLocations;
	public CooldownManager cooldownManager;
	
	public Plugin() {
		plugin = this;
	}
	
	@Override
	public void onLoad() {
		cooldownManager = new CooldownManager();
		//
		// Pre-Init
		//
		_commands = new EssentialCommand[] {
				new CommandBack(),
				new CommandBreak(),
				new CommandExtinguish(),
				new CommandFeed(),
				new CommandFire(),
				new CommandFly(),
				new CommandGamemodeAdventure(),
				new CommandGamemodeCreative(),
				new CommandGamemodeSpectator(),
				new CommandGamemodeSurvival(),
				new CommandGive(),
				new CommandGodmode(),
				new CommandHeal(),
				new CommandHome(),
				new CommandInvsee(),
				new CommandPing(),
				new CommandPosition(),
				//new CommandPowertool(), TODO Work in progress
				new CommandRandomTP(),
				new CommandRemoveHome(),
				new CommandRunAs(),
				new CommandSetHome(),
				new CommandSpawn(),
				new CommandSun(),
				new CommandThunder(),
				new CommandTPTo(),
				new CommandVanish(),
				new CommandWorkbench(),
				new CommandWorldSpawn()
		};
		saveDefaultConfig();
		playerReg = new PlayerRegister();
		playerReg.init();
	}
	
	@Override
	public void onEnable() {
		//
		// Init
		//
		config = getConfig();
		initializeDatabase();
		
		playerReg.godModePlayers.addAllPlayerNames(JDBCUtil.runSafeStringListQuery(playerDB, "SELECT uuid FROM players WHERE godMode=1;", "uuid"));
		playerReg.flightPlayers.addAllPlayerNames(JDBCUtil.runSafeStringListQuery(playerDB, "SELECT uuid FROM players WHERE flight=1;", "uuid"));
		backLocations = new HashMap<String, String>();
		
		//Reload safety
		for (Player player : Bukkit.getOnlinePlayers()) {
			playerReg.playerHomes.loadHomes(player);
		}
		
		PluginCommandHelp helpCommand = new PluginCommandHelp("echelp", "EssentialCommands", Arrays.asList("ech", "essentialhelp"), _commands);
		
		CommandMap commandMap = null;
		try {
			Field field = SimplePluginManager.class.getDeclaredField("commandMap");
			field.setAccessible(true);
			commandMap = (CommandMap)(field.get(getServer().getPluginManager()));
			
			for (EssentialCommand provider : _commands) {
				if (provider instanceof ICommandConfigurable) {
					((ICommandConfigurable) provider).fetchConfig(config);
				}
				commandMap.register("essentialcommands", provider);
			}
			commandMap.register("essentialcommands", helpCommand);
		} catch (ReflectiveOperationException e) {
			getLogger().severe("[EC] An error occured while building the new commandmap");
			e.printStackTrace();
		}
		
		_playerEventHandler = new PlayerEventHandler();
		
		//
		// Post-Init
		//
		for (String uuid : playerReg.flightPlayers.getAllPlayers()) {
			Player p = Bukkit.getPlayer(UUID.fromString(uuid));
			if (p != null) {
				if (p.isOnline()) {
					p.setFlying(true);
					p.sendMessage(ChatColor.AQUA + "[EC] Enabled flight");
				}
			}
		}
	}
	
	private void initializeDatabase() {
		playerDB = JDBCUtil.openConnection(new File(getDataFolder(), "player.db"));
		if (!JDBCUtil.isTable(playerDB, "players")) {
			Map<String, String> cols = new HashMap<String, String>();
			JDBCUtil.generateColumnMapping(cols, "uuid", "text", true, true);
			JDBCUtil.generateColumnMapping(cols, "lastUsername", "text", false, false);
			JDBCUtil.generateColumnMapping(cols, "lastPos", "text", false, false);
			JDBCUtil.generateColumnMapping(cols, "lastJoined", "text", false, false);
			JDBCUtil.generateColumnMapping(cols, "lastPlayed", "text", false, false);
			JDBCUtil.generateColumnMapping(cols, "godMode", "boolean", true, false);
			JDBCUtil.generateColumnMapping(cols, "flight", "boolean", true, false);
			JDBCUtil.generateColumnMapping(cols, "vanish", "boolean", true, false);
			JDBCUtil.generateColumnMapping(cols, "teleportFrom", "text", false, false);
			JDBCUtil.createTable(playerDB, "players", "id", null, cols);
		}
		if (!JDBCUtil.isTable(playerDB, "homes")) {
			Map<String, String> cols = new HashMap<String, String>();
			JDBCUtil.generateColumnMapping(cols, "uuid", "text", true, false);
			JDBCUtil.generateColumnMapping(cols, "homeName", "text", true, false);
			JDBCUtil.generateColumnMapping(cols, "pos", "text", true, false);
			JDBCUtil.createTable(playerDB, "homes", "id", "uuid, homeName", cols);
		}
		if (!JDBCUtil.isTable(playerDB, "powertool")) {
			Map<String, String> cols = new HashMap<String, String>();
			JDBCUtil.generateColumnMapping(cols, "uuid", "text", true, false);
			JDBCUtil.generateColumnMapping(cols, "itemId", "text", true, false);
			JDBCUtil.generateColumnMapping(cols, "commands", "text", true, false);
			JDBCUtil.createTable(playerDB, "powertool", "id", null, cols);
		}
	}
	
	@Override
	public void onDisable() {
		try {
			Statement statement = null;
			try {
				statement = playerDB.createStatement();
				statement.execute("UPDATE players SET godMode = 0;");
				statement.execute("UPDATE players SET flight = 0;");
				for (String uuid : playerReg.godModePlayers.getAllPlayers()) {
					statement.execute("UPDATE players SET godMode=1 WHERE uuid='" + uuid + "';");
				}
				for (String uuid : playerReg.flightPlayers.getAllPlayers()) {
					statement.execute("UPDATE players SET flight=1 WHERE uuid='" + uuid + "';");
				}
			} catch (SQLException e) {
				Bukkit.getLogger().warning("SQLite: Could not write to database closing data");
				e.printStackTrace();
			} finally {
				if (statement != null) {
					statement.close();
				}
			}
			playerReg.playerHomes.saveHomeData();
			playerDB.close();
		} catch (SQLException e) {
			Bukkit.getLogger().warning("SQLite: Could not close database connection");
			e.printStackTrace();
		}
	}
}
