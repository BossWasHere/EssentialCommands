package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandGive extends EssentialCommand {

	public CommandGive() {
		super("getitem", Arrays.asList("i"), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
		setOverridePermission("minecraft.command.give");
	}
	
	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			String fullTag = "";
			int amount = -1;
			if (args.length > 1) {
				try {
					amount = Integer.parseInt(args[args.length - 1]);
					for (int p = 0; p < args.length - 1; p++) {
						fullTag += args[p] + "_";
					}
				} catch (NumberFormatException e) {
					for (int p = 0; p < args.length; p++) {
						fullTag += args[p] + "_";
					}
				}
			}
			String itemRequest;
			if (fullTag.length() > 0) {
				fullTag = fullTag.substring(0, fullTag.length() - 1);
				itemRequest = fullTag.toUpperCase();
			} else {
				itemRequest = args[0].toUpperCase();
				fullTag = args[0];
			}
			
			Material m = null;
			try {
				m = Material.matchMaterial(itemRequest);
				if (m == null) {
					m = Material.valueOf(itemRequest);
				}
			} catch (IllegalArgumentException e) {}
			if (m != null) {
				if (amount < 1) {
					amount = m.getMaxStackSize();
				}
				player.getInventory().addItem(new ItemStack(m, amount));
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.getitem.success", player.getLocale(), amount + "", fullTag));
			} else {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.getitem.noitem", player.getLocale(), fullTag));
			}
		} else {
			player.sendMessage(getUsage());
		}
		return true;
	}

}
