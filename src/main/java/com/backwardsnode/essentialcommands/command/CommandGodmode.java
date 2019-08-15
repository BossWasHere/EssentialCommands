package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.CommandBulkApplication;

public class CommandGodmode extends CommandBulkApplication {
	
	private final Plugin plugin;
	
	public CommandGodmode() {
		super("god", Arrays.asList("togglegod", "godmode"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), true);
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.gamemode");
	}
	
	@Override
	protected boolean applyIndividual(Player player) {
		String uuid = player.getUniqueId().toString();
		if (plugin.playerReg.godModePlayers.isPlayerAdded(uuid)) {
			plugin.playerReg.godModePlayers.removePlayer(uuid);
			return false;
		} else {
			plugin.playerReg.godModePlayers.addPlayer(uuid);
			return true;
		}
	}
}
