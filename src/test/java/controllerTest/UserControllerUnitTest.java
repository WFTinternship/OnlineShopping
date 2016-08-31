package controllerTest;

import com.workfront.internship.business.*;
import com.workfront.internship.common.Category;
import com.workfront.internship.common.Media;
import com.workfront.internship.common.Product;
import com.workfront.internship.common.User;
import com.workfront.internship.controller.HomePageController;
import com.workfront.internship.controller.UserController;
import com.workfront.internship.dao.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static controllerTest.TestHelper.getTestUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by Anna Asmangulyan on 8/30/2016.
 */
public class UserControllerUnitTest {

    private UserManager userManager;

    private UserController userController;
    private HttpServletRequest testRequest;
    private HttpSession testSession;

    private User testUser;


    @Before
    public void setUp() {
        userController = new UserController();

        userManager = mock(UserManager.class);

        Whitebox.setInternalState(userController, "userManager", userManager);

        testRequest = mock(HttpServletRequest.class);
        testSession = mock(HttpSession.class);

        when(testRequest.getSession()).thenReturn(testSession);

        testUser = getTestUser();
        when(testRequest.getParameter("username")).thenReturn(testUser.getUsername());
        when(testRequest.getParameter("password")).thenReturn(testUser.getPassword());

    }


    @After
    public void tearDown() {

    }

    @Test
    public void login_success() {


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

        verify(testSession).setAttribute(eq("user"), any(User.class));

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
    @Test
    public void getLoginPage(){
        String result = userController.getLoginPage();
        assertEquals("signin", result);

    }
    @Test
    public void getRegistrationPage(){
        String result = userController.getRegistrationPage();
        assertEquals("registration", result);

    }
}
