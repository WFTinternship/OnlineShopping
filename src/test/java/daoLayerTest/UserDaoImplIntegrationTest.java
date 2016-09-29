package daoLayerTest;

import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.*;
import com.workfront.internship.spring.TestConfiguration;
import controllerTest.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class UserDaoImplIntegrationTest {

    private User user = null;
    private Product product = null;
    private Category category = null;
    private int lastInsertedIndex = 0;
    private int lastIsertedProductIndex = 0;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CategoryDao categoryDao;

    @Before
    public void setUpDB() throws SQLException, IOException {

        user = TestHelper.getTestUser();
        userDao.insertUser(user);

        category = TestHelper.getTestCategory();
        categoryDao.insertCategory(category);

        product = TestHelper.getTestProduct();
        product.setCategory(category);
        lastInsertedIndex = productDao.insertProduct(product);
    }

    @After
    public void tearDown() {

        userDao.deleteAllUsers();

        categoryDao.deleteAllCategories();

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
       //testing method...
        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());

        List<Product> whishlist = userDao.getWishlist(user.getUserID());

        assertEquals(whishlist.get(0).getProductID(), product.getProductID());

    }
    @Test
    public void deleteFromWishlist(){

        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());
        //testing method...
        userDao.deleteFromWishlistByUserIDAndProductID(user.getUserID(), product.getProductID());

        List<Product> whishlist = userDao.getWishlist(user.getUserID());

        assertTrue(whishlist.isEmpty());

    }
    @Test
    public void deleteWishlistByUserID(){

        userDao.insertIntoWishlist(user.getUserID(), product.getProductID());
        //testing method...
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

        User user1 = TestHelper.getTestUser();
        user1.setUsername("newUsername").setEmail("newEmail@gmail.com");
        int insertindex = userDao.insertUser(user1);
        user1.setUserID(insertindex);

        User user2 = userDao.getUserByID(insertindex);
        doAssertion(user2, user1);

        userDao.deleteUser(insertindex);
    }
    @Test(expected = RuntimeException.class)
    public void insertUser_duplicate(){
        userDao.insertUser(user);
    }

    @Test
    public void getUserByID() {

        User user1 = userDao.getUserByID(user.getUserID());

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

        User user1 = userDao.getUserByID(user.getUserID());

        doAssertion(user, user1);

    }
    @Test
    public void getAllUsers(){

        userDao.deleteAllUsers();

        List<User> users = new ArrayList<>();
        List<User> users1 = new ArrayList<>();

        User user = TestHelper.getTestUser();
        int insertindex = userDao.insertUser(user);
        user.setUserID(insertindex);
        users.add(user);

        User user1 = TestHelper.getTestUser();
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
