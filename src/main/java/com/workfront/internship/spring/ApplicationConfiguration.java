/*
package com.workfront.internship.spring;

*/
/**
 * Created by Anna Asmangulyan on 8/23/2016.
 *//*



import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.dao.UserDao;
import com.workfront.internship.dao.UserDaoImpl;
import org.apache.commons.dbcp.BasicDataSource;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.workfront")
public class ApplicationConfiguration {

    @Bean
        public DataSource getDataSource() throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        Properties props = new Properties();
        props.load(ApplicationConfiguration.class.getClassLoader().getResourceAsStream("config.properties"));

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");

        dataSource.setUsername(props.getProperty("jdbc.username"));
        dataSource.setPassword(props.getProperty("jdbc.password"));
        dataSource.setUrl(props.getProperty("jdbc.url"));

        return dataSource;
    }

}

*/
