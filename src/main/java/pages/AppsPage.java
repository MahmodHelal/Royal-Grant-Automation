package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * AppsPage — صفحة التطبيقات
 * After login → redirected to apps listing page
 * بعد تسجيل الدخول → صفحة قائمة التطبيقات
 *
 * Real app name from UI: "المنح الملكية"
 * الاسم الحقيقي من الـ UI: "المنح الملكية"
 */
public class AppsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // بطاقة المنح الملكية / Royal Grants app card
    // From real UI screenshot: card contains "المنح الملكية" + "الدخول إلى التطبيق" button
    private By grantsAppButton = By.xpath(
        "//a[contains(@href,'grantroyal')]//button[contains(.,'الدخول')]");

    // Fallback: any button in المنح الملكية section
    private By grantsAppButtonAlt = By.xpath(
        "//div[contains(@class,'card') or contains(@class,'app')]" +
        "[.//text()[contains(.,'المنح الملكية')]]//button");

    // Verify apps page loaded | التحقق من تحميل صفحة التطبيقات
    private By appsPageIndicator = By.xpath(
        "//*[contains(text(),'المنح الملكية')]");

    public AppsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Verifies the apps page is loaded.
     * يتحقق من تحميل صفحة التطبيقات
     */
    public boolean isAppsPageLoaded() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(appsPageIndicator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Clicks the entry button for المنح الملكية app.
     * يضغط زر الدخول لتطبيق المنح الملكية
     */
    public void clickGrantsAppCard() {
        wait.until(ExpectedConditions.elementToBeClickable(grantsAppButton));
        driver.findElement(grantsAppButton).click();

    }
}
