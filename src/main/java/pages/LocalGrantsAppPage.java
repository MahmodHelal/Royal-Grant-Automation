package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

/**
 * LocalGrantsAppPage — الصفحة الرئيسية لتطبيق المنح (localhost:8080)
 *
 * Handles:
 * - New transaction dropdown (Citizen)
 * - Sidebar navigation (Employee / Manager)
 * - Inbox table rows (عرض / متابعة / طباعة)
 * - Success toast messages
 */
public class LocalGrantsAppPage {

    private final Logger log = LoggerFactory.getLogger(LocalGrantsAppPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    // ─── New Transaction Dropdown ────────────────────────────────────────────
    // "معاملة جديدة" dropdown trigger — class: addTranDiv
    private By newTransactionDropdown = By.xpath(
        "//div[contains(@class,'addTranDiv')]//div[contains(@class,'ant-select-selection--single')]");

    // Dropdown option: "طلب منح ملكية"
    private By grantRequestOption = By.xpath(
        "//li[@role='option'][contains(text(),'طلب منح ملكية')]");

    // ─── Sidebar Menu ────────────────────────────────────────────────────────
    // المعاملات الواردة — Incoming transactions (for employee & manager)
// Try broader locator — any element containing the text
    private By incomingTransactionsLink = By.xpath(
            "//*[contains(text(),'المعاملات الواردة')]"
    );
    // ─── Inbox Table Buttons ─────────────────────────────────────────────────
    // From real DOM: <button class="btn btn-success view">عرض</button>
    private By viewButtonFirst = By.xpath(
        "(//button[contains(@class,'view') and contains(text(),'عرض')])[1]");

    // From real DOM: <button class="btn btn-success follow">متابعة</button>
    // Note: In grant_manager inbox, only عرض button exists (no متابعة)
    // ملاحظة: في inbox مدير المنح، يوجد زر عرض فقط (لا يوجد متابعة)
    private By followButtonFirst = By.xpath(
        "(//button[contains(@class,'follow') and contains(text(),'متابعة')])[1]");

    // ─── Success Toast ────────────────────────────────────────────────────────
    // Success messages observed:
    //   Step 1 submit:  "تم حفظ وارسال المعاملة بنجاح برقم ١٤٤٧ / ٦٠"
    //   Step 2 forward: "تم حفظ وارسال المعاملة بنجاح برقم ١٤٤٧ / ٦٠"
    //   Step 3 finish:  "تم انهاء المعاملة رقم ١٤٤٧ / ٦٠"
    private By successToast = By.xpath(
        "//*[contains(text(),'تم حفظ') or contains(text(),'تم انهاء')]");

    // Transaction number from toast
    private By toastMessage = By.xpath(
        "//*[contains(@class,'toast') or contains(@class,'ant-message') " +
        "or contains(@class,'notification') or contains(@class,'success')]" +
        "[contains(text(),'تم')]");
    private By successMessage = By.xpath("//div[contains(@class,'ant-message')]//span");

    // في LocalGrantsAppPage أو Step2Page
    private By nextButton = By.xpath("//button[contains(@class,'next')]//span[text()='التالى']/parent::button");


    public void clickNext() {
        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(nextButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        btn.click();
        log.info("Clicked التالى");
    }

    public LocalGrantsAppPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ─── Citizen: New Transaction ─────────────────────────────────────────────

    /**
     * Opens the "معاملة جديدة" dropdown and selects "طلب منح ملكية"
     */
    public void startNewGrantTransaction() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.spinner-bk")
                ));
        wait.until(ExpectedConditions.elementToBeClickable(newTransactionDropdown));
        driver.findElement(newTransactionDropdown).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(grantRequestOption));
        driver.findElement(grantRequestOption).click();

        System.out.println("[INFO] New grant transaction wizard opened.");
    }

    // ─── Employee / Manager: Inbox ────────────────────────────────────────────

    /**
     * Clicks "المعاملات الواردة" in the sidebar.
     * يضغط على "المعاملات الواردة" في الشريط الجانبي.
     */
    public void clickIncomingTransactions() {
        // Wait for spinner to disappear first
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.spinner-bk")
                ));

        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'المعاملات الواردة')]")
                ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(el)).click();

        log.info("Clicked المعاملات الواردة from sidebar.");
    }
    /**
     * Clicks "عرض" on the first transaction in the inbox table.
     * يضغط عرض على أول معاملة في الجدول.
     *
     * Used by both grant_emp (then follows up with متابعة)
     * and grant_manager (عرض only — opens summary wizard directly)
     */
    public void clickViewOnFirstTransaction() {
        wait.until(ExpectedConditions.elementToBeClickable(viewButtonFirst));
        driver.findElement(viewButtonFirst).click();
        System.out.println("[INFO] Clicked: عرض (first transaction)");
    }

    /**
     * Clicks "متابعة" on the first transaction.
     * يضغط متابعة على أول معاملة.
     * ONLY available for grant_emp — not shown in grant_manager inbox.
     */
    public void clickFollowUpOnFirstTransaction() {
        wait.until(ExpectedConditions.elementToBeClickable(followButtonFirst));
        driver.findElement(followButtonFirst).click();
        System.out.println("[INFO] Clicked: متابعة (first transaction)");
    }

    // ─── Success Verification ─────────────────────────────────────────────────

    /**
     * Waits for and returns the success message text.
     * ينتظر رسالة النجاح ويرجع نصها.
     */
    public String getSuccessMessageText() {

        WebElement msg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(successMessage)
        );

        wait.until(driver -> !msg.getText().trim().isEmpty());

        return msg.getText().trim();
    }


    /**
     * Verifies success message contains expected text.
     * يتحقق من أن رسالة النجاح تحتوي على النص المتوقع.
     */
    public boolean isSuccessMessageDisplayed(String expectedText) {

        String actual = getSuccessMessageText();

        System.out.println("[INFO] Success message: " + actual);

        return actual.startsWith(expectedText);
    }

    public void clickViewOnTransaction(String txNumber) {
        int maxPages = 10;
        // Convert Latin numerals to Arabic for matching table content
        String arabicTxNumber = txNumber
                .replace("0","٠").replace("1","١").replace("2","٢")
                .replace("3","٣").replace("4","٤").replace("5","٥")
                .replace("6","٦").replace("7","٧").replace("8","٨")
                .replace("9","٩");

        System.out.println("[INFO] Searching for transaction: " + arabicTxNumber);

        for (int page = 1; page <= maxPages; page++) {
            // Scroll through current page looking for the transaction
            WebElement found = scrollAndFind(arabicTxNumber);

            if (found != null) {
                WebElement viewBtn = found.findElement(
                        By.xpath("./following-sibling::td//button[contains(@class,'view')]")
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewBtn);
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(viewBtn)).click();
                log.info("Clicked عرض for transaction {} on page {}", arabicTxNumber, page);
                return;
            }

            // Not found — try next page
            By nextBtn = By.xpath(
                    "//li[contains(@class,'ant-pagination-next') and not(contains(@class,'disabled'))]//a"
            );

            if (driver.findElements(nextBtn).isEmpty()) {
                break; // No more pages
            }

            driver.findElement(nextBtn).click();
            log.info("Transaction not on page {}, going to page {}...", page, page + 1);
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        }

        throw new RuntimeException("Transaction not found in any page: " + txNumber);
    }

    private WebElement scrollAndFind(String txNumber) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll down gradually looking for the row
        for (int i = 0; i < 10; i++) {
            List<WebElement> rows = driver.findElements(
                    By.xpath("//td[normalize-space(text())='" + txNumber + "']")
            );

            if (!rows.isEmpty()) {
                return rows.get(0);
            }

            // Scroll down 300px and try again
            js.executeScript("window.scrollBy(0, 300);");
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        // Scroll back to top before switching page
        js.executeScript("window.scrollTo(0, 0);");
        return null;
    }
    private By buildViewLocator(String txNumber) {
        return By.xpath(
                "//td[contains(text(),'" + txNumber + "')]" +
                        "/following-sibling::td//button[contains(@class,'view')]"
        );
    }    public void clickFollowUpOnTransaction(String txNumber) {
        By followBtn = By.xpath(
                "//td[normalize-space(text())='" + txNumber + "']" +
                        "/following-sibling::td//button[contains(@class,'follow')]"
        );
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(followBtn)).click();
        log.info("Clicked متابعة for transaction: {}", txNumber);
    }
    /**
     * Clicks "عرض" on the transaction at the given index (0-based) in the inbox table.
     * TX-01 → index 0 (أول معاملة), TX-02 → index 1 (تاني معاملة), etc.
     */
    public void clickViewOnTransactionByIndex(int index) {
        // استنى الـ spinner يختفي
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("div.spinner-bk")
                ));

        // استنى الجدول يحمل
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//button[contains(@class,'view')]")
                ));

        // جيب كل أزرار العرض
        List<WebElement> viewButtons = driver.findElements(
                By.xpath("//button[contains(@class,'view') and contains(text(),'عرض')]")
        );

        if (viewButtons.isEmpty()) {
            throw new RuntimeException("No عرض buttons found in inbox table.");
        }

        if (index >= viewButtons.size()) {
            throw new RuntimeException(
                    "Transaction index " + index + " out of range. " +
                            "Found " + viewButtons.size() + " transactions."
            );
        }

        WebElement viewBtn = viewButtons.get(index);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewBtn);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(viewBtn)).click();

        log.info("Clicked عرض on transaction at index {} (TX-{})", index, index + 1);
    }


}
