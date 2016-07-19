package com.workfront.internship.dao;


import com.workfront.internship.common.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BasketDaoImpl extends GeneralDao implements BasketDao {
    private static final Logger LOGGER = Logger.getLogger(BasketDao.class);
    private DataSource dataSource;

    public BasketDaoImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;

    }
    @Override
    public int insertBasket(Basket basket) {
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into baskets(total_price, user_id, basket_status) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, basket.getTotalPrice());
            preparedStatement.setInt(2, basket.getUserID());
            preparedStatement.setString(3, basket.getBasketStatus());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                basket.setBasketID(lastId);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw  new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }
    @Override
    public int insertBasket(Connection connection, Basket basket) {
        int lastId = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            String sql = "INSERT into baskets(total_price, user_id, basket_status) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, basket.getTotalPrice());
            preparedStatement.setInt(2, basket.getUserID());
            preparedStatement.setString(3, basket.getBasketStatus());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                basket.setBasketID(lastId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lastId;
    }
    @Override
    public Basket getCurrentBasket(int userId) {
        Basket basket = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = dataSource.getConnection();
            basket = new Basket();

            String sql = "SELECT * from baskets where user_id=? AND basket_status = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, "current");
            resultSet = preparedStatement.executeQuery();
            basket = createBasket(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }


        return basket;
    }
    @Override
    public Basket getBasket(int basketId) {
        Basket basket = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection =dataSource.getConnection();

            String sql = "SELECT * from baskets where basket_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, basketId);
            resultSet = preparedStatement.executeQuery();
            basket = createBasket(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement, connection);
        }


        return basket;
    }

    private Basket createBasket(ResultSet resultSet) throws IOException, SQLException {
        Basket basket = null;
        while (resultSet.next()) {
            OrderItemDao orderItemDao = new OrderItemDaoImpl(dataSource);
            List<OrderItem> orderItems = new ArrayList<OrderItem>();
            int basketId = resultSet.getInt("basket_id");
            int userId = resultSet.getInt("user_id");
            double totalPrice = resultSet.getDouble("total_price");
            String status = resultSet.getString("basket_status");
            orderItems = orderItemDao.getOrderItemByBasketID(basketId);
            basket = new Basket();
            basket = basket.setBasketID(basketId).setOrderItems(orderItems).setTotalPrice(totalPrice).setUserID(userId).setBasketStatus(status);
        }
        return basket;
    }

    @Override
    public void updateBasket(Basket basket) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "UPDATE baskets SET total_price = ?, user_id=? where basket_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, basket.getTotalPrice());
            preparedStatement.setInt(2, basket.getUserID());
            preparedStatement.setInt(3, basket.getBasketID());

            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void updateBasket(Connection connection, Basket basket) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "UPDATE baskets SET total_price = ?, user_id=?, basket_status = ? where basket_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, basket.getTotalPrice());
            preparedStatement.setInt(2, basket.getUserID());
            preparedStatement.setString(3, basket.getBasketStatus());
            preparedStatement.setInt(4, basket.getBasketID());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void deleteBasketByBasketID(int basketid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from baskets where basket_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, basketid);
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
    }
    @Override
    public void deleteBasketByUserId(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from baskets where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void deleteAllBaskets(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from baskets";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public List<Basket> getAllBaskets(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Basket> baskets = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM baskets";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            baskets = createBasketList(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException();
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return baskets;
    }
    private List<Basket> createBasketList(ResultSet resultSet) throws SQLException {
        List<Basket> baskets = new ArrayList<>();
        Basket basket = null;
        while (resultSet.next()) {
            basket = new Basket();
            basket.setBasketID(resultSet.getInt("basket_id")).
                    setTotalPrice(resultSet.getDouble("total_price")).
                    setUserID(resultSet.getInt("user_id")).
                    setBasketStatus(resultSet.getString("basket_status"));
                    baskets.add(basket);
        }

        return baskets;
    }


}

