

   Spring多数据源实现的方式大概有2中，一种是新建多个`MapperScan`扫描不同包，另外一种则是通过继承`AbstractRoutingDataSource`实现动态路由。今天作者主要基于后者做的实现，且方式1的实现比较简单这里不做过多探讨。

## 实现方式

  **方式1的实现（核心代码）：**

```java
@Configuration
@MapperScan(basePackages = "com.goofly.test1", sqlSessionTemplateRef  = "test1SqlSessionTemplate")
public class DataSource1Config1 {

    @Bean(name = "dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
	// .....略

}

@Configuration
@MapperScan(basePackages = "com.goofly.test2", sqlSessionTemplateRef  = "test1SqlSessionTemplate")
public class DataSourceConfig2 {

    @Bean(name = "dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource.test2")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
	// .....略

}
```

**方式2的实现（核心代码）：**

```java
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger log = Logger.getLogger(DynamicRoutingDataSource.class);
    
    @Override
    protected Object determineCurrentLookupKey() {
        //从ThreadLocal中取值
        return DynamicDataSourceContextHolder.get();
    }
}
```

> ​    第1种方式虽然实现比较加单，劣势就是不同数据源的mapper文件不能在同一包名，就显得不太灵活了。所以为了更加灵活的作为一个组件的存在，作者采用的第二种方式实现。



## 设计思路

