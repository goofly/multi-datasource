package com.goofly.multidb.core;

import java.util.*;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.goofly.multidb.prop.DataSourceMap;
import com.goofly.multidb.utils.DBUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

@Configuration
@MapperScan(basePackages = "#{mapper.basePackages}")
@EnableConfigurationProperties(DataSourceMap.class)
public class DynamicDataSourceConfiguration {

	@Autowired
	private DataSourceMap dataSourceMap;

	private Map<Object, Object> initDataSource() throws Exception {
		Map<Object, Object> dbMap = new HashMap<>();

		Map<String, Properties> prop2dbMap = DBUtil.prop2DBMap(dataSourceMap.getDatasource());
		Set<Entry<String, Properties>> entrySet = prop2dbMap.entrySet();
		for (Entry<String, Properties> entry : entrySet) {
			DruidDataSource db = (DruidDataSource) DruidDataSourceFactory.createDataSource(entry.getValue());
			dbMap.put(entry.getKey(), db);
		}
		return dbMap;
	}

	@Bean
	public DataSource dynamicDataSource() throws Exception {

		MapDataSourceLookup dataSourceLookup = new MapDataSourceLookup();
		Map<Object, Object> initDataSourceMap = initDataSource();
		Set<Entry<Object, Object>> entries = initDataSourceMap.entrySet();
		for (Entry<Object, Object> entry : entries){
			dataSourceLookup.addDataSource((String) entry.getKey(),(DataSource)entry.getValue());
		}

		DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
		dataSource.setDataSourceLookup(dataSourceLookup);
		dataSource.setDefaultTargetDataSource(dataSourceMap.getMaindb());
		dataSource.setTargetDataSources(initDataSourceMap);
		return dataSource;
	}

	@Bean
	@ConditionalOnMissingBean(SqlSessionFactory.class)
	public SqlSessionFactory sqlSessionFactory(@Autowired DataSource dynamicDataSource)
			throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dynamicDataSource);
		// 此处设置为了解决找不到mapper文件的问题
		// sqlSessionFactoryBean.setMapperLocations(newPathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	@ConditionalOnMissingBean(PlatformTransactionManager.class)
	public PlatformTransactionManager platformTransactionManager(
			@Autowired DataSource dynamicDataSource) {
		return new DataSourceTransactionManager(dynamicDataSource);
	}
	
	@Bean
	DynamicDataSourceAspect dynamicDataSourceAspect() {
		return new DynamicDataSourceAspect();
	}
}
