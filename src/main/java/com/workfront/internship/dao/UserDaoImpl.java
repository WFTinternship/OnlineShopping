package com.workfront.internship.dao;

import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDaoImpl extends GeneralDao implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private BasketDao basketDao;
    @Autowired
    private SaleDao saleDao;

    @Override
    public int insertUser(User user){
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            //inserting a new user into a db...
            String sql = "INSERT into users(firstname, lastname, username, password, phone, email, confirmation_status, access_privilege)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, preparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setBoolean(7, user.getConfirmationStatus());
            preparedStatement.setString(8, user.getAccessPrivilege());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                user.setUserID(lastId);
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

    @Override
    public User getUserByID(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
            //getting a user by userId...
            String sql = "SELECT * FROM users where user_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);

            resultSet = preparedStatement.executeQuery();
            user = createUser(resultSet);

        }catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return user;
    }
    @Override
    public boolean isInWishList(int userID, int productID){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean result = false;
        try {
            connection = dataSource.getConnection();
            //checks if a given product is in the wishlist of a given user...
            String sql = "SELECT * FROM wishlist where user_id =? and product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, productID);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
                result = true;
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
       return result;
    }
    @Override
    public User getUserByUsername(String uname) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
            //getting a user from db given a username...
            String sql = "SELECT * FROM users where username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uname);

            resultSet = preparedStatement.executeQuery();
            user = createUser(resultSet);

        }  catch (SQLException |IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return user;
    }
    @Override
    public List<User> getAllUsers(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<User>();
        try {
            connection = dataSource.getConnection();
            //getting all users from db...
            String sql = "SELECT * FROM users";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            users = createUserList(resultSet);

        }catch (SQLException |IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
        return users;
    }
    @Override
    public void updateUserStatus(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            //updates user status after he/she gets a confirmation email...
            String sql = "UPDATE users SET confirmation_status = ? where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, id);

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
    public void updateUser(User user){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
           // connection.setAutoCommit(false);

           /* AddressDao addressDao = new AddressDaoImpl(dataSource);
            List<Address> oldAddresses = addressDao.getShippingAddressByUserID(user.getUserID());
            List<Address> newAddresses = user.getShippingAddresses();
            for(int i = 0; i < newAddresses.size(); i ++)
                if(!oldAddresses.contains(newAddresses.get(i)))
                    addressDao.insertAddress(connection, newAddresses.get(i));
            for(int i = 0; i < oldAddresses.size(); i ++)
                if(!newAddresses.contains(oldAddresses.get(i)))
                    addressDao.deleteAddressesByAddressID(connection, oldAddresses.get(i).getAddressID());
*/
            String sql = "UPDATE users SET firstname = ?, lastname = ?, username = ?, password = ?, phone = ?, email = ? where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setInt(7, user.getUserID());

            preparedStatement.executeUpdate();
           // connection.commit();
        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            LOGGER.error("Duplicate entry!");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
           /* try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                LOGGER.error("SQL exception occurred!");
            }*/
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }

    }
    @Override
    public void deleteUser(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from users where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
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
    public void deleteAllUsers(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from users";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

        }  catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }  finally {
            close(resultSet, preparedStatement, connection);
        }
    }
    @Override
    public void deleteWishlistByUserID(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from wishlist where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
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
    public void deleteFromWishlistByUserIDAndProductID(int userID, int productID){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "DELETE from wishlist where user_id = ? and product_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, productID);

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
    public void insertIntoWishlist(int userId, int productId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();

            String sql = "INSERT into wishlist VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();

        }catch(SQLIntegrityConstraintViolationException e){
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        }catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
    }
    @Override
    public List<Product> getWishlist(int userid) {
        List<Product> products = new ArrayList<Product>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            //getting the wishlist of a given user...
            String sql = "SELECT * FROM wishlist where user_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);

            resultSet = preparedStatement.executeQuery();
            products = createWishlist(resultSet);

        }  catch (SQLException | IOException e) {
            e.printStackTrace();
            LOGGER.error("SQL exception occurred!");
            throw new RuntimeException(e);
        } finally {
            close(resultSet, preparedStatement, connection);
        }
        return products;


    }

    private User createUser(ResultSet resultSet) throws SQLException, IOException {
        User user = null;
        while (resultSet.next()) {
            user = new User();
            //Retrieve by column name and set to user...
            user.setUserID(resultSet.getInt("user_id")).
                    setFirstname(resultSet.getString("firstname")).
                    setLastname(resultSet.getString("lastname")).
                    setUsername(resultSet.getString("username")).
                    setPassword(resultSet.getString("password")).
                    setPhone(resultSet.getString("phone")).
                    setEmail(resultSet.getString("email")).

                    setShippingAddresses(addressDao.getShippingAddressByUserID(resultSet.getInt("user_id"))).
                    setConfirmationStatus(resultSet.getBoolean("confirmation_status")).
                    setAccessPrivilege(resultSet.getString("access_privilege"));
        }
        return user;
    }

    private List<Product> createWishlist(ResultSet resultSet) throws SQLException, IOException {
        List<Product> wishlist = new ArrayList<Product>();
        while (resultSet.next()) {
            //Retrieve by column name
            int uid = resultSet.getInt("user_id");
            int pid = resultSet.getInt("product_id");
            wishlist.add(productDao.getProductByID(pid));
        }
        return wishlist;
    }
    private List<User> createUserList(ResultSet resultSet) throws SQLException, IOException{
        List<User> users = new ArrayList<User>();
        User user = null;
        int userId = 0;
        while (resultSet.next()) {
            user = new User();
            userId = resultSet.getInt("user_id");
            user.setUserID(userId).setFirstname(resultSet.getString("firstname")).setLastname(resultSet.getString("lastname")).setUsername(resultSet.getString("username")).setPassword(resultSet.getString("password")).setPhone(resultSet.getString("phone")).setEmail(resultSet.getString("email")).setBasket(basketDao.getCurrentBasket(userId)).setShippingAddresses(addressDao.getShippingAddressByUserID(userId)).setRecords(saleDao.getSales(userId)).setAccessPrivilege(resultSet.getString("access_privilege")).setConfirmationStatus(resultSet.getBoolean("confirmation_status"));
            users.add(user);
        }
        return users;
    }

}

