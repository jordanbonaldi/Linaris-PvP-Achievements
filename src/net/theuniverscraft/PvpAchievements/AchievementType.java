package net.theuniverscraft.PvpAchievements;

public enum AchievementType {
	 // LEVEL
	 SPEEDER("Coureur", new int[] { 1000, 5000, 10000, 50000, 100000 },  new int[] { 50,150,200,250,350 }, 0),
	 MINEUR("Mineur", new int[] { 100, 500, 1200, 3000, 10000 }, new int[] { 50,150,200,250,350 }, 1),
	 KILLER("Tueur", new int[] { 5, 50, 200, 1000, 2500 },  new int[] { 50,150,200,250,300 }, 2),
	 
	 // AU PIF
	 BUY_P4("Achetez votre première P4 dans le shop", 1, 100),
	 LAVA_BUCKET("Avoir son premier seau de lave", 1, 50),
	 BREAK_IRON("Miner 80 minerais de fer", 80, 100),
	 BREAK_OBSI("Miner 50 blocs d'obsidienne", 50, 120),
	 CRAFT_APPLE_CHEAT("Crafter votre première pomme cheat", 1, 100),
	 CRAFT_POTION("Faire votre première potion", 1, 100),
	 BREAK_LOG("Cassez votre première buches en bois", 1, 20),
	 GO_OTHER_WORLD("Aller pour la première fois dans le monde build ou ressource", 1, 50),
	 BREAK_QUARTZ("Miner 30 minerais de quartz", 30, 100),
	 LAUNCH_SPELL("Lancer votre premier sort", 1, 70),
	 SPEND_SHOP_POINTS("Dépenser plus de 1000 points dans le shop", 1000, 400),
	 USE_FURNACE("Utiliser pour la première fois un four", 1, 50),
	 USE_ANVIL("Utiliser une enclume", 1, 40),
	 BREAK_DIAMOND("Miner 100 minerai de diamant", 100, 300),
	 GO_THE_END("Aller pour la premiere fois dans l'end", 1, 60),
	 WATER_BUCKET("Avoir son premier seau d'eau", 1, 50),
	 PLACE_OBSI("Poser 130 blocs d'obsidienne", 130, 150),
	 KILL_ENNEMI("Tuer votre premier ennemi", 1, 30),
	 BUY_SWORD("Acheter votre premier épée en diamant dans le shop", 1, 80),
	 ENCHANTE_P4("Enchanter un plastron en diamant P4", 1, 80);
	
	
	private String m_name = "";
	private int[] m_actions;
	private int[] m_points;
	private int m_line;
	AchievementType(String name, int[] actions, int[] points, int line){
		m_name = name;
		m_actions = actions;
		m_points = points;
		m_line = line;
	}
	
	AchievementType(String name, int actions, int points){
		this(name, new int[] { actions }, new int[] { points }, -1);
	}
	
	public String getName() { return m_name; }
	
	public boolean hasLevels() {
		return m_actions.length > 1;
	}
	
	public int getNbLevel() {
		return m_actions.length;
	}
	
	public int getActionLevel(int level) {
		level--;
		if(level >= m_actions.length) return m_actions[m_actions.length - 1];
		else if(level < 0) return m_actions[0];
		return m_actions[level];
	}
	
	public int getPoints(int level) {
		level--;
		if(level >= m_points.length) return m_points[m_points.length - 1];
		else if(level < 0) return m_points[0];
		return m_points[level];
	}
	
	public int getLevel(int actions) {
		for(int i = m_actions.length - 1; i >= 0; i--) {
			if(actions >= m_actions[i]) return i + 1;
		}
		return 0;
	}
	
	public int getLine() {
		return m_line;
	}
}
