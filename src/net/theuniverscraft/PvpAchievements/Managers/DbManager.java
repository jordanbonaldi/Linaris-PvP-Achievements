package net.theuniverscraft.PvpAchievements.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.theuniverscraft.PvpAchievements.AchievementType;
import net.theuniverscraft.PvpAchievements.Utils.SqlUtils;

import org.bukkit.entity.Player;

import com.theuniverscraft.PointsManager.Managers.PointsManager;

public class DbManager {
	private Connection connection;
	
	private List<PlayerAchievement> m_achs = new LinkedList<PlayerAchievement>();
	
	private final String bddName = GameConfig.BDD_PREFIX+"achs";
	
	private static DbManager dbManager = null;
	public static DbManager getInstance() {
		if(dbManager == null) {
			dbManager = new DbManager();
		}
		return dbManager;
	}
	public static void saveInstance() {
		getInstance().save();
	}
	
	private DbManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+GameConfig.BDD_HOST+":"+
					GameConfig.BDD_PORT.toString()+
					"/"+GameConfig.BDD_NAME;
			String user = GameConfig.BDD_USER;
			String password = GameConfig.BDD_PASSWORD;
			
			connection = DriverManager.getConnection(url, user, password);
			Statement state = connection.createStatement();
			
			StringBuilder sqlBuilder = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(bddName)
					.append("` (`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,");
			
			for(AchievementType type : AchievementType.values()) {
				sqlBuilder.append("`");
				sqlBuilder.append(type.toString());
				sqlBuilder.append("` int(11) NOT NULL,");
			}
			
			sqlBuilder.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;");
						
            
			state.executeUpdate(sqlBuilder.toString());
			
			state.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public int getActions(Player player, AchievementType type) {
		restorePlayer(player);
		
		for(PlayerAchievement ach : m_achs) {
			if(ach.getPlayer().getName().equals(player.getName())) {
				return ach.getActions(type);
			}
		}
		return 0;
	}
	
	public void addAction(Player player, AchievementType type) { addAction(player, type, 1); }
	public void addAction(Player player, AchievementType type, int amount) {
		restorePlayer(player);
		
		for(PlayerAchievement ach : m_achs) {
			if(ach.getPlayer().getName().equals(player.getName())) {
				ach.addAch(type, amount);
				break;
			}
		}
	}
	
	public void restorePlayer(Player player) {
		for(PlayerAchievement ach : m_achs) {
			if(ach.getPlayer().getName().equals(player.getName())) {
				return;
			}
		}
		
		try {
			Statement state = connection.createStatement();
			
			PlayerAchievement pa = new PlayerAchievement(player);
			
			ResultSet result = state.executeQuery("SELECT * FROM " + bddName + " WHERE pseudo='"+ player.getName() +"'");
			if(result.next()) {
				for(AchievementType type : AchievementType.values()) {
					pa.setAch(type, result.getInt(type.toString()));
				}
			}
			
			m_achs.add(pa);
			
			state.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void savePlayer(Player player) {
		try {
			Statement state = connection.createStatement();
			
			for(int i = 0; i < m_achs.size(); i++) {
				PlayerAchievement ach = m_achs.get(i);
				if(ach.getPlayer().getName().equals(player.getName())) {
					HashMap<String, Object> values = new HashMap<String, Object>();
					values.put("pseudo", ach.getPlayer().getName());
					
					for(AchievementType type : AchievementType.values()) {
						values.put(type.toString(), ach.getActions(type));
					}
					
					String sql = SqlUtils.buildInsertQuery(bddName, values, "pseudo");
					
					state.executeUpdate(sql);
					m_achs.remove(i);
					break;
				}
			}
			
			state.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void save() {
		try {
			Statement state = connection.createStatement();
			
			for(PlayerAchievement ach : m_achs) {
				this.savePlayer(ach.getPlayer());
			}
			
			state.close();
			connection.close();
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public class PlayerAchievement {
		private Player m_player;
		private HashMap<AchievementType, Integer> m_actions = new HashMap<AchievementType, Integer>();
		
		private PlayerAchievement(Player player) {
			m_player = player;
			for(AchievementType type : AchievementType.values()) {
				m_actions.put(type, 0);
			}
		}
		
		public void setAch(AchievementType type, int actions) {
			m_actions.put(type, actions);
		}
		
		public void addAch(AchievementType type, int amount) {
			int now = 0;
			if(m_actions.containsKey(type)) {
				now = m_actions.get(type);
			}
			m_actions.put(type, now + amount);
			
			// Check de l'achievement conserné
			int lastLevel = type.getLevel(now);
			int newLevel = type.getLevel(now + amount);
			
			if(lastLevel != newLevel) {
				String ach = type.getName();
				if(type.hasLevels()) ach = ach + " " + newLevel;
				
				int points = type.getPoints(newLevel);
				m_player.sendMessage(Translation.getString("UNLOCK_ACH").replaceAll("<ach>", ach)
						.replaceAll("<points>", Integer.toString(points)));
				PointsManager.getInstance().addPoints(m_player, points);
			}
		}
		
		public int getActions(AchievementType type) {
			return m_actions.containsKey(type) ? m_actions.get(type) : 0;
		}
		
		public Player getPlayer() { return m_player; }
	}
}
