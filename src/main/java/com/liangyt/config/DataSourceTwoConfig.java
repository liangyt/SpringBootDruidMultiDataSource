package com.liangyt.config;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 描述：数据源two
 * 创建时间 2017-12-14 10:34
 * 作者 liangyongtong
 */
@SuppressWarnings("all")
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
