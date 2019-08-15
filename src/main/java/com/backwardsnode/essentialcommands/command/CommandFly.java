package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.CommandBulkApplication;

public class CommandFly extends CommandBulkApplication {

	private final Plugin plugin;
	
	public CommandFly() {
		super("fly", Arrays.asList("flight", "toggleflight"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), true);
		this.plugin = Plugin.plugin;
	}

	protected boolean applyIndividual(Player player) {
		String uuid = player.getUniqueId().toString();
		if (plugin.playerReg.flightPlayers.isPlayerAdded(uuid)) {
			plugin.playerReg.flightPlayers.removePlayer(uuid);
			player.setAllowFlight(false);
			return false;
		} else {
			plugin.playerReg.flightPlayers.addPlayer(uuid);
			player.setAllowFlight(true);
			return true;
		}
	}

}
