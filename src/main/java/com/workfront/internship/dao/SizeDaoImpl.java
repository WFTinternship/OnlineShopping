package com.workfront.internship.dao;

import java.util.List;
import org.apache.log4j.Logger;
import com.workfront.internship.common.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


/**
 * Created by Anna Asmangulyan on 03.09.2016.
 */
@Component
public class SizeDaoImpl extends GeneralDao implements SizeDao{

    private static final Logger LOGGER = Logger.getLogger(AddressDaoImpl.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public List<Size> getSizesByCategoryId(int categoryId) {
        List<Size> sizes = new ArrayList<Size>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = dataSource.getConnection();

            String sql = "SELECT * from sizes where category_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, categoryId);
            resultSet = preparedStatement.executeQuery();
            sizes = createListOfSizes(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);

        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return sizes;
    }
    private List<Size> createListOfSizes(ResultSet resultSet) throws SQLException {

        List<Size> sizes = new ArrayList<>();
        while (resultSet.next()) {
            Size size = new Size();
            //setting values from resultset...
            size.setCategoryId(resultSet.getInt("category_id")).
                    setSizeOption(resultSet.getString("size_option")).setSizeId(resultSet.getInt("id"));
            sizes.add(size);
        }
        return sizes;
    }


    public int insertSize(Size size){
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            //inserting a new user into a db...
            String sql = "INSERT into sizes(category_id, size_option)" +
                    " VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, size.getCategoryId());
            preparedStatement.setString(2, size.getSizeOption());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                size.setSizeId(lastId);
            }

        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            LOGGER.error("Duplicate entry!");
            throw new RuntimeException("Duplicate entry!");
        }catch (SQLException  e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;

    }

}
