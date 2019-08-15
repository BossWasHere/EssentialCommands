package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.LocalizedRegistry.Locales;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandFire extends EssentialCommand {

	public CommandFire() {
		super("fire", Arrays.asList("burn", "ignite"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
	}
	
	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", player.getLocale(), args[0]));
			} else {
				int duration = 100;
				if (args.length > 1) {
					try {
						duration = Integer.parseInt(args[1]) * 20;
					} catch (NumberFormatException e) { }
				}
				p.setFireTicks(duration);
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.fire.success", player.getLocale(), p.getName(), duration / 20 + ""));
			}
		} else {
			player.setFireTicks(100);
		}
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", Locales.getFallback(), args[0]));
			} else {
				int duration = 100;
				if (args.length > 1) {
					try {
						duration = Integer.parseInt(args[1]) * 20;
					} catch (NumberFormatException e) { }
				}
				p.setFireTicks(duration);
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.fire.success", Locales.getFallback(), p.getName(), duration / 20 + ""));
			}
		} else {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.onlyplayers"));
		}
		return true;
	}

}
