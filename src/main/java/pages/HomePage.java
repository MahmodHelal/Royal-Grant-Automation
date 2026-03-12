package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class HomePage {
    private WebDriver driver;

    // Button to open the dropdown (by unique class and child image with alt, or by text)
    private By profileDropdownButton = By.xpath("//button[contains(@class, 'ant-dropdown-trigger') and .//img[@alt='userPhoto']]");
    // Logout option in the dropdown
    private By logoutButton = By.xpath("//li[contains(text(),'تسجيـل خروج')]");
    // Login button as before
    private By loginButton = By.xpath("//a[@href='/home/Login']");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }



    public void performLogoutIfLoggedIn() {
        // If already on login page — skip logout entirely
        if (!driver.findElements(By.xpath("//input[@type='password']")).isEmpty()) {
            System.out.println("[INFO] Already on login page, skipping logout.");
            return;
        }

        try {
            if (!driver.findElements(profileDropdownButton).isEmpty()) {
                driver.findElement(profileDropdownButton).click();
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
                driver.findElement(logoutButton).click();
                new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.presenceOfElementLocated(loginButton));
            } else {
                System.out.println("[INFO] No active session, skipping logout.");
            }
        } catch (Exception e) {
            System.out.println("[INFO] performLogoutIfLoggedIn skipped: " + e.getMessage());
        }
    }
/*    public void clickLoginButton() {
        if (driver.findElements(loginButton).isEmpty()) {
            performLogoutIfLoggedIn();
        }
        driver.findElement(loginButton).click();
    }*/
}
