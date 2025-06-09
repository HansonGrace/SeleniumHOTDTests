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
* Selenium automated testing integrated through JUNit and ChromeDriver.
* Used to test frontend functions for web application "Hike of the Day"
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapUITest {

    //instance for browser control
    WebDriver driver;
    WebDriverWait wait;

    @BeforeAll
    public void setUp() {
        //launch chrome browser
        driver = new ChromeDriver();
        //maximize window
        driver.manage().window().maximize();
        //set up wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:3000/map"); // Adjust if needed
    }

    @Test
    public void testHeaderButtonsVisible() {
        //verifies home & account buttons render in the header
        WebElement homeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("home")));
        WebElement accountBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account")));
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("plan")));

        //verify elements are displayed & correct
        assertTrue(homeBtn.isDisplayed());
        assertTrue(accountBtn.isDisplayed());
        assertTrue(title.isDisplayed());
        //validates the title
        assertEquals("Plan Your Hike", title.getText());
    }

    @Test
    public void testFilterDropdownVisible() {
        //wait for filter dropdown with ID to appear
        WebElement filterSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filter1")));
        //verify its displayed
        assertTrue(filterSelect.isDisplayed());
    }

    @Test
    public void testSearchRadiusSliderWorks() {
        //locate slider input
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-radius-slider")));
        //confirm slider is visible
        assertTrue(slider.isDisplayed());

        //simulates setting the slider to 25 using Javascript
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = 25;", slider);
        //verify
        assertEquals("25", slider.getAttribute("value"));

        //simulates setting slider to 45 and verify again
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = 45;", slider);
        assertEquals("45", slider.getAttribute("value"));


    }

    @Test
    public void testDropdownInteraction() {
        //locate selected trail dropdown display element and clicks to open trail
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("selected-trail-display")));
        dropdown.click();

        //after opening locate search input field inside of dropdown
        WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search trails...']")));

        //confirm input box is shown
        assertTrue(inputBox.isDisplayed());
    }

    @AfterAll
    public void tearDown() {
        //close browser when finished
        System.out.println("Tests finished. Exiting");
        driver.quit();
    }
}
