package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.GameMode;

import com.backwardsnode.essentialcommands.command.base.CommandGamemode;

public class CommandGamemodeSpectator extends CommandGamemode {
	
	public CommandGamemodeSpectator() {
		super("spectator", Arrays.asList("gmsp", "spectatormode"));
	}

	@Override
	public GameMode getGamemode() {
		return GameMode.SPECTATOR;
	}

}
