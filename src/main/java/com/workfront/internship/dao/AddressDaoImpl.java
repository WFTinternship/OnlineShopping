package com.workfront.internship.dao;
import org.apache.log4j.Logger;
import com.workfront.internship.common.Address;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDaoImpl extends GeneralDao implements AddressDao {

    private static final Logger LOGGER = Logger.getLogger(AddressDaoImpl.class);
    private DataSource dataSource;

    public AddressDaoImpl(DataSource dataSource) throws IOException, SQLException {
        this.dataSource = dataSource;

    }

    @Override
    public List<Address> getShippingAddressByUserID(int userid) {
        List<Address> addresses = new ArrayList<Address>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            connection = dataSource.getConnection();

            String sql = "SELECT * from addresses where user_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            resultSet = preparedStatement.executeQuery();
            addresses = createListOfAddresses(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);

        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return addresses;
    }
    public Address getAddressByID(int id){
        Address basket = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection =dataSource.getConnection();

            String sql = "SELECT * from addresses where address_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            basket = createAddress(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }


        return basket;
    }
    private List<Address> createListOfAddresses(ResultSet resultSet) throws SQLException {
        List<Address> addresses = new ArrayList<Address>();
        while (resultSet.next()) {
            int addressId = resultSet.getInt("address_id");
            String shippingAddress = resultSet.getString("shipping_address");
            int userId = resultSet.getInt("user_id");
            Address address = new Address();
            address.setAddressID(addressId).setAddress(shippingAddress).setUserID(userId);
            addresses.add(address);
        }
        return addresses;
    }
    private Address createAddress(ResultSet resultSet) throws  SQLException{
        Address address = null;
        while (resultSet.next()) {
            address = new Address();
            address.setAddressID(resultSet.getInt("address_id")).
                    setAddress(resultSet.getString("shipping_address")).
                    setUserID(resultSet.getInt("user_id"));
        }
        return address;
    }
    @Override
    public void deleteAddressesByUserID(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from addresses where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            preparedStatement.executeUpdate();


        } catch ( SQLException e) {

            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    @Override
    public void  deleteAddressesByAddressID(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from addresses where address_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }finally {
            close(resultSet, preparedStatement, connection);
        }
    }

    @Override
    public int insertAddress(Address address){
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into addresses(shipping_address, user_id) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, address.getAddress());
            preparedStatement.setInt(2, address.getUserID());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            while(resultSet.next()){
                lastId = resultSet.getInt(1);
                address.setAddressID(lastId);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return  lastId;
    }
    @Override
    public void updateAddress(Address address) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "UPDATE addresses SET shipping_address = ?, user_id=? where address_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, address.getAddress());
            preparedStatement.setInt(2, address.getUserID());
            preparedStatement.setInt(3, address.getAddressID());

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
    public void deleteAllAddresses(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "DELETE from addresses";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public List<Address> getAllAddresses(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Address> addresses = new ArrayList<>();
        try {
            connection = dataSource.getConnection();
            String sql = "SELECT * FROM baskets";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            addresses = createListOfAddresses(resultSet);
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return addresses;
    }
}
