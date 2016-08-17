package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by Anna Asmangulyan on 8/12/2016.
 */
public class AbstractPage {

    static private WebDriver webDriver;


    static public void init(String url) {
        webDriver = new ChromeDriver();
        webDriver.get(url);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }


}


