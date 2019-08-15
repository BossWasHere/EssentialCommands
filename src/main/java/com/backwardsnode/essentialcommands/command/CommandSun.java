package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandSun extends EssentialCommand {

	public CommandSun() {
		super("sun", Arrays.asList("clearweather", "norain"), CommandModel.GENERIC, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		player.getWorld().setThundering(false);
		player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.sun.success", player.getLocale()));
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Bukkit.getWorlds().forEach(world -> world.setThundering(false));
		sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.sun.success"));
		return true;
	}
}
