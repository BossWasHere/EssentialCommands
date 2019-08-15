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

public class CommandExtinguish extends EssentialCommand {

	public CommandExtinguish() {
		super("extinguish", Arrays.asList("ext"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
	}
	
	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", player.getLocale(), args[0]));
			} else {
				p.setFireTicks(0);
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.extinguish.success", player.getLocale(), p.getName()));
			}
		} else {
			player.setFireTicks(0);
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
				p.setFireTicks(0);
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.extinguish.success", Locales.getFallback(), p.getName()));
			}
		} else {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.onlyplayers"));
		}
		return true;
	}

}
