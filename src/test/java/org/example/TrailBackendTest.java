package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

/*
 * @author Grace Hanson
 * Tests backend components, such as REST components and posts/gets
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrailBackendTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        // set up the chrome driver
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // helper: login and go to map
    private void loginAndGoToMap() {
        driver.get("http://localhost:3000/login");
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("testpass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // wait and redirect
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get("http://localhost:3000/map");
    }

    @Test
    @Order(1)
    @DisplayName("should block starting trail if not logged in")
    public void testStartTrailBlockedWithoutLogin() {
        driver.get("http://localhost:3000/map");

        WebElement button = driver.findElement(By.cssSelector(".start-trail-button"));
        button.click();

        WebElement alert = driver.findElement(By.className("custom-alert"));
        Assertions.assertTrue(alert.getText().toLowerCase().contains("please select"));
    }

    @Test
    @Order(2)
    @DisplayName("should start a trail when user is logged in")
    public void testStartTrailAllowedAfterLogin() {
        loginAndGoToMap();

        // assumes a trail is selected by default or can be clicked
        WebElement dropdown = driver.findElement(By.className("selected-trail-display"));
        dropdown.click();
        WebElement firstTrail = driver.findElement(By.className("trail-option"));
        firstTrail.click();

        WebElement button = driver.findElement(By.cssSelector(".start-trail-button"));
        button.click();

        WebElement alert = driver.findElement(By.className("custom-alert"));
        Assertions.assertTrue(alert.getText().toLowerCase().contains("started"));
    }

    @Test
    @Order(3)
    @DisplayName("should load rating if trail has one")
    public void testTrailRatingLoads() {
        loginAndGoToMap();

        WebElement dropdown = driver.findElement(By.className("selected-trail-display"));
        dropdown.click();
        WebElement firstTrail = driver.findElement(By.className("trail-option"));
        firstTrail.click();

        WebElement ratingText = driver.findElement(By.className("star-rating-display"));
        Assertions.assertNotNull(ratingText);
        Assertions.assertTrue(ratingText.getText().contains("("));
    }
}
