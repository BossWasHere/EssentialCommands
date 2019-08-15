package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.GameMode;

import com.backwardsnode.essentialcommands.command.base.CommandGamemode;

public class CommandGamemodeCreative extends CommandGamemode {
	
	public CommandGamemodeCreative() {
		super("creative", Arrays.asList("gmc", "creativemode"));
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.CREATIVE;
	}

}
