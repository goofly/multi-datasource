package com.goofly.multidb.prop;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring")
public class DataSourceMap {
	
	private Map<String,String> datasource;

	private String maindb = "single";
}