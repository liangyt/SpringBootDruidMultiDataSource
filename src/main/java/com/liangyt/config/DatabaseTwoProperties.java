package com.liangyt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：数据源 two
 * 创建时间 2017-12-15 11:31
 * 作者 liangyongtong
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.two")
public class DatabaseTwoProperties extends DatabaseProperties{

}
