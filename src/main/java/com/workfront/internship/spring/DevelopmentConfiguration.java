package com.workfront.internship.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

/**
 * Created by Anna Asmangulyan on 8/29/2016.
 */
@Configuration
@ComponentScan(basePackages = "com.workfront")
@Profile("developer")
public class DevelopmentConfiguration {
    @Bean
    public DataSource getDataSource() throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        Properties props = new Properties();
        props.load(DevelopmentConfiguration.class.getClassLoader().getResourceAsStream("config.properties"));

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url"));

        return dataSource;
    }
    @Bean(name = "multipartResolver")
        public CommonsMultipartResolver multipartResolver() {
                CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
                multipartResolver.setMaxUploadSize(100000);
                return new CommonsMultipartResolver();
            }
    /*@Bean
    public DataSource dataSource() throws IOException {
        return new EmbeddedDatabaseBuilder()
                .setType(H2)
                .addScript("schema.sql")
                .build();
    }*/
}
