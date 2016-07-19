package com.workfront.internship.dao;

import com.workfront.internship.common.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SaleDaoImpl extends GeneralDao implements SaleDao {

    private static final Logger LOGGER = Logger.getLogger(SaleDao.class);
    private DataSource dataSource;

    public SaleDaoImpl(DataSource dataSource) throws SQLException, IOException {
        this.dataSource = dataSource;
    }

    @Override
    public Sale getSaleBySaleID(int id){
        Sale sale = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = dataSource.getConnection();

            String sql = "SELECT * from sales where sale_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            sale = createSale(resultSet);

        }  catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return sale;

    }
    @Override
    public List<Sale> getSales(int userId) {
        List<Sale> sales = new ArrayList<Sale>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = dataSource.getConnection();

            String sql = "SELECT * from sales where user_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            sales = createListOfSales(resultSet);

        }  catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return sales;
    }
    private Sale createSale(ResultSet resultSet)throws SQLException, IOException{
        Sale sale = null;
        Basket basket = null;
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        while (resultSet.next()) {
            basket = basketDao.getBasket(resultSet.getInt("basket_id"));
            sale = new Sale();
            sale =  sale.setSaleID(resultSet.getInt("sale_id")).
                    setAddressID(resultSet.getInt("address_id")).
                    setBasket(basket).
                    setCreditCard(resultSet.getInt("card_id")).
                    setDate(resultSet.getTimestamp("date_of_purchuase")).
                    setUserID(resultSet.getInt("user_id"));
        }
        return sale;
    }
    private List<Sale> createListOfSales(ResultSet resultSet) throws SQLException, IOException{
        List<Sale> sales = new ArrayList<Sale>();
        Sale sale = null;
        Basket basket = null;
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        while (resultSet.next()) {
            int saleId = resultSet.getInt("sale_id");
            int userId = resultSet.getInt("user_id");
            java.util.Date date = resultSet.getTimestamp("date_of_purchuase");
            int cardId = resultSet.getInt("card_id");
            int addressId = resultSet.getInt("address_id");
            int basketId = resultSet.getInt("basket_id");
            basket = basketDao.getBasket(basketId);
            sale = new Sale();
            sale = sale.setSaleID(saleId).
                    setAddressID(addressId).
                    setBasket(basket).
                    setCreditCard(cardId).
                    setDate(date).
                    setUserID(userId);
            sales.add(sale);
        }
        return sales;
    }
    @Override
    public void deletSaleByUserID(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from sales where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void deleteSaleBySaleID(int saleid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from sales where sale_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, saleid);
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
    public int insertSale(Sale sale) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Basket basket = new Basket();
        int lastId = 0;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            BasketDao basketDao = new BasketDaoImpl(dataSource);
            basket.setUserID(sale.getUserID()).setTotalPrice(0.0).setBasketStatus("current");

            basketDao.insertBasket(connection, basket);

            basketDao.updateBasket(connection, sale.getBasket());

            String sql = "INSERT into sales(user_id, card_id, address_id, basket_id) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, sale.getUserID());
            preparedStatement.setTimestamp(2, new Timestamp(sale.getDate().getTime()));
            preparedStatement.setInt(2, sale.getCreditCardID());
            preparedStatement.setInt(3, sale.getAddressID());
            preparedStatement.setInt(4, sale.getBasket().getBasketID());
            preparedStatement.executeUpdate();
            connection.commit();
            resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                lastId = resultSet.getInt(1);
                sale.setSaleID(lastId);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }
    @Override
    public void deleteAllSales(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from sales";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
        }  finally {
            close(null, preparedStatement, connection);
        }

    }
    @Override
    public List<Sale> getAllSales(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Sale> sales = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM sales";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            sales = createListOfSales(resultSet);
        }catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return sales;
    }
    @Override
    public void updateSale(Sale sale){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dataSource.getConnection();

            String sql = "UPDATE sales SET user_id = ?, date_of_purchuase = ?, card_id = ?, address_id = ?," +
                    " basket_id = ? where sale_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, sale.getUserID());
            preparedStatement.setTimestamp(2, new Timestamp(sale.getDate().getTime()));
            preparedStatement.setInt(3, sale.getCreditCardID());
            preparedStatement.setInt(4, sale.getAddressID());
            preparedStatement.setInt(5, sale.getBasket().getBasketID());
            preparedStatement.setInt(6, sale.getSaleID());

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(null, preparedStatement, connection);
        }

    }
}
