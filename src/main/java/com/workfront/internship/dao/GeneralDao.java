package com.workfront.internship.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Workfront on 7/1/2016.
 */
public class GeneralDao {
    protected void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        if (resultSet != null)
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (preparedStatement != null)
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (connection != null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}

