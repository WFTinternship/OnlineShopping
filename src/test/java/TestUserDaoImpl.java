import com.workfront.internship.common.Address;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;


public class TestUserDaoImpl{
    DataSource dataSource;
    User user = null;
    Product product = null;
    Category category = null;
    int lastInsertedIndex = 0;
    int lastIsertedProductIndex = 0;
    UserDao userDao;
    ProductDao productDao;
    CategoryDao categoryDao;

    @Before
    public void setUpDB() throws SQLException, IOException {
        dataSource = DataSource.getInstance();
        userDao = new UserDaoImpl(dataSource);
        productDao = new ProductDaoImpl(dataSource);
        categoryDao = new CategoryDaoImpl(dataSource);

        user = getRandomUser();

        category = getRandomCategory();
        product = getRandomProduct();

        lastInsertedIndex = userDao.insertUser(user);
        user.setUserID(lastInsertedIndex);

        categoryDao.insertCategory(category);

        lastIsertedProductIndex = productDao.insertProduct(product);
    }

    @After
    public void tearDown() {

        userDao.deleteAllUsers();
        productDao.deleteProductByID(lastIsertedProductIndex);
        categoryDao.deleteCategoryByID(category.getCategoryID());
        userDao.deleteWishlistByUserID(lastInsertedIndex);
    }

    @Test
    public void deleteUser(){

        userDao.deleteUser(lastInsertedIndex);
        User user1 = userDao.getUserByID(lastInsertedIndex);
        assertNull(user1);
    }

    @Test
    public void insertIntoWishlist(){
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());
        List<Product> whishlist = userDao.getWishlist(user.getUserID());
        assertEquals(whishlist.get(0).getProductID(), product.getProductID());

    }
    @Test
    public void deleteWishlistByUserID(){
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());
        userDao.deleteWishlistByUserID(user.getUserID());
        List<Product> whishlist = userDao.getWishlist(user.getUserID());
        assertEquals(true, whishlist.isEmpty());

    }
    @Test(expected = RuntimeException.class)
    public void insert_user_duplicate(){
        userDao.insertUser(user);

    }
    @Test
    public void insertUser() {

        User user1 = getRandomUser();
        user1.setUsername("newUsername").setEmail("newEmail@gmail.com");
        int insertindex = userDao.insertUser(user1);
        user1.setUserID(insertindex);

        User user2 = userDao.getUserByID(insertindex);
        doAssertion(user2, user1);

        userDao.deleteUser(insertindex);
    }

    @Test
    public void getUserByID() {

        User user1 = userDao.getUserByID(lastInsertedIndex);

        doAssertion(user, user1);
    }
    @Test
    public void getUserByUsername() {

        User user1 = userDao.getUserByUsername(user.getUsername());

        doAssertion(user, user1);
    }
    @Test
    public void deleteAllUsers(){

        userDao.deleteAllUsers();

        List<User> users = userDao.getAllUsers();

        assertEquals(true, users.isEmpty());
    }

    @Test
    public void updateUser() {

        user.setUsername("new username");
        userDao.updateUser(user);

        User user1 = userDao.getUserByID(lastInsertedIndex);

        doAssertion(user, user1);

    }
    @Test
    public void getAllUsers(){

        userDao.deleteAllUsers();

        List<User> users = new ArrayList<>();
        List<User> users1 = new ArrayList<>();

        User user = getRandomUser();
        int insertindex = userDao.insertUser(user);
        user.setUserID(insertindex);
        users.add(user);

        User user1 = getRandomUser();
        user1.setUsername("newUsername").setEmail("newEmail@gmail.com");
        insertindex = userDao.insertUser(user1);
        user1.setUserID(insertindex);
        users.add(user1);

        users1 = userDao.getAllUsers();

        for (int i = 0; i < users1.size(); i++) {
            assertEquals(users.get(i).getUserID(), users1.get(i).getUserID());
            assertEquals(users.get(i).getFirstname(), users1.get(i).getFirstname());
            assertEquals(users.get(i).getUsername(), users1.get(i).getUsername());
            assertEquals(users.get(i).getPassword(), users1.get(i).getPassword());
        }
    }

    private User getRandomUser() {
        user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").setUsername("anigal").setPassword("anahitgal85").setEmail("galstyan@gmailgom").setConfirmationStatus(true).setAccessPrivilege("user");
        return user;
    }

    private Category getRandomCategory(){
        category = new Category();
        category.setName("hat");
        return category;
    }

    private Product getRandomProduct(){
        product = new Product();
        product.setName("baby hat").setPrice(50).setDescription("color:white").setShippingPrice(1).setQuantity(50).setCategory(category);
        return product;
    }
    private void doAssertion(User user, User user1){

        assertEquals(user.getUserID(), user1.getUserID());
        assertEquals(user.getFirstname(), user1.getFirstname());
        assertEquals(user.getLastname(), user1.getLastname());
        assertEquals(user.getUsername(), user1.getUsername());
        assertEquals(user.getPassword(), user1.getPassword());
        assertEquals(user.getPhone(), user1.getPhone());
        assertEquals(user.getAccessPrivilege(), user1.getAccessPrivilege());
        assertEquals(user.getConfirmationStatus(), user1.getConfirmationStatus());
    }

}
