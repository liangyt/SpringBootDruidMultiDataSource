package com.liangyt.config;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * 描述：使用 MysqlXA
 * 创建时间 2017-12-15 14:15
 * 作者 liangyongtong
 */
@SuppressWarnings("all")
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
