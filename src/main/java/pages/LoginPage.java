package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * LoginPage — صفحة تسجيل الدخول
 * URL: geoservices1.syadtech.com/home/Login
 * reCAPTCHA is DISABLED in test environment
 *
 * LOCATOR STRATEGY:
 * Avoid matching Arabic button text directly — encoding issues in Maven console
 * cause Arabic text to appear as ??? in XPath at runtime.
 * Use structural locators instead (button type, position, class).
 *
 * استراتيجية الـ locators:
 * تجنب مطابقة النص العربي مباشرة في زر الـ login — مشكلة encoding في Maven
 * تسبب ظهور النص العربي كـ ??? في وقت التشغيل.
 * استخدم محددات هيكلية بدلاً من ذلك.
 */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    // Fallback username locator | محدد احتياطي
    private By usernameFieldAlt = By.xpath(
            "//label[contains(text(),'اسم المستخدم')]/following::input[1]");

    // Username: first text input on page
    private By usernameField = By.xpath("(//input[@type='text'])[1]");

    // Password: only password input
    private By passwordField = By.xpath("//input[@type='password']");

    // Login button: the only submit button on login form
    // زر تسجيل الدخول / Login button — real text from UI: "تسجيل الدخول"
    private By loginButton = By.xpath(
            "//button[contains(.,'تسجيل الدخول')]");

    // Page load indicator: password field visible = login page loaded
    private By pageLoadedIndicator = By.xpath("//input[@type='password']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Performs login with given credentials.
     * يُجري تسجيل الدخول بالبيانات المعطاة.
     */
    public void login(String username, String password) {
        // Wait for form to load | انتظر تحميل النموذج
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));

        // Enter username | إدخال اسم المستخدم
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
            driver.findElement(usernameField).clear();
            driver.findElement(usernameField).sendKeys(username);
        } catch (Exception e) {
            // Try fallback | جرّب المحدد الاحتياطي
            driver.findElement(usernameFieldAlt).clear();
            driver.findElement(usernameFieldAlt).sendKeys(username);
        }

        // Enter password | إدخال كلمة المرور
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);

        // Click login | الضغط على تسجيل الدخول
        driver.findElement(loginButton).click();
    }
}
