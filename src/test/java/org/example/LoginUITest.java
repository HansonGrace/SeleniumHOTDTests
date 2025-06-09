package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/*
* @author Grace Hanson
* Login testing for the frontend ONLY. Tests UI components using Selenium and Chromedriver
*
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginUITest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeAll
    public void setUp() {
        // Start Chrome browser and navigate to login page
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:3000/login");
    }

    @Test
    public void testLoginFormElementsVisible() {
        // Verify the login form renders all expected input fields and buttons
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Password']")));
        WebElement backButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Back']")));
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        WebElement registerLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Register")));

        assertTrue(usernameField.isDisplayed());
        assertTrue(passwordField.isDisplayed());
        assertTrue(backButton.isDisplayed());
        assertTrue(loginButton.isDisplayed());
        assertTrue(registerLink.isDisplayed());
    }

    @Test
    public void testAlertShowsOnEmptyFormSubmit() {
        // Trigger alert for empty submission and check that it becomes visible
        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Login']")));
        loginButton.click();

        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("custom-alert")));
        assertTrue(alert.isDisplayed());
        assertFalse(alert.getText().isEmpty());
    }

    @AfterAll
    public void tearDown() {
        //quit the browser
        driver.quit();
    }
}
