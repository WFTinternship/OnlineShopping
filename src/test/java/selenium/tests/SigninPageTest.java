package selenium.tests;

import com.workfront.internship.business.UserManager;
import com.workfront.internship.business.UserManagerImpl;
import com.workfront.internship.common.User;

import org.junit.*;
import org.mockito.internal.util.reflection.Whitebox;
import selenium.pages.HomePage;
import selenium.pages.SigninPage;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class SigninPageTest {
    private static SigninPage signinPage;
    private static LegacyDataSource dataSource;
    private static UserManager userManager;
    private static HomePage homePage;
    private static User testUser;

    @Before
    public  void setUP() throws IOException, SQLException {
        testUser = getTestUser();
        userManager.createAccount(testUser);
        testUser = getTestUser();

    }
    @After
    public void tearDown() {
        userManager.deleteAllUsers();
        signinPage.getWebDriver().get("http://localhost:8080/signin.jsp");

    }

    @BeforeClass
    public static void setUpClass() throws IOException, SQLException {
        dataSource = LegacyDataSource.getInstance();
        userManager = new UserManagerImpl();
        Whitebox.setInternalState(userManager, "dataSource", dataSource);
        signinPage = new SigninPage();
        homePage = new HomePage();
        signinPage.init("http://localhost:8080/signin.jsp");
    }

    @AfterClass
    public static void tearDownClass() {
        signinPage.getWebDriver().close();
        userManager=null;
        signinPage=null;
        dataSource=null;
        testUser=null;
    }

    @Test
    public void login_success() throws InterruptedException {
        signinPage.typeUsername(testUser.getUsername());
        signinPage.typePassword(testUser.getPassword());
        signinPage.clickSignin();

        assertNotNull("loguot button is not displayed", homePage.getCart());
    }

    @Test
    public void login_fail() throws InterruptedException {
        signinPage.typeUsername("asasdadadasd");
        signinPage.typePassword("asfawefwaefawd");
        signinPage.clickSignin();

        assertNotNull("login was done", signinPage.getSigninButton());
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
