package com.workfront.internship.dao;


import com.workfront.internship.common.CreditCard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreditCardDaoImpl extends GeneralDao implements CreditCardDao {

    private static final Logger LOGGER = Logger.getLogger(CreditCard.class);
    @Autowired
    private DataSource dataSource;

    @Override
    public CreditCard getCreditCardByCardID(int cardId) {
        CreditCard creditCard = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            creditCard = new CreditCard();

            String sql = "SELECT * from creditcards where card_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cardId);

            resultSet = preparedStatement.executeQuery();
            creditCard = createCreditCard(resultSet);

        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }finally {
            close(resultSet, preparedStatement, connection);
        }
        return creditCard;
    }

    @Override
    public CreditCard getCreditCardByCardNumber(String cartNumber){
        CreditCard creditCard = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            creditCard = new CreditCard();

            String sql = "SELECT * from creditcards where card_number =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, cartNumber);

            resultSet = preparedStatement.executeQuery();
            creditCard = createCreditCard(resultSet);

        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }finally {
            close(resultSet, preparedStatement, connection);
        }
        return creditCard;
    }


    private CreditCard createCreditCard(ResultSet resultSet) throws SQLException {
        CreditCard creditCard = null;
        while (resultSet.next()) {
            creditCard = new CreditCard();

            creditCard.setCardID(resultSet.getInt("card_id")).
                    setBillingAddress(resultSet.getString("billing_address")).
                    setBalance(resultSet.getDouble("balance")).
            setCardNumber(resultSet.getString("card_number")).setCvc(resultSet.getInt("cvc"));

        }
        return creditCard;
    }

    @Override
    public int insertCreditCard(CreditCard creditCard) {
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into creditcards(billing_address, balance, card_number, cvc) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, creditCard.getBillingAddress());
            preparedStatement.setDouble(2, creditCard.getBalance());
            preparedStatement.setString(3, creditCard.getCardNumber());
            preparedStatement.setInt(4, creditCard.getCvc());
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                creditCard.setCardID(lastId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return lastId;
    }

    @Override
    public void updateCreditCard(CreditCard creditCard) {
        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            updateCreditCard(connection, creditCard);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("can not get a connection!");
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void updateCreditCard(Connection connection, CreditCard creditCard) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            String sql = "UPDATE creditcards SET balance = ?, billing_address = ? where card_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDouble(1, creditCard.getBalance());
            preparedStatement.setString(2, creditCard.getBillingAddress());
            preparedStatement.setInt(3, creditCard.getCardID());

            preparedStatement.executeUpdate();


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

    }

    @Override
    public void deleteCreditCard(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from creditcards where card_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
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
    public void deleteAllCreditCards() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from creditcards";
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
    public List<CreditCard> getAllCreditCards() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CreditCard> creditCards = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM creditcards";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            creditCards = createCreditCardList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return creditCards;
    }

    private List<CreditCard> createCreditCardList(ResultSet resultSet) throws SQLException {
        List<CreditCard> creditCards = new ArrayList<>();
        CreditCard creditCard = null;
        while (resultSet.next()) {
            creditCard = new CreditCard();
            creditCard.setCardID(resultSet.getInt("card_id")).
                    setBalance(resultSet.getDouble("balance")).
                    setBillingAddress(resultSet.getString("billing_address"));
            creditCards.add(creditCard);
        }

        return creditCards;
    }
}
