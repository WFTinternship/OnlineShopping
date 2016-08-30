package controllerTest;



import com.workfront.internship.business.ProductManager;
import com.workfront.internship.common.Product;
import com.workfront.internship.controller.ProductController;
import com.workfront.internship.controller.UserController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static controllerTest.TestHelper.getTestProduct;
import static controllerTest.TestHelper.getTestUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class ProductControllerTest {

    private ProductManager productManager;

    private ProductController productController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private Product testProduct;


    @Before
    public void setUp() {
        productController = new ProductController();

        productManager = mock(ProductManager.class);

        Whitebox.setInternalState(productController, "productManager", productManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getSession()).thenReturn(testSession);

        testProduct = getTestProduct();

    }


    @After
    public void tearDown() {

    }

    @Test
    public void getProductDescription() {


        when(userManager.login(testUser.getUsername(), testUser.getPassword())).thenReturn(testUser);
        String result = userController.login(testRequest);
        verify(testSession).setAttribute("user", testUser);

        assertEquals(result, "index");


    }

    @Test
    public void login_fali() {
        when(userManager.login(testUser.getUsername(), testUser.getPassword())).thenReturn(null);
        String errorString = "Username or password invalid";
        //testing method... in case of wrong username or password
        String result = userController.login(testRequest);

        verify(testRequest).setAttribute("errorString", errorString);
        assertEquals(result, "signin");
    }

    @Test
    public void registration_succsess() {
        when(userManager.createAccount(any(User.class))).thenReturn(testUser.getUserID());
        //testing method... success case of registration
        String result = userController.registration(testRequest);

        verify(testSession).setAttribute(any(String.class), any(User.class));

        assertEquals(result, "index");

    }

    @Test
    public void registration_fail() {
        when(userManager.createAccount(any(User.class))).thenReturn(0);
        String errorString = "User with that username already exists";
        //testing method... in case of duplicate username
        String result = userController.registration(testRequest);
        verify(testRequest).setAttribute("errorString", errorString);
        assertEquals(result, "registration");

    }

    @Test
    public void logout() {
        //testing method...
        String result = userController.logout(testRequest);

        verify(testSession).setAttribute("user", null);
        assertEquals(result, "index");

    }
}
