package com.backwardsnode.essentialcommands.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandRemoveHome extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandRemoveHome() {
		super("removehome", Arrays.asList("removehometp", "remhome", "delhome", "deletehome"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		String homeName = "home";
		if (args.length > 0) {
			homeName = args[0];
		}
		if (plugin.playerReg.playerHomes.getPlayerHomes(player).isHome(homeName)) {
			if (plugin.playerReg.playerHomes.getPlayerHomes(player).removeHome(homeName)) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.removehome.success", player.getLocale(), homeName));
			}
		} else {
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.removehome.noexist", player.getLocale(), homeName));
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args, Location location) {
		List<String> homeList = new ArrayList<String>();
		if (sender instanceof Player) {
			Map<String, Boolean> homes = plugin.playerReg.playerHomes.getPlayerHomes((Player)sender).getHomesAsMap();
			for (String key : homes.keySet()) {
				homeList.add(key);
			}
		}
		return homeList;
	}
}
