package selenium.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import selenium.pages.HomePage;
import selenium.pages.SigninPage;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class SigninPageTest {
    private static SigninPage signinPage;
    private static HomePage homePage;

    @BeforeClass
    public static void setUp() {
        signinPage = new SigninPage();
        homePage = new HomePage();
        signinPage.init();
    }

    @AfterClass
    public static void tearDown() {
        signinPage.getWebDriver().close();
    }

    @Test
    public void login_success() throws InterruptedException {
        homePage.clickLogin();
        signinPage.typeUsername("sonamika");
        signinPage.typePassword("sonasona");
        signinPage.clickSignin();

        // assertFalse("login page is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("loguot button is not displayed", homePage.getCart());
    }

    @Test
    public void login_fail() throws InterruptedException {
        homePage.clickLogin();
        signinPage.typeUsername("asasdadadasd");
        signinPage.typePassword("asfawefwaefawd");
        signinPage.clickSignin();

        // assertFalse("login page is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("login was done", signinPage.getSigninButton());
    }
}
