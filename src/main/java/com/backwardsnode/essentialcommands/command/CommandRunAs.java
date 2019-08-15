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

public class CommandRunAs extends EssentialCommand {

	public CommandRunAs() {
		super("runas", Arrays.asList("sudo", "as", "executeas"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
		setUsage("/runas <player> <command>|c:<text>");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", player.getLocale()));
			} else {
				String command = "";
				for (int i = 1; i < args.length; i++) {
					command += args[i] + " ";
				}
				if (command.toLowerCase().startsWith("c:")) {
					command = command.substring(2);
					target.chat(command);
				} else {
					if (!command.startsWith("/")) {
						command = "/" + command;
					}
					target.chat(command);
				}
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.runas.success", player.getLocale(), target.getName()));
			}
		} else {
			player.sendMessage(getUsage());
		}
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length > 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound"));
			} else {
				String command = "";
				for (int i = 1; i < args.length; i++) {
					command += args[i] + " ";
				}
				if (command.toLowerCase().startsWith("c:")) {
					command = command.substring(2);
					target.chat(command);
				} else {
					if (!command.startsWith("/")) {
						command = "/" + command;
					}
					target.chat(command);
				}
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.runas.success", Locales.getFallback(), target.getName()));
			}
		} else {
			sender.sendMessage(getUsage());
		}
		return true;
	}
	
}
