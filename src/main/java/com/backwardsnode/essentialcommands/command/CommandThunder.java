package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandThunder extends EssentialCommand {

	public CommandThunder() {
		super("thunder", Arrays.asList("rainyweather", "darkweather"), CommandModel.GENERIC, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		player.getWorld().setThundering(true);
		player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.thunder.success", player.getLocale()));
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Bukkit.getWorlds().forEach(world -> world.setThundering(true));
		sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.thunder.success"));
		return true;
	}
}
