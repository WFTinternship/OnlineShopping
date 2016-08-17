package selenium.pages;

import com.workfront.internship.common.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class RegistrationPage extends AbstractPage {

    public void register(User user) throws InterruptedException {
        typeFirstname(user.getFirstname());
        typeLastname(user.getLastname());
        typeEmail(user.getEmail());
        typeUsername(user.getUsername());
        typePassword(user.getPassword());
    }
    public void typeFirstname(String firstname) throws InterruptedException {
        WebElement firstnameField = getWebDriver().findElement(By.name("firstname"));
        firstnameField.sendKeys(firstname);
    }

    public void typeLastname(String lastname) throws InterruptedException {
        WebElement lastnameField = getWebDriver().findElement(By.name("lastname"));
        lastnameField.sendKeys(lastname);
    }

    public void typeEmail(String email) throws InterruptedException {
        WebElement emailField = getWebDriver().findElement(By.name("email"));
        emailField.sendKeys(email);
    }

    public void typeUsername(String username) throws InterruptedException {
        WebElement usernameField = getWebDriver().findElement(By.name("username"));
        usernameField.sendKeys(username);
    }

    public void typePassword(String password) throws InterruptedException {
        WebElement passwordField = getWebDriver().findElement(By.name("password"));
        passwordField.sendKeys(password);

    }

    public void typeRepeatPassword(String repeatPassword) throws InterruptedException {
        WebElement repeatPasswordField = getWebDriver().findElement(By.name("repeatpassword"));
        repeatPasswordField.sendKeys(repeatPassword);

    }

    public void clickCreateAccount() throws InterruptedException {
        WebElement createAccountButton = getWebDriver().findElement(By.id("createAccountButton"));
        createAccountButton.click();

    }

    public WebElement getErrorMessage() {
        WebElement errorMessage = getWebDriver().findElement(By.id("message"));
        return errorMessage;
    }
    public WebElement getErrorString(){
        WebElement errorString = getWebDriver().findElement(By.id("errorString"));
        return errorString;
    }
}
