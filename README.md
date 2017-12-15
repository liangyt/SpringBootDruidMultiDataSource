##### 尝试多数据源处理

使用基于 <code>spring-boot-starter-thymeleaf</code> 和 <code>atomikos</code>实现的分布式事务处理.
  
两种实现：
* atomikos 和 MysqlXA  
* atomikos 和 DruidXA  

先添加所有的依赖：
```apple js
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>${mybatis.springboot.version}</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>${pagehelper.version}</version>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>${druid.version}</version>
</dependency>

<!--mybatis generator-->
<dependency>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>${mybatis.generator.version}</version>
</dependency>

<!--test-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>

<!--进行分布式事务管理-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jta-atomikos</artifactId>
</dependency>
```
因为是在一个项目实现的，所以把所有的依赖都贴出来了。  
>基本思路是，一个数据源对应着一类 Mapper,该Mapper下的相关操作（增删改查）使用同一个数据源，在Service下把各个Mapper的操作混合，
把这些混合操作添加事务，以达到操作的原子性。

在数据源 one 添加一个表：
```apple js
CREATE TABLE `ds_one` (
  `id` int(11) NOT NULL,
  `one` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```
在数据源 two 添加一个表：
```apple js
CREATE TABLE `ds_two` (
  `id` int(11) NOT NULL,
  `two` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```
注意数据源 two 的表id 字段是主键，主要是为了测试添加的时候事务是否正常。  

