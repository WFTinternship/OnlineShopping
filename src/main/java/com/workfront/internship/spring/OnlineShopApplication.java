package com.workfront.internship.spring;

import com.workfront.internship.dao.LegacyDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * Created by Anna Asmangulyan on 8/18/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.workfront")
public class OnlineShopApplication {
    public static final String SPRING_CONTEXT_KEY = "springContextKey";

    public static void init(ServletContext servletContext) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(OnlineShopApplication.class);
        servletContext.setAttribute(SPRING_CONTEXT_KEY, applicationContext);
    }

    public static ApplicationContext getApplicationContext(ServletContext servletContext) {
        return (ApplicationContext)servletContext.getAttribute(SPRING_CONTEXT_KEY);
    }

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
