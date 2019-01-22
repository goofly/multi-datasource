package com.goofly.multidb.core;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Order(2)
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger log = Logger.getLogger(DynamicRoutingDataSource.class);
    
    @Override
    protected Object determineCurrentLookupKey() {
    	log.debug("current DataSource :"+ DynamicDataSourceContextHolder.get());
        return DynamicDataSourceContextHolder.get();
    }
}
