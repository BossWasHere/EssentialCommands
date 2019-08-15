package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandPowertool extends EssentialCommand {

	private final Plugin plugin;
	
	public CommandPowertool(Plugin plugin) {
		super("powertool", Arrays.asList("pt", "commandtool"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		this.plugin = plugin;
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item == null) {
			player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.powertool.noitem", player.getLocale()));
		} else {
			Material material = item.getType();
			//TODO Work in progress
		}
		return true;
	}
	
}
