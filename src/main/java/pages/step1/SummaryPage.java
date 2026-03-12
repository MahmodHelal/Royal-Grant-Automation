package pages.step1;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 4 — الملخص / Summary (Citizen view — submit step)
// Step indicator: <div name="summery" class="ant-steps-item-active">
// ═══════════════════════════════════════════════════════════════════════════════
public class SummaryPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Step indicator
    private By summaryStepActive = By.xpath(
            "//div[@name='summery'][contains(@class,'ant-steps-item-active')]");

    // Submit button — class: btn-success, text: "ارسال" (not إرسال)
    // ملاحظة: النص "ارسال" وليس "إرسال" — Bug #826 related
    private By submitBtn = By.xpath(
            "//button[contains(@class,'btn-success')][.//span[contains(text(),'ارسال')]]");

    // Summary tree navigation (read-only display)
    private By summaryTree = By.xpath(
            "//*[contains(@class,'tree') or contains(@class,'navigation')]" +
                    "[.//*[contains(text(),'بيانات المالك') or contains(text(),'الملاك')]]");

    // confirmation modal
    private By confirmModal = By.xpath("//div[contains(@class,'ant-modal-content')]");

    // Yes button
    private By confirmYesBtn = By.xpath("//button[.//span[text()='نعم']]");

    // No button (optional)
    private By confirmNoBtn = By.xpath("//button[.//span[text()='لا']]");
    public SummaryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isSummaryPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(summaryStepActive));
            return true;
        } catch (Exception e) {
            // fallback: look for submit button
            try {
                return driver.findElement(submitBtn).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Clicks the "ارسال" submit button to finalize the transaction.
     * يضغط زر "ارسال" لإرسال الطلب.
     */
    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        driver.findElement(submitBtn).click();
        System.out.println("[INFO] Clicked: ارسال (submit transaction)");
        confirmSubmit();

    }
    private void confirmSubmit() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));

        WebElement yesBtn = wait.until(
                ExpectedConditions.elementToBeClickable(confirmYesBtn)
        );

        yesBtn.click();

        System.out.println("[INFO] Confirmation accepted (نعم).");
    }
}
