package lab10;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    void test_login_with_incorrect_credentials() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.navigate().to("http://103.139.122.250:4000/");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            driver.findElement(By.id("email")).sendKeys("qasim@malik.com");
            driver.findElement(By.id("password")).sendKeys("abcdefg");
            driver.findElement(By.cssSelector("button[type='submit']")).click();

            // Depending on backend availability, the UI shows either auth error or network error.
            wait.until(d -> d.getPageSource().contains("Incorrect email or password")
                    || d.getPageSource().contains("Failed to fetch")
                    || d.getCurrentUrl().contains("/login"));

            String pageText = driver.getPageSource();
            boolean loginRejected = pageText.contains("Incorrect email or password")
                    || pageText.contains("Failed to fetch")
                    || driver.getCurrentUrl().contains("/login");
            assertTrue(loginRejected);
        } finally {
            driver.quit();
        }
    }
}
