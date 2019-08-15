package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.GameMode;

import com.backwardsnode.essentialcommands.command.base.CommandGamemode;

public class CommandGamemodeAdventure extends CommandGamemode {
	
	public CommandGamemodeAdventure() {
		super("adventure", Arrays.asList("gma", "adventuremode"));
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.ADVENTURE;
	}

}
