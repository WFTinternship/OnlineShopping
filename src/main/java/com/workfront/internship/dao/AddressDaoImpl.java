package com.workfront.internship.dao;
import org.apache.log4j.Logger;
import com.workfront.internship.common.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddressDaoImpl extends GeneralDao implements AddressDao {

    private static final Logger LOGGER = Logger.getLogger(AddressDaoImpl.class);

    @Autowired
    private DataSource dataSource;

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
        Address address = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection =dataSource.getConnection();

            String sql = "SELECT * from addresses where address_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            address = createAddress(resultSet);

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }


        return address;
    }
    private List<Address> createListOfAddresses(ResultSet resultSet) throws SQLException {
        List<Address> addresses = new ArrayList<Address>();
        while (resultSet.next()) {
            Address address = new Address();
            //setting values from resultset...
            address.setAddressID(resultSet.getInt("address_id")).
                    setAddress(resultSet.getString("shipping_address")).
                    setUserID(resultSet.getInt("user_id")).
                    setCity(resultSet.getString("city")).
                    setCountry(resultSet.getString("country")).
                    setZipCode(resultSet.getInt("zip_code"));
            addresses.add(address);
        }
        return addresses;
    }
    private Address createAddress(ResultSet resultSet) throws  SQLException{
        Address address = null;
        while (resultSet.next()) {
            address = new Address();
            //setting values from resultset...
            address.setAddressID(resultSet.getInt("address_id")).
                    setAddress(resultSet.getString("shipping_address")).
                    setUserID(resultSet.getInt("user_id")).
                    setCity(resultSet.getString("city")).
                    setCountry(resultSet.getString("country")).
                    setZipCode(resultSet.getInt("zip_code"));
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
            //inserting into db...
            String sql = "INSERT into addresses(shipping_address, user_id, city, country, zip_code) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, address.getAddress());
            preparedStatement.setInt(2, address.getUserID());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setString(4, address.getCountry());
            preparedStatement.setInt(5, address.getZipCode());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            while(resultSet.next()){
                lastId = resultSet.getInt(1);
                address.setAddressID(lastId);
            }
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            LOGGER.error("Duplicate entry!");
            throw new RuntimeException("Duplicate entry!");
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
            //updateing address in stored in db...
            String sql = "UPDATE addresses SET shipping_address = ?, user_id=?, city=?, country=?, zip_code=? where address_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, address.getAddress());
            preparedStatement.setInt(2, address.getUserID());
            preparedStatement.setInt(3, address.getAddressID());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setString(4, address.getCountry());
            preparedStatement.setInt(5, address.getZipCode());

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
            //deleteing all addresses from db...
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
            //getting all addresses from db...
            String sql = "SELECT * FROM addresses";
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
