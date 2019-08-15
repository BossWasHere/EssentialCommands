package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandBreak extends EssentialCommand	 {

	public CommandBreak() {
		super("break", Arrays.asList("breakblock"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		Block block = player.getTargetBlockExact(50, FluidCollisionMode.SOURCE_ONLY);
		if (block != null) {
			block.setType(Material.AIR);
		} else {
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.break.noblock", player.getLocale()));
		}
		return true;
	}
	
}
