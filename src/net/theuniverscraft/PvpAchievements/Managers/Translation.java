package net.theuniverscraft.PvpAchievements.Managers;

import java.util.HashMap;

import org.bukkit.ChatColor;

public class Translation {	
	private HashMap<String, String> lang = new HashMap<String, String>();
	
	
	
	private static Translation instance = null;
	public static String getString(String key) {
		if(instance == null) instance = new Translation();
		return instance.getStringLocale(key);
	}
	
	private Translation() {
		lang.put("UNLOCK_ACH", "&6&k||| &aVous avez débloqué le succès &f: &b<ach> &avous gagnez &b<points> points &6&k|||");
	}
	
	public String getStringLocale(String key) {
		return ChatColor.translateAlternateColorCodes('&', lang.get(key));
	}
}
