package com.backwardsnode.essentialcommands.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandHome extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandHome() {
		super("home", Arrays.asList("tphome", "homes"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.teleport");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			String pos = plugin.playerReg.playerHomes.getPlayerHomes(player).getHomePositionByName(args[0]);
			if (pos == null) {
				String homeStr = ChatColor.GREEN + "";
				Map<String, Boolean> homes = plugin.playerReg.playerHomes.getPlayerHomes(player).getHomesAsMap();
				boolean modified = false;
				for (String key : homes.keySet()) {
					modified = true;
					if (homes.get(key)) {
						homeStr += key + ", ";
					} else {
						homeStr += ChatColor.RED + key + ChatColor.GREEN + ", ";
					}
				}
				if (modified) {
					homeStr = homeStr.substring(0, homeStr.length() - 2);
				}
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.home.list", player.getLocale(), homeStr));
			} else {
				Location loc = WorldUtil.locationFromString(pos);
				if (loc != null) {
					plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
					player.teleport(loc);
					player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.default", player.getLocale()));
				}
			}
		} else {
			String pos = plugin.playerReg.playerHomes.getPlayerHomes(player).getDefaultHomePosition();
			if (pos == null) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.home.none", player.getLocale()));
			} else {
				Location loc = WorldUtil.locationFromString(pos);
				if (loc != null) {
					plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
					player.teleport(loc);
					player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.teleport.default", player.getLocale()));
				}
			}
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args, Location location) {
		List<String> homeList = new ArrayList<String>();
		if (sender instanceof Player) {
			Map<String, Boolean> homes = plugin.playerReg.playerHomes.getPlayerHomes((Player)sender).getHomesAsMap();
			for (String key : homes.keySet()) {
				if (homes.get(key)) {
					homeList.add(key);
				}
			}
		}
		return homeList;
	}
	
}
