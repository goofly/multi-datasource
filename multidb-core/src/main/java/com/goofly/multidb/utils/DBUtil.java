package com.goofly.multidb.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;

public class DBUtil {

	private static final String DEFAULT_SOURCE_NAME = "single";
	
	public static final Map<String, Properties> prop2DBMap(Map<String,String> map){
		Properties properties = null;
		Map<String, Properties> allMap = new HashMap<>();

		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String[] split = StringUtils.split(key, '.');
			String prefix = split[0];
			String suffix = split[split.length-1];

			// 单数据源
			if(prefix.equals(suffix) || split.length ==1){
				prefix = DEFAULT_SOURCE_NAME;
			}

			if (allMap.containsKey(prefix)) {
				allMap.get(prefix).put(suffix, entry.getValue());
				continue;
			}
			
			properties = new Properties();
			properties.put(suffix, entry.getValue());
			allMap.put(prefix, properties);
		}
		return allMap;
	}
}
