package selenium.tests;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import selenium.pages.HomePage;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class HomePageTest {
    static private HomePage homePage;

    @BeforeClass
    static public void setUp() {
        homePage = new HomePage();
        homePage.init();
    }

    @AfterClass
    static public void tearDown() {
        homePage.getWebDriver().close();
    }

    @Test
    public void loginButtonClick() throws InterruptedException {
        WebElement loginPage = homePage.clickLogin();
        Assert.assertNotNull("Login Page is not displayed", loginPage);
    }
    @Test
    public void createAccountButtonClick() throws InterruptedException {
        WebElement registrationPage = homePage.clickCreateAccount();
        Assert.assertNotNull("Login Page is not displayed", registrationPage);
    }
}