application.properties配置文件：
```apple js
# JDBC 配置
# 数据源 one
spring.datasource.one.url=jdbc:mysql://localhost/datasourceone
spring.datasource.one.username=root
spring.datasource.one.password=
spring.datasource.one.min-pool-size=5
spring.datasource.one.max-pool-size=50
spring.datasource.one.max-lifet-time=30000
spring.datasource.one.borrow-connection-timeout=30
spring.datasource.one.login-timeout=30
spring.datasource.one.maintenance-interval=60
spring.datasource.one.max-idle-time=60
spring.datasource.one.test-query=select 1
spring.datasource.one.type=com.alibaba.druid.pool.xa.DruidXADataSource
# 数据源 two
spring.datasource.two.url=jdbc:mysql://localhost/datasourcetwo
spring.datasource.two.username=root
spring.datasource.two.password=
spring.datasource.two.min-pool-size=5
spring.datasource.two.max-pool-size=50
spring.datasource.two.max-lifet-time=30000
spring.datasource.two.borrow-connection-timeout=30
spring.datasource.two.login-timeout=30
spring.datasource.two.maintenance-interval=60
spring.datasource.two.max-idle-time=60
spring.datasource.two.test-query=select 1
spring.datasource.two.type=com.alibaba.druid.pool.xa.DruidXADataSource
```
这个主要是两个数据源配置数据，druid 的配置如下:
```apple js
# StatViewServlet配置
# 开启页面访问展示  路径 /druid
spring.datasource.druid.stat-view-servlet.enabled=true
# 根据配置中的url-pattern来访问内置监控页面，如果是以下的配置，内置监控页面的首页是/druid/index.html
spring.datasource.druid.stat-view-servlet.url-pattern=/druid/*
# 允许清空数据
spring.datasource.druid.stat-view-servlet.reset-enable=true
# druid登录用户名
spring.datasource.druid.stat-view-servlet.login-username=druid
# druid 登录发密码
spring.datasource.druid.stat-view-servlet.login-password=druid
# 如果allow没有配置或者为空，则允许所有访问;deny优先于allow，如果在deny列表中，就算在allow列表中，也会被拒绝。
spring.datasource.druid.stat-view-servlet.allow=127.0.0.1
spring.datasource.druid.stat-view-servlet.deny=192.168.6.1

# StatFilter配置
# 是否启用StatFilter 默认开启
spring.datasource.druid.web-stat-filter.enabled=true
#spring.datasource.druid.web-stat-filter.url-pattern=
#spring.datasource.druid.web-stat-filter.profile-enable=
#spring.datasource.druid.web-stat-filter.principal-cookie-name=
#spring.datasource.druid.web-stat-filter.principal-session-name=
# 过滤访问,需要排除一些不必要的url
spring.datasource.druid.web-stat-filter.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*
# 是否关闭session统计功能
spring.datasource.druid.web-stat-filter.session-stat-enable=false
# session存储个数，缺省sessionStatMaxCount是1000个;
spring.datasource.druid.web-stat-filter.session-stat-max-count=1000
# 开启filter
spring.datasource.druid.filter.stat.enabled=true
# 合并SQL
spring.datasource.druid.filter.stat.merge-sql=true
# 多于这个时间的SQL执行时间为慢SQL
spring.datasource.druid.filter.stat.slow-sql-millis=6000
# 慢SQL记录
spring.datasource.druid.filter.stat.log-slow-sql=true
```
如果配置的是MysqlXA 则 druid 相关的配置则可以去掉。
相应的数据源 one 和 数据源 two 初始相关类为：
```apple js
public class DatabaseProperties {
    private String url;
    private String username;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;
    private int maxLifetime;
    private int borrowConnectionTimeout;
    private int loginTimeout;
    private int maintenanceInterval;
    private int maxIdleTime;
    private String testQuery;
    private String type;
    
    // ... setter 和 getter
}

@Component
@ConfigurationProperties(prefix = "spring.datasource.one")
public class DatabaseOneProperties extends DatabaseProperties{

}

@Component
@ConfigurationProperties(prefix = "spring.datasource.two")
public class DatabaseTwoProperties extends DatabaseProperties{

}
```
下面看看 MysqlXA 和 DruidXA 的数据源配置方式：
MysqlXA 方式:
```apple js
@Configuration
public class MysqlXA {
    @Primary
    @Bean(name = "dataSourceOne")
    public DataSource dataSourceOne(Environment environment, DatabaseOneProperties databaseOneProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(databaseOneProperties.getUrl());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setUser(databaseOneProperties.getUsername());
        mysqlXADataSource.setPassword(databaseOneProperties.getPassword());

        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("one");
        atomikosDataSourceBean.setMinPoolSize(databaseOneProperties.getMinPoolSize());
        atomikosDataSourceBean.setMaxPoolSize(databaseOneProperties.getMaxPoolSize());
        atomikosDataSourceBean.setBorrowConnectionTimeout(databaseOneProperties.getBorrowConnectionTimeout());
        atomikosDataSourceBean.setLoginTimeout(databaseOneProperties.getLoginTimeout());
        atomikosDataSourceBean.setMaintenanceInterval(databaseOneProperties.getMaintenanceInterval());
        atomikosDataSourceBean.setMaxIdleTime(databaseOneProperties.getMaxIdleTime());
        atomikosDataSourceBean.setTestQuery(databaseOneProperties.getTestQuery());

        return atomikosDataSourceBean;
    }

    @Bean(name = "dataSourceTwo")
    public DataSource dataSourceTwo(DatabaseTwoProperties databaseTwoProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        MysqlXADataSource mysqlXADataSource = new MysqlXADataSource();
        mysqlXADataSource.setUrl(databaseTwoProperties.getUrl());
        mysqlXADataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXADataSource.setUser(databaseTwoProperties.getUsername());
        mysqlXADataSource.setPassword(databaseTwoProperties.getPassword());

        atomikosDataSourceBean.setXaDataSource(mysqlXADataSource);
        atomikosDataSourceBean.setUniqueResourceName("two");
        atomikosDataSourceBean.setMinPoolSize(databaseTwoProperties.getMinPoolSize());
        atomikosDataSourceBean.setMaxPoolSize(databaseTwoProperties.getMaxPoolSize());
        atomikosDataSourceBean.setBorrowConnectionTimeout(databaseTwoProperties.getBorrowConnectionTimeout());
        atomikosDataSourceBean.setLoginTimeout(databaseTwoProperties.getLoginTimeout());
        atomikosDataSourceBean.setMaintenanceInterval(databaseTwoProperties.getMaintenanceInterval());
        atomikosDataSourceBean.setMaxIdleTime(databaseTwoProperties.getMaxIdleTime());
        atomikosDataSourceBean.setTestQuery(databaseTwoProperties.getTestQuery());

        return atomikosDataSourceBean;
    }
}
```
DruidXA 方式:
```apple js
//@Configuration
public class DruidXA {
    @Primary
    @Bean(name = "dataSourceOne")
    public DataSource dataSourceOne(Environment environment, DatabaseOneProperties databaseOneProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        atomikosDataSourceBean.setXaDataSourceClassName(databaseOneProperties.getType());
        Properties oneProperties = new Properties();

        oneProperties.put("url", databaseOneProperties.getUrl());
        oneProperties.put("username", databaseOneProperties.getUsername());
        oneProperties.put("password", databaseOneProperties.getPassword());

        atomikosDataSourceBean.setXaProperties(oneProperties);

        return atomikosDataSourceBean;
    }

    @Bean(name = "dataSourceTwo")
    public DataSource dataSourceTwo(DatabaseTwoProperties databaseTwoProperties) throws Exception {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();

        atomikosDataSourceBean.setXaDataSourceClassName(databaseTwoProperties.getType());
        Properties twoProperties = new Properties();

        twoProperties.put("url", databaseTwoProperties.getUrl());
        twoProperties.put("username", databaseTwoProperties.getUsername());
        twoProperties.put("password", databaseTwoProperties.getPassword());

        atomikosDataSourceBean.setXaProperties(twoProperties);

        return atomikosDataSourceBean;
    }
}
```
这两种只能同时开启其中一种，想用哪种把  <code>@Configuration</code>打开就可以了。
数据源配置好了之后就是 <code>SqlSessionFactory</code>和<code>SqlSessionTemplate</code>了:
```apple js
@Configuration
@MapperScan(basePackages = "com.liangyt.mapper.one", sqlSessionTemplateRef = "sqlSessionTemplateOne")
public class DataSourceOneConfig {
    @Primary
    @Bean("sqlSessionFactoryOne")
    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("dataSourceOne") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setTypeAliasesPackage("com.liangyt.entity.datasourceone");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/one/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean("sqlSessionTemplateOne")
    public SqlSessionTemplate sqlSessionTemplateOne(@Qualifier("sqlSessionFactoryOne") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```
```apple js
@Configuration
@MapperScan(basePackages = "com.liangyt.mapper.two", sqlSessionTemplateRef = "sqlSessionTemplateTwo")
public class DataSourceTwoConfig {
    @Bean("sqlSessionFactoryTwo")
    public SqlSessionFactory sqlSessionFactoryTwo(@Qualifier("dataSourceTwo") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setTypeAliasesPackage("com.liangyt.entity.datasourcetwo");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/two/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean("sqlSessionTemplateTwo")
    public SqlSessionTemplate sqlSessionTemplateTwo(@Qualifier("sqlSessionFactoryTwo") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
```
数据源相关的配置完成。

启动类：
```apple js
@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

一些实体类和测试服务直接看代码吧。