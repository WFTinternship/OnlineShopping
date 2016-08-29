package com.workfront.internship.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Anna Asmangulyan on 8/29/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.workfront")
@Profile("test")
public class TestConfiguration {
    @Bean
    public DataSource getDataSource() throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        Properties props = new Properties();
        props.load(DevelopmentConfiguration.class.getClassLoader().getResourceAsStream("config.properties"));

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url_test"));

        return dataSource;
    }
}
