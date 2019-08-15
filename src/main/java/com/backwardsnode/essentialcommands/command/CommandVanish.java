package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandVanish extends EssentialCommand {

	public CommandVanish() {
		super("vanish", Arrays.asList("v"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		Plugin plugin = Plugin.plugin;
		if (plugin.playerReg.vanishedPlayers.isPlayerAdded(player)) {
			plugin.playerReg.vanishedPlayers.removePlayer(player);
			Bukkit.getOnlinePlayers().forEach(online -> online.showPlayer(plugin, player));
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.vanish.showing", player.getLocale()));
		} else {
			plugin.playerReg.vanishedPlayers.addPlayer(player);
			Bukkit.getOnlinePlayers().forEach(online -> online.hidePlayer(plugin, player));
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.vanish.hiding", player.getLocale()));
		}
		return true;
	}

}
