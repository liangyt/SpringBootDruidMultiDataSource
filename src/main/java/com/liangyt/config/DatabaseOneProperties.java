package com.liangyt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：数据源 one
 * 创建时间 2017-12-15 10:56
 * 作者 liangyongtong
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.one")
public class DatabaseOneProperties extends DatabaseProperties{

}
