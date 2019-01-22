package com.goofly.multidb.core;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

import com.alibaba.druid.util.StringUtils;
import com.goofly.multidb.annotation.DS;
import com.goofly.multidb.prop.DataSourceMap;

@Aspect
@Order(1)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DynamicDataSourceAspect {

	@Autowired
	private DataSourceMap dataSourceMap;

	private static final Logger log = Logger.getLogger(DynamicDataSourceAspect.class);

	@Before(value = "@within(ds)")
	public void before(JoinPoint joinPoint, DS ds) throws Throwable {

		String dataSourceKey = ds.value();
		if (StringUtils.isEmpty(dataSourceKey)) {
			DynamicDataSourceContextHolder.set(dataSourceMap.getMaindb());
		} else {
			DynamicDataSourceContextHolder.set(dataSourceKey);
		}
	}

	@After("@within(com.goofly.multidb.annotation.DS)")
	public void doAfter(JoinPoint joinPoint) {
		log.debug("====>缓存清理");
		DynamicDataSourceContextHolder.clear();
	}

}
