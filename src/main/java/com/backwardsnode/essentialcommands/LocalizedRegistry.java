package com.backwardsnode.essentialcommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

import com.backwardsnode.backwardsapi.ILocalization;

public class LocalizedRegistry implements ILocalization {

	private static LocalizedRegistry _localization;
	
	public static LocalizedRegistry get() {
		if (_localization == null) {
			_localization = new LocalizedRegistry();
		}
		return _localization;
	}
	
	private HashMap<Locales, HashMap<String, String>> loaded;
	
	private LocalizedRegistry() { 
		loaded = new HashMap<Locales, HashMap<String,String>>();
	}
	
	private HashMap<String, String> getLocale(Locales target) {
		if (loaded.containsKey(target)) {
			return loaded.get(target);
		}
		InputStream in = null;
		BufferedReader bfr = null;
		HashMap<String, String> load = new HashMap<String, String>();
		try {
			in = getClass().getResourceAsStream("/" + target.getFileDef() + ".txt");
			bfr = new BufferedReader(new InputStreamReader(in));
			Iterator<String> lines = bfr.lines().iterator();
			while (lines.hasNext()) {
				String line = lines.next();
				if (line.contains("=")) {
					String[] translation = line.split("=");
					load.put(translation[0], translation[1]);
				}
			}
			loaded.put(target, load);
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) in.close();
				if (bfr != null) bfr.close();
			} catch (IOException e) {}
		}
		return load;
	}
	
	private String get(String unlocalizedString, HashMap<String, String> index) {
		String value = index.get(unlocalizedString);
		if (value == null) return unlocalizedString;
		return value;
	}
	
	public String getLocalizedString(String unlocalizedString) {
		return getLocalizedString(unlocalizedString, Locales.getFallback());
	}
	
	public String getLocalizedString(String unlocalizedString, Locales target) {
		return format(get(unlocalizedString, getLocale(target)));
	}
	
	public String getLocalizedString(String unlocalizedString, String locale) {
		return getLocalizedString(unlocalizedString, Locales.getLocale(locale));
	}
	
	public String getLocalizedString(String unlocalizedString, Locales target, String... formatVars) {
		return formatVars(format(get(unlocalizedString, getLocale(target))), formatVars);
	}
	
	public String getLocalizedString(String unlocalizedString, String locale, String... formatVars) {
		return getLocalizedString(unlocalizedString, Locales.getLocale(locale), formatVars);
	}
	
	public String format(String str) {
		return Pattern.compile("[&]([0-9a-fk-or])").matcher(str).replaceAll(ChatColor.COLOR_CHAR + "$1");
	}
	
	public String formatVars(String str, String... vars) {
		for (int i = 0; i < vars.length; i++) {
			str = str.replace("$" + i, vars[i]);
		}
		return str;
	}
	
	public enum Locales {
		
		en_us(true, "en-US");
		
		private final boolean fallback;
		private final String def;
		
		private Locales(final String def) {
			fallback = false;
			this.def = def;
		}
		private Locales(final boolean fallback, final String def) {
			this.fallback = fallback;
			this.def = def;
		}
		
		public boolean isFallback() {
			return fallback;
		}
		
		public static Locales getLocale(String target) {
			try {
				return Locales.valueOf(target);
			} catch (Exception e) {
				return getFallback();
			}
		}
		
		public static Locales getFallback() {
			for (Locales l : Locales.values()) {
				if (l.fallback) {
					return l;
				}
			}
			return null;
		}
		
		public String getFileDef() {
			return def;
		}
	}

	@Override
	public String command_Description(String command) {
		return getLocalizedString("command." + command + ".desc");
	}
	
	@Override
	public String feedback_ExclusiveSender(String locale, String targetSenderType) {
		return getLocalizedString("command.default.exclusive", locale, targetSenderType);
	}

	@Override
	public String feedback_NoPermission(String locale) {
		return getLocalizedString("command.default.noperm", locale);
	}

	@Override
	public String feedback_InCooldown(String locale) {
		return getLocalizedString("command.default.incooldown", locale);
	}
}