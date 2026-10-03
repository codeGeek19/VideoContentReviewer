package com.example.demo.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseUiTest {

    @Value("${local.server.port}")
    protected int port;

    protected WebDriver driver;
    protected WebDriverWait wait;

    @RegisterExtension
    ScreenshotOnFailure screenshots = new ScreenshotOnFailure(() -> driver);

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:" + port + "/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("count-TOTAL")));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    protected String uniqueTitle(String prefix) {
        return prefix + "-" + System.nanoTime();
    }

    protected By rowOf(String title) {
        return By.xpath("//table[@id='tbl']//tr[td[2]='" + title + "']");
    }

    protected void createRecord(String title, String url) {
        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("videoUrl")).sendKeys(url);
        driver.findElement(By.id("saveBtn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf(title)));
    }

    protected void selectRole(String role) {
        new Select(driver.findElement(By.id("role"))).selectByVisibleText(role);
    }

    protected String statusOf(String title) {
        return driver.findElement(By.xpath("//table[@id='tbl']//tr[td[2]='" + title + "']/td[@class='status']"))
                .getText();
    }
}