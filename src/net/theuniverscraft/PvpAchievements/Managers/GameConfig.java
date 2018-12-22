package net.theuniverscraft.PvpAchievements.Managers;

import net.theuniverscraft.PvpAchievements.PvpAchievements;

import org.bukkit.configuration.file.FileConfiguration;

public class GameConfig {
	public static String BDD_HOST = "localhost";
	public static Integer BDD_PORT = 3306;
	public static String BDD_NAME = "login_mc";
	public static String BDD_USER = "root";
	public static String BDD_PASSWORD = "5p3p28wq";
	public static String BDD_PREFIX = "pvp_ach_";
	
	public static void initConfig(PvpAchievements plugin) {
		FileConfiguration config = plugin.getConfig();
		
		config.addDefault("mysql.host", BDD_HOST);
		config.addDefault("mysql.port", BDD_PORT);
		config.addDefault("mysql.name", BDD_NAME);
		config.addDefault("mysql.user", BDD_USER);
		config.addDefault("mysql.password", BDD_PASSWORD);
		config.addDefault("mysql.prefix", BDD_PREFIX);
		config.options().copyDefaults(true);
			
		plugin.saveConfig();
		
		BDD_HOST = config.getString("mysql.host");
		BDD_PORT = config.getInt("mysql.port");
		BDD_NAME = config.getString("mysql.name");
		BDD_USER = config.getString("mysql.user");
		BDD_PASSWORD = config.getString("mysql.password");
		BDD_PREFIX = config.getString("mysql.prefix");
	}
}
