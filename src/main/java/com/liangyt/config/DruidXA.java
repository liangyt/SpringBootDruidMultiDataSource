package com.liangyt.config;

import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 描述：使用 DruidXADataSource
 * 创建时间 2017-12-15 14:12
 * 作者 liangyongtong
 */
@SuppressWarnings("all")
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
