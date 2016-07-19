package com.workfront.internship.dao;

/**
 * Created by Workfront on 7/1/2016.
 */


import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public class DataSource {
    private static DataSource datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException {
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername("root");
        ds.setPassword("annaasm88");
        ds.setUrl("jdbc:mysql://localhost/onlineshop");

        // the settings below are optional -- dbcp can work with defaults
        ds.setMinIdle(5);
        ds.setMaxIdle(20);
        ds.setMaxOpenPreparedStatements(180);

        ds.getConnection();
    }

    public static DataSource getInstance() {
        if (datasource == null) {
            try {
                datasource = new DataSource();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}