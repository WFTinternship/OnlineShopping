package selenium.tests;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.User;
import com.workfront.internship.dao.DataSource;
import org.junit.*;
import selenium.pages.HomePage;
import selenium.pages.RegistrationPage;


import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class RegistrationPageTest {
    private static RegistrationPage registrationPage;
    private static HomePage homePage;
    private static DataSource dataSource;
    private static UserManager userManager;
    private static User testUser;
    @Before
    public void setUP() throws IOException, SQLException {


        testUser = getTestUser();
        userManager.createAccount(testUser);
    }

    @After
    public void tearDown() {
        userManager.deleteAllUsers();
        registrationPage.getWebDriver().get("http://localhost:8080/registration.jsp");

    }
    @BeforeClass
    public static void setUpClass() throws IOException, SQLException {
        dataSource = DataSource.getInstance();
        userManager = new UserManagerImpl(dataSource);
        registrationPage = new RegistrationPage();
        homePage = new HomePage();
        registrationPage.init("http://localhost:8080/registration.jsp");
    }

    @AfterClass
    public static void tearDownClass() {
        registrationPage.getWebDriver().close();
        dataSource=null;
        homePage = null;
        registrationPage=null;
        testUser=null;
    }



    @Test
    public void registration_success() throws InterruptedException {
      //  homePage.clickCreateAccount();
        User user=getTestUser();
        user.setUsername("newUsername").setEmail("newEmail@gmail.com");
        registrationPage.register(user);
        registrationPage.typeRepeatPassword(user.getPassword());
        registrationPage.clickCreateAccount();

        // assertFalse("login page is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("loguot button is not displayed", homePage.getCart());
    }

    @Test
    public void registration_fail() throws InterruptedException {
      //  homePage.clickCreateAccount();
        registrationPage.register(testUser);
        registrationPage.typeRepeatPassword(testUser.getPassword());
        registrationPage.clickCreateAccount();

        assertTrue("incorrect registration!", registrationPage.getErrorString().isDisplayed());
    }

    @Test
    public void registration_fail_different_passwords() throws InterruptedException {
       // homePage.clickCreateAccount();
        registrationPage.register(testUser);
        registrationPage.typeRepeatPassword("annaannaanna");
        registrationPage.clickCreateAccount();

        assertTrue("could not handle error of different passwords", registrationPage.getErrorMessage().isDisplayed());
    }
    private User getTestUser() {
        User user = new User();
        user.setFirstname("Anahit").setLastname("galstyan").
                setUsername("anigal").setPassword("anahitgal85").
                setEmail("galstyan@gmail.com").setConfirmationStatus(true).
                setAccessPrivilege("user");
        return user;
    }
}
