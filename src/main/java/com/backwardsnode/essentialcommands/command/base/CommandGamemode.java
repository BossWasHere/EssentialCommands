package com.backwardsnode.essentialcommands.command.base;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.LocalizedRegistry.Locales;
import com.backwardsnode.essentialcommands.util.GamemodeUtil;

public abstract class CommandGamemode extends CommandBulkApplication {
	
	public CommandGamemode(String command, List<String> aliases) {
		super(command, aliases, CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), false);
		setOverridePermission("minecraft.command.gamemode");
	}
	
	@Override
	protected boolean applyIndividual(Player player) {
		GamemodeUtil.changeGamemode(player, getGamemode());
		return true;
	}
	
	public abstract GameMode getGamemode();
	
	@Override
	protected String feedback_Success(Locales locale) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".success", locale, getGamemode().toString().toLowerCase());
	}
	
	@Override
	protected String feedback_Success_Toggleable(Locales locale, String toggleOutput) {
		return feedback_Success(locale);
	}
	
	@Override
	protected String feedback_RunBy(Locales locale, String executorName) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".runby", locale, getGamemode().toString().toLowerCase(), executorName);
	}
	
	@Override
	protected String feedback_RunOther(Locales locale, String targetName) {
		return LocalizedRegistry.get().getLocalizedString("command." + getName() + ".runother", locale, targetName, getGamemode().toString().toLowerCase());
	}
}
