package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.CommandBulkApplication;

public class CommandFeed extends CommandBulkApplication {

	public CommandFeed() {
		super("feed", Arrays.asList("fillhunger", "eat"), CommandModel.PLAYER_OR_OTHER, LocalizedRegistry.get(), false);
	}

	@Override
	protected boolean applyIndividual(Player player) {
		player.setFoodLevel(20);
		return true;
	}
}
