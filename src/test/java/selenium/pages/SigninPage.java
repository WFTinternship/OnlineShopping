package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class SigninPage extends AbstractPage {
    public void typeUsername(String username) throws InterruptedException {
        WebElement usernameField = getWebDriver().findElement(By.name("username"));
        usernameField.sendKeys(username);
    }

    public void typePassword(String password) throws InterruptedException {
        WebElement passwordField = getWebDriver().findElement(By.name("password"));
        passwordField.sendKeys(password);
    }

    public void clickSignin() {
        WebElement signinButton = getWebDriver().findElement(By.id("signinButton"));
        signinButton.click();

    }

    public WebElement getSigninButton() {
        WebElement signinButton = getWebDriver().findElement(By.id("signinButton"));
        return signinButton;
    }
}
