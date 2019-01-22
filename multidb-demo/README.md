#  多数据源使用

## 引包

```
		<dependency>
			<groupId>com.sygroup</groupId>
			<artifactId>xli-multidb-core</artifactId>
			<version>${parent.version}</version>
		</dependency>
```


## 配置示例

### 单数据源 -- 无数据源名称

```
# db1
#spring.datasource.url = jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false
#spring.datasource.username = root
#spring.datasource.password = 123456
#spring.datasource.driverClassName = com.mysql.jdbc.Driver
#spring.datasource.validationQuery = true
#spring.datasource.testOnBorrow = true

# mapper包路径
mapper.basePackages = com.btps.xli.multidb.demo.mapper
```

### 单数据源 --- 有数据源名称
```
spring.datasource.master.url = jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.master.username = root
spring.datasource.master.password = 123456
spring.datasource.master.driverClassName = com.mysql.jdbc.Driver
spring.datasource.master.validationQuery = true
spring.datasource.master.testOnBorrow = true

# mapper包路径
mapper.basePackages = com.btps.xli.multidb.demo.mapper
# 主数据源名称
spring.maindb=master
```

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

spring.maindb=master
#mapperper包路径
mapper.basePackages =com.btps.xli.multidb.demo.mapper
```


## 注解使用
> @DS("DataSourceName")作用于controller或者services或者Dao的类或者方法上面.
当作用于类时,代表整个类的方法都受用;作用于方法时,仅针对于该方法.

> PS:切勿将该注解作用于mapper类上,与此同时建议尽量做鱼用services类或者方法上

```
@Service
@DS("slave")
public class SlaveServiceImpl implements SlaveService{
    @Autowired
    private MasterDao happinessDao;

    public Happiness selectService(String city){
        return happinessDao.findByName(city);
    }

    @Transactional
    public void insertService(Happiness happiness){
    	happinessDao.insertHappiness(happiness.getId(), happiness.getUserName(), happiness.getPassword());
    }
}
```

## 重写默认配置
> 程序默认实现,但是使用者可根据需求重写 SqlSessionFactory 和 PlatformTransactionManager.