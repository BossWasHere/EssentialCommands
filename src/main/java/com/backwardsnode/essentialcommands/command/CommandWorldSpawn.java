package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandWorldSpawn extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandWorldSpawn() {
		super("worldspawn", Arrays.asList("wspawn", "wstp"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.teleport");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
		player.teleport(getMainWorld().getSpawnLocation());
		player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.worldspawn.success"));
		return true;
	}
	
	private World getMainWorld() {
		for (World world : Bukkit.getWorlds()) {
			String name = world.getName().toLowerCase();
			if (name.contains("_nether") || name.contains("_the_end")) {
				continue;
			}
			return world;
		}
		return Bukkit.getWorlds().get(0);
	}

}
