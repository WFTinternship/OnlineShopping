package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class HomePage extends AbstractPage {
    public HomePage() {
        //getWebDriver().get("http://localhost:8080");
    }

    public WebElement clickLogin() throws InterruptedException {
        WebElement loginButton = getWebDriver().findElement(By.cssSelector("#login_button"));
        loginButton.click();
        return getLoginPage();
    }

    public WebElement clickCreateAccount() throws InterruptedException {
        Thread.sleep(2000);
        WebElement createAccountButton = getWebDriver().findElement(By.cssSelector("#registration_button"));
        createAccountButton.click();
        return getRegistrationPage();
    }

    public WebElement getLoginPage() throws InterruptedException {
        Thread.sleep(2000);
        WebElement loginPage = getWebDriver().findElement(By.cssSelector("#signinPage"));
        return loginPage;
    }

    public WebElement getRegistrationPage() throws InterruptedException {
        Thread.sleep(2000);
        WebElement registrationPage = getWebDriver().findElement(By.cssSelector("#registrationPage"));
        return registrationPage;
    }

    public WebElement getCart() {
        return getWebDriver().findElement(By.cssSelector(".cart"));
    }

    public WebElement hoverOnMenu() throws InterruptedException {
        WebElement createAccountButton = getWebDriver().findElement(By.cssSelector("#dropdown1"));

        Actions actions = new Actions(getWebDriver());
        actions.moveToElement(createAccountButton).perform();
        Thread.sleep(1000);
        return getDropdownContent();
    }


    public WebElement getDropdownContent() throws InterruptedException {
        Thread.sleep(2000);
        WebElement registrationPage = getWebDriver().findElement(By.cssSelector("#dropdown-content1"));
        return registrationPage;
    }
}