![](https://github.com/goofly/file/blob/master/SpringCloud-xli/%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90.png?raw=true)

1. 当请求经过被注解修饰的类后，此时会进入到切面逻辑中。
2. 切面逻辑会获取注解中设置的key值，然后将该值存入到`ThreadLocal`中
3. 执行完切面逻辑后，会执行`AbstractRoutingDataSource.determineCurrentLookupKey()`方法，然后从`ThreadLocal`中获取之前设置的key值，然后将该值返回。
4. 由于`AbstractRoutingDataSource`的`targetDataSources`是一个map,保存了数据源key和数据源的对应关系，所以能够顺利的找到该对应的数据源。

### 源码解读

`org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource`，如下：

```Java
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements InitializingBean {
	
	private Map<Object, Object> targetDataSources;
	private Object defaultTargetDataSource;
	private boolean lenientFallback = true;
	private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
	private Map<Object, DataSource> resolvedDataSources;
	private DataSource resolvedDefaultDataSource;
    
    	protected DataSource determineTargetDataSource() {
		Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = this.resolvedDataSources.get(lookupKey);
		if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
			dataSource = this.resolvedDefaultDataSource;
		}
		if (dataSource == null) {
			throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
		}
		return dataSource;
	}

	/**
	 * Determine the current lookup key. This will typically be
	 * implemented to check a thread-bound transaction context.
	 * <p>Allows for arbitrary keys. The returned key needs
	 * to match the stored lookup key type, as resolved by the
	 * {@link #resolveSpecifiedLookupKey} method.
	 */
	protected abstract Object determineCurrentLookupKey();
    
    //........略
```

`targetDataSources`是一个map结构，保存了key与数据源的对应关系；

`dataSourceLookup`是一个`DataSourceLookup`类型，默认实现是`JndiDataSourceLookup`。点开该类源码会发现，它实现了通过key获取DataSource的逻辑。当然，这里可以通过`setDataSourceLookup()`来改变其属性，因为关于此处有一个坑，后面会讲到。

```java
public class JndiDataSourceLookup extends JndiLocatorSupport implements DataSourceLookup {

	public JndiDataSourceLookup() {
		setResourceRef(true);
	}

	@Override
	public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
		try {
			return lookup(dataSourceName, DataSource.class);
		}
		catch (NamingException ex) {
			throw new DataSourceLookupFailureException(
					"Failed to look up JNDI DataSource with name '" + dataSourceName + "'", ex);
		}
	}

}
```



### 配置示例

**多数据源**

~~~java
### 多数据源
```
# db1
spring.datasource.master.url = jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.master.username = root
spring.datasource.master.password = 123456
spring.datasource.master.driverClassName = com.mysql.jdbc.Driver
spring.datasource.master.validationQuery = true
spring.datasource.master.testOnBorrow = true
## db2
spring.datasource.slave.url = jdbc:mysql://127.0.0.1:3306/test1?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.slave.username = root
spring.datasource.slave.password = 123456
spring.datasource.slave.driverClassName = com.mysql.jdbc.Driver
spring.datasource.slave.validationQuery = true
spring.datasource.slave.testOnBorrow = true

#主数据源名称
spring.maindb=master
#mapperper包路径
mapper.basePackages =com.btps.xli.multidb.demo.mapper
```
~~~

**单数据源**

为了让使用者能够用最小的改动实现最好的效果，作者对单数据源的多种配置做了兼容。

配置1：

```java
spring.datasource.master.url = jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.master.username = root
spring.datasource.master.password = 123456
spring.datasource.master.driverClassName = com.mysql.jdbc.Driver
spring.datasource.master.validationQuery = true
spring.datasource.master.testOnBorrow = true

# mapper包路径
mapper.basePackages = com.goofly.xli.multidb.demo.mapper
# 主数据源名称
spring.maindb=master
```

配置2

```java
spring.datasource.url = jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.username = root
spring.datasource.password = 123456
spring.datasource.driverClassName = com.mysql.jdbc.Driver
spring.datasource.validationQuery = true
spring.datasource.testOnBorrow = true

# mapper包路径
mapper.basePackages = com.goofly.xli.multidb.demo.mapper
```



## 踩坑之路

### 多数据源的循环依赖

```java
Description:

The dependencies of some of the beans in the application context form a cycle:

   happinessController (field private com.db.service.HappinessService com.db.controller.HappinessController.happinessService)
      ↓
   happinessServiceImpl (field private com.db.mapper.MasterDao com.db.service.HappinessServiceImpl.masterDao)
      ↓
   masterDao defined in file [E:\GitRepository\framework-gray\test-db\target\classes\com\db\mapper\MasterDao.class]
      ↓
   sqlSessionFactory defined in class path resource [com/goofly/xli/datasource/core/DynamicDataSourceConfiguration.class]
┌─────┐
|  dynamicDataSource defined in class path resource [com/goofly/xli/datasource/core/DynamicDataSourceConfiguration.class]
↑     ↓
|  firstDataSource defined in class path resource [com/goofly/xli/datasource/core/DynamicDataSourceConfiguration.class]
↑     ↓
|  dataSourceInitializer
```

**解决方案：**

在Spring boot启动的时候排除`DataSourceAutoConfiguration`即可。如下:

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DBMain {
	public static void main(String[] args) {
		SpringApplication.run(DBMain.class, args);
	}
}
```

> ​     但是作者在创建多数据源的时候由于并未创建多个`DataSource`的Bean，而是只创建了一个即需要做动态数据源的那个Bean。 其他的`DataSource`则直接创建实例然后存放在Map里面，然后再设置到`DynamicRoutingDataSource#setTargetDataSources`即可。
>
>    因此这种方式也不会出现循环依赖的问题！



### 动态刷新数据源

> ​     笔者在设计之初是想构建一个动态刷新数据源的方案，所以利用了`SpringCloud`的`@RefreshScope`去标注数据源，然后利用`RefreshScope#refresh`实现刷新。但是在实验的时候发现由Druid创建的数据源会因此而关闭，由Spring的`DataSourceBuilder`创建的数据源则不会发生任何变化。
>
> ​    最后关于此也没能找到解决方案。同时思考，如果只能的可以实现动态刷新的话，那么数据源的原有连接会因为刷新而中断吗还是会有其他处理？



### 多数据源事务

> ​    有这么一种特殊情况，一个事务中调用了两个不同数据源，这个时候动态切换数据源会因此而失效。
>
> 翻阅了很多文章，大概找了2中解决方案，一种是Atomikos进行事务管理，但是貌似性能并不是很理想。
>
>   另外一种则是通过优先级控制，切面的的优先级必须要大于数据源的优先级，用注解`@Order`控制。
>
> 此处留一个坑！