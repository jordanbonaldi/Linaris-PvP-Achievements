package net.theuniverscraft.PvpAchievements.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SqlUtils {
	public static String buildInsertQuery(String table, HashMap<String, Object> values, String... updateExclude) {
		List<String> exclude = new LinkedList<String>();
		if(updateExclude != null) {
			exclude = Arrays.asList(updateExclude);
		}
		
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("INSERT INTO "+ table +"(");
		
		int i = 0;
		Iterator<String> it = values.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			if(i > 0) sqlBuilder.append(", ");
			sqlBuilder.append(key);
			i++;
		}
		sqlBuilder.append(") VALUES(");
		
		i = 0;
		it = values.keySet().iterator();
		while(it.hasNext()) {
			Object value = values.get(it.next());
			
			if(i > 0) sqlBuilder.append(", ");
			sqlBuilder.append("'");
			sqlBuilder.append(value.toString());
			sqlBuilder.append("'");
			i++;
		}
		sqlBuilder.append(") ON DUPLICATE KEY UPDATE ");
		
		boolean useSeparator = false;
		it = values.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Object value = values.get(key);
			
			if(!exclude.contains(key)) {
				if(useSeparator) sqlBuilder.append(", ");
				sqlBuilder.append(key+"='"+ value.toString() +"'");
				useSeparator = true;
			}
		}
		
		sqlBuilder.append(";");
		return sqlBuilder.toString();
	}
}
