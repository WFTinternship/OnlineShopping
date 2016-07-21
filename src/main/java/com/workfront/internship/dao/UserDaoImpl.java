package com.workfront.internship.dao;

import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends GeneralDao implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDao.class);
    private DataSource dataSource;

    public UserDaoImpl(DataSource dataSource) throws SQLException, IOException {
        this.dataSource = dataSource;
    }
    @Override
    public int insertUser(User user) {
        int lastId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        BasketDao basketDao;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            basketDao = new BasketDaoImpl(dataSource);
            basketDao.insertBasket(connection, user.getBasket());

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
            connection.commit();
            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                user.setUserID(lastId);
            }

        } catch (SQLException  | IOException e) {
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
    public int insertUserWithShippingAddresses(User user){
        int lastId = 0;
        AddressDao addressDao;
        BasketDao basketDao;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            addressDao = new AddressDaoImpl(dataSource);
            for(int i = 0; i< user.getShippingAddresses().size(); i++)
            addressDao.insesrtAddress(connection, user.getShippingAddresses().get(i));

            basketDao = new BasketDaoImpl(dataSource);
            basketDao.insertBasket(connection, user.getBasket());

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
            connection.commit();
            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
                user.setUserID(lastId);
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
    public User getUserByID(int userid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = dataSource.getConnection();
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
    public User getUserByUsername(String uname) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = dataSource.getConnection();

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
    public void updateUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            String sql = "UPDATE users SET firstname = ?, lastname = ?, username = ?, password = ?, phone = ?, email = ?," +
                    "confirmation_status = ?, access_privilege = ? where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setBoolean(7, user.getConfirmationStatus());
            preparedStatement.setString(8, user.getAccessPrivilege());
            preparedStatement.setInt(9, user.getUserID());

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

        }  catch (SQLException e) {
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

            String sql = "SELECT * FROM wishlist where user_id =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            System.out.println("trying to get wishlist.....");
            resultSet = preparedStatement.executeQuery();
            System.out.println("already have wishlist.....");
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

            int uid = resultSet.getInt("user_id");
            String fname = resultSet.getString("firstname");
            String lname = resultSet.getString("lastname");
            String uname = resultSet.getString("username");
            String pass = resultSet.getString("password");
            String phone = resultSet.getString("phone");
            String email = resultSet.getString("email");
            boolean status = resultSet.getBoolean("confirmation_status");
            String access = resultSet.getString("access_privilege");
            BasketDao basketDao = new BasketDaoImpl(dataSource);
            SaleDao saleDao = new SaleDaoImpl(dataSource);
            AddressDao addressDao = new AddressDaoImpl(dataSource);
            user = new User();
            user.setUserID(uid).setFirstname(fname).setLastname(lname).setUsername(uname).setPassword(pass).setPhone(phone).setEmail(email).setBasket(basketDao.getCurrentBasket(uid)).setWishList(getWishlist(uid)).setSales(saleDao.getSales(uid)).setShippingAddresses(addressDao.getShippingAddressByUserID(uid)).setConfirmationStatus(status).setAccessPrivilege(access);
        }
        return user;
    }

    private List<Product> createWishlist(ResultSet resultSet) throws SQLException, IOException {
        List<Product> wishlist = new ArrayList<Product>();
        ProductDao productDao = new ProductDaoImpl(dataSource);
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
        BasketDao basketDao = new BasketDaoImpl(dataSource);
        SaleDao saleDao = new SaleDaoImpl(dataSource);
        AddressDao addressDao = new AddressDaoImpl(dataSource);
        while (resultSet.next()) {
            user = new User();
            userId = resultSet.getInt("user_id");
            user.setUserID(userId).setFirstname(resultSet.getString("firstname")).setLastname(resultSet.getString("lastname")).setUsername(resultSet.getString("username")).setPassword(resultSet.getString("password")).setPhone(resultSet.getString("phone")).setEmail(resultSet.getString("email")).setBasket(basketDao.getCurrentBasket(userId)).setShippingAddresses(addressDao.getShippingAddressByUserID(userId)).setSales(saleDao.getSales(userId)).setAccessPrivilege(resultSet.getString("access_privilege")).setConfirmationStatus(resultSet.getBoolean("confirmation_status"));
            users.add(user);

        }

        return users;
    }

}

