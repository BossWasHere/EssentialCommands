package com.backwardsnode.essentialcommands.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandTPTo extends EssentialCommand {
	
	private final Plugin plugin;
	
	public CommandTPTo() {
		super("teleportto", Arrays.asList("tpto", "tp2p"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
		this.plugin = Plugin.plugin;
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				if (player.hasPermission("essentialcommands.tpto.offline")) {
					//Begin SQLite Query
					Statement statement = null;
					ResultSet rs = null;
					try {
						statement = plugin.playerDB.createStatement();
						rs = statement.executeQuery("SELECT lastPos FROM players WHERE lastUsername='" + args[0] + "' COLLATE NOCASE;");
						try {
							String locData = rs.getString("lastPos");
							if (locData.split(",").length == 6) {
								Location l = WorldUtil.locationFromString(locData);
								if (l != null) {
									plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
									player.teleport(l);
									player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.tooffline", player.getLocale(), args[0]));
								} else {
									player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.erroffline", player.getLocale(), args[0]));
								}
							} else {
								player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.erroffline", player.getLocale(), args[0]));
							}
						} catch (SQLException e) {
							player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.nooffline", player.getLocale(), args[0]));
						}
					} catch (SQLException e) {
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
							e.printStackTrace();
						}
					}
					//End SQLite Query
				} else {
					player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.noperm", player.getLocale()));
				}
			} else {
				if (p.getName().contentEquals(player.getName())) {
					player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.noself", player.getLocale()));
				} else {
					plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
					player.teleport(p);
					player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.to", player.getLocale(), p.getName()));
				}
			}
		} else {
			player.sendMessage(getUsage());
		}
		return true;
	}

}
