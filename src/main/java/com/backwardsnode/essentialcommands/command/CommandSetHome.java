package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandSetHome extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandSetHome() {
		super("sethome", Arrays.asList("sethometp"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		String homeName = "home";
		if (args.length > 0) {
			homeName = args[0];
		}
		plugin.playerReg.playerHomes.getPlayerHomes(player).setHome(homeName, WorldUtil.stringFromLocation(player.getLocation()));
		player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.sethome.success", player.getLocale(), homeName));
		return true;
	}
	
}
