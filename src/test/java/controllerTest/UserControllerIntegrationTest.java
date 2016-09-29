package controllerTest;

import com.workfront.internship.business.CategoryManager;
import com.workfront.internship.business.ProductManager;
import com.workfront.internship.business.UserManager;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.controller.UserController;
import com.workfront.internship.spring.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static controllerTest.TestHelper.getTestCategory;
import static controllerTest.TestHelper.getTestUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Anna Asmangulyan on 8/31/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserManager userManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private CategoryManager categoryManager;

    private HttpServletRequestMock testRequest;
    private User testUser;
    private Product testProduct;
    private Category testCategory;

    @Before
    public void setUp() {
        testRequest = new HttpServletRequestMock();
        testUser = getTestUser();
        testProduct = TestHelper.getTestProduct();
        testCategory = getTestCategory();

        categoryManager.createNewCategory(testCategory);


        testProduct.setCategory(testCategory);
        productManager.createNewProduct(testProduct);

        testRequest.setParameter("username", testUser.getUsername());
        testRequest.setParameter("password", testUser.getPassword());
        testRequest.setParameter("firstname", testUser.getFirstname());
        testRequest.setParameter("lastname", testUser.getLastname());
        testRequest.setParameter("email", testUser.getEmail());
        testRequest.setParameter("repeatpassword", testUser.getPassword());

        testRequest.getSession().setAttribute("user", testUser);

        userController.registration(testRequest);

    }

    @After
    public void tearDown() {
        userManager.deleteAllUsers();
        categoryManager.deleteAllCategories();
    }

    @Test
    public void login() {
        //testing method...
        String result = userController.login(testRequest);

        Object object = testRequest.getSession().getAttribute("user");

        assertEquals(result, "index");
        assertNotNull(object);

    }

    @Test
    public void registration() {
        userManager.deleteAllUsers();
        //testing method...
        String result = userController.registration(testRequest);

        Object object = testRequest.getSession().getAttribute("user");

        assertEquals(result, "index");
        assertNotNull(object);


    }
    @Test
    public void addToList(){
        testRequest.setParameter("productId", Integer.toString(testProduct.getProductID()));
        //testing method...
        String result = userController.addToList(testRequest);

        assertEquals("wrong message", "the item is added to your wishlist", result);

    }
    @Test
    public void addToList_duplicate(){
        testRequest.setParameter("productId", Integer.toString(testProduct.getProductID()));
        //testing method...
        userController.addToList(testRequest);

        String result = userController.addToList(testRequest);

        assertEquals("wrong message", "the item is already in your wishlist", result);

    }
    @Test
    public void showWishlistContent(){

        userManager.addToList(testUser, testProduct);
        //testing method...
        String result = userController.showWishlistContent(testRequest);

        assertNotNull(testRequest.getAttribute("products"));

        assertEquals("can not get wishlist", result, "wishList");
    }
    @Test
    public void deleteFromList(){

        testRequest.setParameter("productId", Integer.toString(testProduct.getProductID()));
        userManager.addToList(testUser, testProduct);
        //testing method...
        userController.deleteFromList(testRequest);

        List<Product> products = userManager.getList(testUser);

        assertTrue(products.isEmpty());
    }


}
