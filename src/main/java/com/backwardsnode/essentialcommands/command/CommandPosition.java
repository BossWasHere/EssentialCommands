package com.backwardsnode.essentialcommands.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.LocalizedRegistry.Locales;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;
import com.backwardsnode.essentialcommands.Plugin;

public class CommandPosition extends EssentialCommand {

	private final Plugin plugin;

	public CommandPosition() {
		super("position", Arrays.asList("getpos", "whereami", "coords", "coordinates", "getloc", "getlocation", "pos"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
		this.plugin = Plugin.plugin;
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			otherPlayer(player, args, Locales.getLocale(player.getLocale()));
		} else {
			Location l = player.getLocation();
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.position.exec", player.getLocale(), l.getBlockX() + "", l.getBlockY() + "", l.getBlockZ() + "", l.getWorld().getName()));	
		}
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			otherPlayer(sender, args, Locales.getFallback());
		} else {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.onlyplayers"));
		}
		return true;
	}
	
	public void otherPlayer(CommandSender sender, String[] args, Locales locale) {
		Player p = Bukkit.getPlayer(args[0]);
		if (p == null) {
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
							sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.position.other", locale, args[0], l.getBlockX() + "", l.getBlockY() + "", l.getBlockZ() + "", l.getWorld() == null ? "unknown" : l.getWorld().getName()));
						} else {
							sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.erroffline", locale, args[0]));
						}
					} else {
						sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.erroffline", locale, args[0]));
					}
				} catch (SQLException e) {
					sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.nooffline", locale, args[0]));
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
		} else {
			Location l = p.getLocation();
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.position.other", locale, p.getName(), l.getBlockX() + "", l.getBlockY() + "", l.getBlockZ() + "", l.getWorld().getName()));
		}
	}

}
