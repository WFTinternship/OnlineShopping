package selenium.tests;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
    static public void tearDownAfterClass() {
        homePage.getWebDriver().close();
    }

    @After
    public void tearDown() {
        homePage.getWebDriver().get("http://localhost:8080");
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

    @Test
    public void hoverOnMenu() throws InterruptedException {
        Thread.sleep(1000);
        WebElement dropDownMenu = homePage.hoverOnMenu();
        Assert.assertTrue("menu is not displayed", dropDownMenu.isDisplayed());
        WebElement webElement = homePage.getWebDriver().findElement(By.cssSelector(".wrapper"));
        Actions actions = new Actions(homePage.getWebDriver());
        actions.moveToElement(webElement).perform();

    }
}
