package com.backwardsnode.essentialcommands.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.backwardsnode.essentialcommands.Plugin;

public class GamemodeUtil {

	public static void changeGamemode(Player player, GameMode gamemode) {
		player.setGameMode(gamemode);
		updateFlightState(player, player.getGameMode().equals(GameMode.CREATIVE));
	}
	
	public static void updateFlightState(Player player, boolean defaultFlyingMode) {
		if (defaultFlyingMode) {
			player.setAllowFlight(true);
		} else if (Plugin.plugin.playerReg.flightPlayers.isPlayerAdded(player)) {
			player.setAllowFlight(true);
		} else {
			player.setAllowFlight(false);
		}
	}
	
}
