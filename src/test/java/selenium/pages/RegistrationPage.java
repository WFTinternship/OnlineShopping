package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class RegistrationPage extends AbstractPage{
    public void typeFirstname(String firstname) throws InterruptedException {
        WebElement firstnameField = getWebDriver().findElement(By.name("firstname"));
        Thread.sleep(1000);
        firstnameField.sendKeys(firstname);
    }
    public void typeLastname(String lastname) throws InterruptedException {
        WebElement lastnameField = getWebDriver().findElement(By.name("lastname"));
        Thread.sleep(1000);
        lastnameField.sendKeys(lastname);
    }
    public void typeEmail(String email) throws InterruptedException {
        WebElement emailField = getWebDriver().findElement(By.name("email"));
        Thread.sleep(1000);
        emailField.sendKeys(email);
    }
    public void typeUsername(String username) throws InterruptedException {
        WebElement usernameField = getWebDriver().findElement(By.name("username"));
        Thread.sleep(1000);
        usernameField.sendKeys(username);
    }

    public void typePassword(String password) throws InterruptedException {
        WebElement passwordField = getWebDriver().findElement(By.name("password"));
        Thread.sleep(2000);
        passwordField.sendKeys(password);
    }
    public void typeRepeatPassword(String repeatPassword) throws InterruptedException {
        WebElement repeatPasswordField = getWebDriver().findElement(By.name("repeat password"));
        Thread.sleep(2000);
        repeatPasswordField.sendKeys(repeatPassword);
        Thread.sleep(2000);
    }

    public void clickCreateAccount() {
        WebElement createAccountButton = getWebDriver().findElement(By.id("createAccountButton"));
        createAccountButton.click();

    }
}
