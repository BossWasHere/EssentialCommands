package com.backwardsnode.essentialcommands.command.base;

import java.util.List;

import com.backwardsnode.backwardsapi.ILocalization;
import com.backwardsnode.backwardsapi.command.PluginCommand;
import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;

public abstract class EssentialCommand extends PluginCommand {

	public EssentialCommand(String command, List<String> aliases, CommandModel model, ILocalization baseLocale) {
		super(command, aliases, model, baseLocale);
	}
	
	public EssentialCommand(String command, List<String> aliases, CommandModel model, ILocalization baseLocale, TabCompleteMethod tabCompleteMethod) {
		super(command, aliases, model, baseLocale, tabCompleteMethod);
	}

	@Override
	public String getPluginPrefix() {
		return "essentialcommands";
	}
}
