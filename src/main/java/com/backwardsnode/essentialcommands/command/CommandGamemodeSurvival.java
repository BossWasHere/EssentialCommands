package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.GameMode;

import com.backwardsnode.essentialcommands.command.base.CommandGamemode;

public class CommandGamemodeSurvival extends CommandGamemode {
	
	public CommandGamemodeSurvival() {
		super("survival", Arrays.asList("gms", "survivalmode"));
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.SURVIVAL;
	}

}
