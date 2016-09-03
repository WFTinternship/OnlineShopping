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



}
