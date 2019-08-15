package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandSpawn extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandSpawn() {
		super("spawn", Arrays.asList("bedtp"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.teleport");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		String lastPos = WorldUtil.stringFromLocation(player.getLocation());
		try {
			player.teleport(player.getBedSpawnLocation());
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.spawn.success", player.getLocale()));
			plugin.backLocations.put(player.getUniqueId().toString(), lastPos);
		} catch (Exception e) {
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.spawn.notfound", player.getLocale()));
		}
		return true;
	}

}
