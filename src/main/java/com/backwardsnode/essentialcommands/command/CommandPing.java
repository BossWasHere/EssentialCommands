package com.backwardsnode.essentialcommands.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.backwardsnode.backwardsapi.command.CommandModel;
import com.backwardsnode.backwardsapi.reflect.VersionGetter;
import com.backwardsnode.essentialcommands.LocalizedRegistry;
import com.backwardsnode.essentialcommands.command.base.EssentialCommand;

public class CommandPing extends EssentialCommand {

	public CommandPing() {
		super("ping", new ArrayList<String>(), CommandModel.PLAYER_ONLY_SELF, LocalizedRegistry.get());
	}

	@Override
	public boolean executeCommand(Player player, String[] args) {
		try {
			Class<?> craftPlayerClass = Class.forName(VersionGetter.getBukkitPackageHeader() + ".entity.CraftPlayer");
			Class<?> entityPlayerClass = Class.forName(VersionGetter.getPackageHeader() + ".EntityPlayer");
			if (craftPlayerClass.isInstance(player)) {
				Object craftPlayer = craftPlayerClass.cast(player);
				Method getHandle = craftPlayerClass.getMethod("getHandle", new Class<?>[] {});
				Object handle = getHandle.invoke(craftPlayer, new Object[] {});
				Field pingField = entityPlayerClass.getField("ping");
				pingField.setAccessible(true);
				Object objPing = pingField.get(handle);
				Integer ping = (Integer) objPing;
				
				player.sendMessage(LocalizedRegistry.get().getLocalizedString("command.ping.success", player.getLocale(), ping.toString()));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return true;
	}
}
