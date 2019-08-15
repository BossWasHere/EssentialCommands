package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.CommandBulkApplication;

public class CommandHeal extends CommandBulkApplication {

	public CommandHeal() {
		super("heal", Arrays.asList("fillhealth"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), false);
	}
	
	@Override
	protected boolean applyIndividual(Player player) {
		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		return true;
	}
}
