package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.ICommandConfigurable;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandBack extends EssentialCommand implements ICommandConfigurable {

	private final Plugin plugin;
	
	private int cooldownPeriod = 300;
	
	public CommandBack() {
		super("back", Arrays.asList("tpback"), CommandModel.asCoolableModel(CommandModel.PLAYER_ONLY_SELF, Plugin.plugin.cooldownManager.attach("back")), LocalizedRegistry.get());
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.teleport");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		String uuid = player.getUniqueId().toString();
		if (plugin.backLocations.containsKey(uuid)) {
			Location l = WorldUtil.locationFromString(plugin.backLocations.get(uuid));
			if (l != null) {
				plugin.backLocations.put(player.getUniqueId().toString(), WorldUtil.stringFromLocation(player.getLocation()));
				player.teleport(l);
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.back.teleport", player.getLocale()));
				addCooldown(player, cooldownPeriod * 1000);
			}
		} else {
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.back.nolocation", player.getLocale()));
		}
		return true;
	}

	@Override
	public void fetchConfig(FileConfiguration config) {
		cooldownPeriod = config.getInt("commands.back.cooldown", cooldownPeriod);
	}

}
