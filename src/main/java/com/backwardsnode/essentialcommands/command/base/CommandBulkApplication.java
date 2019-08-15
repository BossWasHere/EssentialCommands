package com.backwardsnode.essentialcommands.command.base;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.ILocalization;
import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.LocalizedRegistry.Locales;

public abstract class CommandBulkApplication extends EssentialCommand {

	private final boolean isToggleable;
	
	public CommandBulkApplication(String command, List<String> aliases, CommandModel model, ILocalization baseLocale, boolean isToggleable) {
		super(command, aliases, model, baseLocale, TabCompleteMethod.ONLINE_PLAYERS);
		this.isToggleable = isToggleable;
	}
	
	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			applyBulk(player, args, true);
		} else {
			boolean enabled = applyIndividual(player);
			if (isToggleable) {
				player.sendMessage(feedback_Success_Toggleable(Locales.getLocale(player.getLocale()), enabled ? "enabled" : "disabled"));
			} else {
				player.sendMessage(feedback_Success(Locales.getLocale(player.getLocale())));
			}
		}
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			applyBulk(sender, Locales.getFallback(), args, true);
		} else {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.onlyplayers"));
		}
		return true;
	}
	
	public final void applyBulk(Player player, String[] args, boolean notifyPlayer) {
		applyBulk(player, Locales.getLocale(player.getLocale()), args, notifyPlayer);
	}
	
	public final void applyBulk(CommandSender sender, Locales locale, String[] args, boolean notifyPlayer) {
		int playersChanged = 0;
		for (String name : args) {
			Player p = Bukkit.getPlayer(name);
			if (p != null) {
				applyIndividual(p);
				playersChanged++;
				sender.sendMessage(feedback_RunOther(locale, p.getName()));
				if (notifyPlayer) {
					p.sendMessage(feedback_RunBy(Locales.getLocale(p.getLocale()), sender.getName()));
				}
			} else {
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", locale, name));
			}
		}
		if (args.length > 1) {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command." + getName() + ".bulkcomplete", locale, playersChanged + "", args.length + ""));
		}
	}
	
	protected String feedback_Success(Locales locale) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".success", locale);
	}
	
	protected String feedback_Success_Toggleable(Locales locale, String toggleOutput) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".success", locale, toggleOutput);
	}
	
	protected String feedback_RunOther(Locales locale, String targetName) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".runother", locale, targetName);
	}
	
	protected String feedback_RunBy(Locales locale, String executorName) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".runby", locale, executorName);
	}

	protected abstract boolean applyIndividual(Player player);
}
