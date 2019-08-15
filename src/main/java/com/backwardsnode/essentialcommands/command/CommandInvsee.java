package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandInvsee extends EssentialCommand {

	public CommandInvsee() {
		super("invsee", Arrays.asList("inventoryof", "invedit"), CommandModel.OTHER_PLAYER, LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", player.getLocale(), args[0]));
			} else {
				String name = p.getName();
				Inventory i = Bukkit.createInventory(null, 45, name.endsWith("s") ? name + "' inventory" : name + "'s inventory");
				PlayerInventory pInv = p.getInventory();
				for (int slot = 0; slot < 36; slot++) {
					ItemStack item = pInv.getItem(slot);
					if (item != null) {
						i.setItem(slot, item);
					}
				}
				player.openInventory(i);
			}
		}
		return true;
	}
}
