package com.backwardsnode.essentialcommands.command;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.command.ICommandConfigurable;
import com.backwardsnode.backwardsapi.command.TabCompleteMethod;
import com.backwardsnode.backwardsapi.util.WorldUtil;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.LocalizedRegistry.Locales;
import com.backwardsnode.essentialcommands.Plugin;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandRandomTP extends EssentialCommand implements ICommandConfigurable {

	private final Plugin plugin;
	
	private int maxDistance = 32;
	private int cooldownPeriod = 1200;
	
	public CommandRandomTP() {
		super("randomtp", Arrays.asList("rtp"), CommandModel.asCoolableModel(CommandModel.PLAYER_OR_OTHER, Plugin.plugin.cooldownManager.attach("rtp")), LocalizedRegistry.get(), TabCompleteMethod.ONLINE_PLAYERS);
		this.plugin = Plugin.plugin;
		setOverridePermission("minecraft.command.teleport");
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		if (args.length > 0) {
			applyBulk(player, Locales.getLocale(player.getLocale()), args, true);
		} else {
			if (teleport(player, 0)) {
				String loc = String.format("%d,%d,%d", player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.success", player.getLocale(), loc));
			} else {
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.nosafe", player.getLocale()));
			}
		}
		return true;
	}
	
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length > 0) {
			applyBulk(sender, Locales.getFallback(), args, true);
		} else {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.onlyplayers"));
		}
		return true;
	}	
	private boolean teleport(Player p, int stackdepth) {
		if (stackdepth > 20) return false;
		Random r = new Random();
		int dX = r.nextInt(maxDistance * 2) - maxDistance;
		int dZ = r.nextInt(maxDistance * 2) - maxDistance;
		
		Location location = p.getLocation();
		location = new Location(location.getWorld(), location.getBlockX() + dX, location.getBlockY(), location.getBlockZ() + dZ);
		
		int y = r.nextInt(240) + 14;
		if (location.getChunk().load(true)) {
			for (; y < 255; y++) {
				location.setY(y);
				if (location.getBlock().getType() == Material.AIR && location.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
					Location below = location.getBlock().getRelative(BlockFace.DOWN).getLocation();
					if (below.getBlock().getType().isSolid()) {
						plugin.backLocations.put(p.getUniqueId().toString(), WorldUtil.stringFromLocation(p.getLocation()));
						p.teleport(location.add(0.5, 0, 0.5));
						return true;
					}
					y += 2;
				}
			}
		}
		return teleport(p, stackdepth + 1);
	}
	
	public void applyBulk(CommandSender sender, Locales locale, String[] args, boolean notifyPlayer) {
		int playersChanged = 0;
		for (String name : args) {
			Player p = Bukkit.getPlayer(name);
			if (p != null) {
				if (teleport(p, 0)) {
					playersChanged++;
					String loc = String.format("%d,%d,%d", p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
					sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.runother", locale, p.getName(), loc));
					if (notifyPlayer) {
						p.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.runby", p.getLocale(), sender.getName()));
					}
				} else {
					sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.nosafefor", locale, name));
				}
			} else {
				sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.default.playernotfound", locale, name));
			}
		}
		if (args.length > 1) {
			sender.sendMessage(LocalizedRegistry.get().getLocalizedString("command.randomtp.bulkcomplete", locale, playersChanged + "", args.length + ""));
		}
	}

	@Override
	public void fetchConfig(FileConfiguration config) {
		maxDistance = config.getInt("commands.randomtp.distance", maxDistance);
		cooldownPeriod = config.getInt("commands.randomtp.cooldown", cooldownPeriod);
	}

}
