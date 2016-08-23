package com.workfront.internship.spring;

/**
 * Created by Anna Asmangulyan on 8/23/2016.
 */

import org.apache.commons.dbcp.BasicDataSource;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;

    import javax.sql.DataSource;

@Configuration
public class SpringApplication {

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("annaasm88");
        dataSource.setUrl("jdbc:mysql://localhost/onlineshop");
        // the settings below are optional -- dbcp can work with defaults
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(20);
        dataSource.setMaxOpenPreparedStatements(180);
        return dataSource;
    }
}

