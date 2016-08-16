package selenium.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import selenium.pages.HomePage;
import selenium.pages.RegistrationPage;


import static org.junit.Assert.assertNotNull;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class RegistrationPageTest {
    private static RegistrationPage registrationPage;
    private static HomePage homePage;

    @BeforeClass
    public static void setUp() {
        registrationPage = new RegistrationPage();
        homePage = new HomePage();
        registrationPage.init();
    }

    @AfterClass
    public static void tearDown() {
         registrationPage.getWebDriver().close();
    }

    @Test
    public void registration_success() throws InterruptedException {
        homePage.clickCreateAccount();
        registrationPage.typeFirstname("Anna");
        registrationPage.typeLastname("Asmangulyan");

        registrationPage.typeEmail("asmangulyananna@gmail.com");
        registrationPage.typeUsername("annaasm");
        registrationPage.typePassword("annaanna");
        registrationPage.typeRepeatPassword("annaanna");
        registrationPage.clickCreateAccount();

        // assertFalse("login page is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("loguot button is not displayed", homePage.getCart());
    }

    @Test
    public void registration_fail() throws InterruptedException {
        homePage.clickCreateAccount();
        registrationPage.typeFirstname("Anna");
        registrationPage.typeLastname("Asmangulyan");

        registrationPage.typeEmail("asmangulyananna@gmail.com");
        registrationPage.typeUsername("annaasm");
        registrationPage.typePassword("annaanna");
        registrationPage.typeRepeatPassword("annaanna");
        registrationPage.clickCreateAccount();

        // assertFalse("login page is not closed", homePage.getLoginPopup().isDisplayed());
        assertNotNull("incorrect registration!", registrationPage.getCreateAccountButton());
    }

    @Test
    public void registration_fail_different_passwords() throws InterruptedException {
        homePage.clickCreateAccount();
        registrationPage.typeFirstname("Anna");
        registrationPage.typeLastname("Asmangulyan");

        registrationPage.typeEmail("asmangulyananna@gmail.com");
        registrationPage.typeUsername("annaasm");
        registrationPage.typePassword("annaanna");
        registrationPage.typeRepeatPassword("annaannaanna");
        registrationPage.clickCreateAccount();

        assertNotNull("could not handle error of different passwords", registrationPage.getErrorMessage());
    }
}
