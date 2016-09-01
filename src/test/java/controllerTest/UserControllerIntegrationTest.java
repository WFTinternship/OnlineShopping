package controllerTest;

import com.workfront.internship.common.User;
import com.workfront.internship.controller.UserController;
import com.workfront.internship.spring.TestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Locale;
import java.util.Map;

import static controllerTest.TestHelper.getTestUser;
import static org.junit.Assert.assertEquals;
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

    private HttpServletRequestMock testRequest;
    private User testUser;

    @Before
    public void setUp() {
        testRequest = new HttpServletRequestMock();
        testUser = getTestUser();

        testRequest.setParameter("username", testUser.getUsername());
        testRequest.setParameter("password", testUser.getPassword());
        testRequest.setParameter("firstname", testUser.getFirstname());
        testRequest.setParameter("lastname", testUser.getLastname());
        testRequest.setParameter("email", testUser.getEmail());
        testRequest.setParameter("repeatpassword", testUser.getPassword());

        userController.registration(testRequest);

    }
@Test
    public void login(){

    String result = userController.login(testRequest);

    assertEquals(result, "index");

}
}
