package com.workfront.internship.dao;

import com.workfront.internship.common.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SaleDaoImpl extends GeneralDao implements SaleDao {

    private static final Logger LOGGER = Logger.getLogger(SaleDao.class);
    @Autowired
    private DataSource dataSource;
    @Autowired
    private BasketDao basketDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CreditCardDao creditCardDao;
    /*public SaleDaoImpl(){

    }
    public SaleDaoImpl(LegacyDataSource dataSource) throws SQLException, IOException {
        this.dataSource = dataSource;
    }
*/
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
        Basket basket = new Basket();
        //BasketDao basketDao = new BasketDaoImpl(dataSource);
        while (resultSet.next()) {
           // basket = basketDao.getBasket(resultSet.getInt("basket_id"));
            basket.setBasketID(resultSet.getInt("basket_id"));
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
        Basket basket = new Basket();
        //BasketDao basketDao = new BasketDaoImpl(dataSource);
        while (resultSet.next()) {
           // basket = basketDao.getBasket(resultSet.getInt("basket_id"));
            basket.setBasketID(resultSet.getInt("basket_id"));
            sale = new Sale();
            sale = sale.setSaleID(resultSet.getInt("sale_id")).
                    setAddressID(resultSet.getInt("address_id")).
                    setBasket(basket).
                    setCreditCard(resultSet.getInt("card_id")).
                    setDate(resultSet.getTimestamp("date_of_purchuase")).
                    setUserID(resultSet.getInt("user_id"));
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
        Product product;

        int lastId = 0;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);







            basketDao.updateBasket(connection, sale.getBasket());

            List<OrderItem> orderItems = orderItemDao.getOrderItemByBasketID(sale.getBasket().getBasketID());
            for(int i = 0; i<orderItems.size(); i++) {

                product = orderItems.get(i).getProduct();
                product.setQuantity(product.getQuantity() - orderItems.get(i).getQuantity());
                productDao.updateProduct(connection, product);
            }

            CreditCard creditCard = creditCardDao.getCreditCardByCardID(sale.getCreditCardID());
            creditCard.setBalance(creditCard.getBalance() - sale.getBasket().getTotalPrice());
            creditCardDao.updateCreditCard(creditCard);


            String sql = "INSERT into sales(user_id, card_id, address_id, basket_id) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, sale.getUserID());
//            preparedStatement.setTimestamp(2, new Timestamp(sale.getDate().getTime()));
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

        } catch (SQLException  e) {
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
