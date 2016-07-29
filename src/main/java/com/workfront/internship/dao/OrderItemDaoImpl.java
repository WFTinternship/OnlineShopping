package com.workfront.internship.dao;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.workfront.internship.common.Basket;
import com.workfront.internship.common.OrderItem;
import com.workfront.internship.common.Product;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 04.07.2016.
 */
public class OrderItemDaoImpl extends GeneralDao implements OrderItemDao {

    private static final Logger LOGGER = Logger.getLogger(OrderItemDao.class);
    private DataSource dataSource;

    public OrderItemDaoImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;
    }

    @Override
    public List<OrderItem> getOrderItemByBasketID(int basketid) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "SELECT * from orderitems where basket_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, basketid);
            resultSet = preparedStatement.executeQuery();
            orderItems = createListOfOrderItems(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return orderItems;
    }

    @Override
    public OrderItem getOrderItemByItemID(int id) {
        OrderItem orderItem = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            orderItem = new OrderItem();
            String sql = "SELECT * from orderitems where orderitem_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            orderItem = createOrderItem(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return orderItem;

    }
    @Override
    public OrderItem getOrderItemByProductID(int productid) {
        OrderItem orderItem = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            orderItem = new OrderItem();
            String sql = "SELECT * from orderitems where oproduct_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productid);
            resultSet = preparedStatement.executeQuery();
            orderItem = createOrderItem(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return orderItem;

    }
    @Override
    public OrderItem getOrderItemByProductAndBasketID(int productid, int basketId) {
        OrderItem orderItem = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            orderItem = new OrderItem();
            String sql = "SELECT * from orderitems where oproduct_id =? and basket_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productid);
            preparedStatement.setInt(2, basketId);
            resultSet = preparedStatement.executeQuery();
            orderItem = createOrderItem(resultSet);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return orderItem;
    }
    private List<OrderItem> createListOfOrderItems(ResultSet resultSet) throws SQLException, IOException {
        Product product = null;
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        while (resultSet.next()) {

            ProductDao productDao = new ProductDaoImpl(dataSource);
            product = productDao.getProductByID(resultSet.getInt("product_id"));

            OrderItem orderItem = new OrderItem();
            orderItem = orderItem.setOrderItemID(resultSet.getInt("orderitem_id")).
                    setProduct(product).
                    setQuantity(resultSet.getInt("quantity")).
                    setBasketID(resultSet.getInt("basket_id"));
            orderItems.add(orderItem);

        }
        return orderItems;
    }

    private OrderItem createOrderItem(ResultSet resultSet) throws SQLException, IOException {
        Product product;
        OrderItem orderItem = null;
        while (resultSet.next()) {
            ProductDao productDao = new ProductDaoImpl(dataSource);
            product = productDao.getProductByID(resultSet.getInt("product_id"));
            orderItem = new OrderItem();
            orderItem = orderItem.setOrderItemID(resultSet.getInt("orderitem_id")).
                    setProduct(product).setQuantity(resultSet.getInt("quantity")).
                    setBasketID(resultSet.getInt("basket_id")).setProduct(product);
        }
        return orderItem;
    }

    @Override
    public void deleteOrderItemByItemID(int itemid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from orderitems where orderitem_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, itemid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }
/*    @Override
    public void deleteOrderItemByProducttIDAndByBasketID(int productId, int basketId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from orderitems where product_id = ? and basket_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, basketId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }*/

    @Override
    public void deleteOrderItemByProductID(int productid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from orderitems where product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            OrderItem oldOrderItem = getOrderItemByItemID(orderItem.getOrderItemID());

            BasketDao basketDao = new BasketDaoImpl(dataSource);
            int basketId = orderItem.getBasketID();

            Basket basket = basketDao.getBasket(basketId);

            basket.setTotalPrice(basket.getTotalPrice() + orderItem.getProduct().getPrice()*(orderItem.getQuantity()-oldOrderItem.getQuantity()));
            basketDao.updateBasket(connection, basket);

            String sql = "UPDATE orderitems SET quantity = ?, basket_id = ?, product_id = ? where orderitem_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderItem.getQuantity());
            preparedStatement.setInt(2, orderItem.getBasketID());
            preparedStatement.setInt(3, orderItem.getProduct().getProductID());
            preparedStatement.setInt(4, orderItem.getOrderItemID());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
                LOGGER.error("SQL exception occurred!");

            }
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }


    @Override
    public int insertOrderItem(OrderItem orderItem) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int lastId = 0;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            BasketDao basketDao = new BasketDaoImpl(dataSource);
            int basketId = orderItem.getBasketID();
            Basket basket = basketDao.getBasket(basketId);
            basket.setTotalPrice(basket.getTotalPrice() + orderItem.getProduct().getPrice()*orderItem.getQuantity()
                    + orderItem.getProduct().getShippingPrice());
            basketDao.updateBasket(connection, basket);


          //  product.setQuantity(product.getQuantity() - orderItem.getQuantity());

          //  ProductDao productDao = new ProductDaoImpl(dataSource);
          //  productDao.updateProduct(connection, product);

            String sql = "INSERT into orderitems(basket_id, product_id, quantity) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderItem.getBasketID());
            preparedStatement.setInt(2, orderItem.getProduct().getProductID());
            preparedStatement.setInt(3, orderItem.getQuantity());
            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet resultSet1 = preparedStatement.getGeneratedKeys();
            while (resultSet1.next()) {
                lastId = resultSet1.getInt(1);
                orderItem.setOrderItemID(lastId);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");

            try {
                connection.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
                LOGGER.error("SQL exception occurred!");

            }
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }

    @Override
    public void deleteAllOrderItems() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from orderitems";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM orderitems";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            orderItems = createListOfOrderItems(resultSet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return orderItems;
    }

}

