package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandWorkbench extends EssentialCommand {

	public CommandWorkbench() {
		super("workbench", Arrays.asList("wbench", "wb"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
			player.openWorkbench(null, true);
		return true;
	}

}
