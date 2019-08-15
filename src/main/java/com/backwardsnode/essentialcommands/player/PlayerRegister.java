package com.backwardsnode.essentialcommands.player;

import com.backwardsnode.backwardsapi.register.SimplePlayerRegistry;
import com.backwardsnode.essentialcommands.database.PlayerHomeRegister;

import com.backwardsnode.essentialcommands.Plugin;

public class PlayerRegister {

	public SimplePlayerRegistry godModePlayers;
	public SimplePlayerRegistry flightPlayers;
	public SimplePlayerRegistry vanishedPlayers;
	public PlayerHomeRegister playerHomes;
	
	public void init() {
		Plugin plugin = Plugin.plugin;
		godModePlayers = new SimplePlayerRegistry(true);
		flightPlayers = new SimplePlayerRegistry(true);
		vanishedPlayers = new SimplePlayerRegistry(true);
		playerHomes = new PlayerHomeRegister(plugin);
	}
}
