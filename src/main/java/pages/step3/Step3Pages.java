package pages.step3;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Step3Pages — مدير إدارة المنح الملكية
 *
 * Wizard triggered by: عرض button (NO متابعة button in manager inbox)
 *
 * From breadcrumb: المنح الملكية ← طلب منح ملكية ← ١٤٤٧/٦٠ ← انهاء المعاملة لإجراء القرعة الالكترونية
 *
 * Wizard tabs (2 tabs):
 *   Tab 1 (step 1): الملخص — read-only summary with owner data tree
 *   Tab 2 (step 2): الملاحظات — notes textarea + file upload
 *
 * Bottom buttons:
 *   - التالى, إغلاق, حفظ التعديلات
 *   - اعادة توجيه (bottom right dropdown — ant-select)
 *
 * Success message: "تم انهاء المعاملة رقم ١٤٤٧ / ٦٠"
 */
public class Step3Pages {

    // ═══════════════════════════════════════════════════════════════════════════
    // TAB 1 — الملخص (Summary - read only)
    // Identical structure to Step 2 summary tab
    // All data is read-only: owner data tree on right, details on left
    // ═══════════════════════════════════════════════════════════════════════════

    public static class Step3SummaryTab {
        private WebDriver driver;
        private WebDriverWait wait;

        // Read-only owner data visible in summary
        private By ownerNameLabel = By.xpath(
            "//*[contains(text(),'الإسم') or contains(text(),'اسم المالك')]" +
            "/following-sibling::*[1]");

        // Breadcrumb confirms we're in انهاء المعاملة
        private By breadcrumbStep = By.xpath(
            "//*[contains(text(),'انهاء المعاملة لإجراء القرعة')]");

        // Tree navigation items (right panel)
        private By treeOwnerData = By.xpath(
            "//*[contains(text(),'بيانات المالك')]");
        private By treeOrderData = By.xpath(
            "//*[contains(text(),'بيانات الأمر السامي')]");

        private By nextBtn = By.xpath(
            "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");

        public Step3SummaryTab(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        public boolean isSummaryDisplayed() {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(treeOwnerData));
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * Verifies owner data is visible in the summary.
         * يتحقق من ظهور بيانات المالك في الملخص.
         */
        public boolean isOwnerDataVisible() {
            try {
                return driver.findElement(
                    By.xpath("//*[contains(text(),'نوع المالك') or contains(text(),'رقم الهوية')]")
                ).isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        public void clickNext() {
            wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            driver.findElement(nextBtn).click();
            System.out.println("[INFO] Step3: Clicked التالى (Summary → Notes)");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TAB 2 — الملاحظات (Notes)
    // From screenshot: simple textarea + file upload icon
    // NO "اضافة" row button (different from Step 1 notes)
    // ═══════════════════════════════════════════════════════════════════════════

    public static class Step3NotesTab {
        private WebDriver driver;
        private WebDriverWait wait;

        // ملاحظات textarea — simple single textarea
        // From screenshot: placeholder="ملاحظات"
        private By notesTextarea = By.xpath(
            "//textarea[@placeholder='ملاحظات']");

        // Fallback
        private By notesTextareaAlt = By.xpath(
            "//label[contains(text(),'ملاحظات')]/following::textarea[1]");

        // File upload area (مرفقات) — upload icon visible in screenshot
        // Not interacted in standard E2E test
        private By fileUploadArea = By.xpath(
            "//input[@type='file']");

        // ─── Navigation & actions ─────────────────────────────────────────────
        // حفظ التعديلات — saves and completes
        private By saveEditsBtn = By.xpath(
            "//button[contains(@class,'saveEdits') or contains(@class,'editsSaave')]" +
            "[.//span[contains(text(),'حفظ التعديلات')]]");

        // اعادة توجيه — redirect dropdown (bottom right)
        // From DOM: class="buttonSelect ant-select ant-select-enabled ant-select-allow-clear ant-select-no-arrow"
        // placeholder: "اعادة توجيه"
        private By redirectDropdown = By.xpath(
            "//div[contains(@class,'buttonSelect')]" +
            "//div[contains(@class,'ant-select-selection--single')]");

        private String redirectOptionXpath =
            "//li[@role='option'][contains(text(),'%s')]";

        // إغلاق button
        private By closeBtn = By.xpath(
            "//button[contains(@class,'cancel')][.//span[contains(text(),'إغلاق')]]");

        public Step3NotesTab(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        public void fillNotes(String notes) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(notesTextarea));
                driver.findElement(notesTextarea).clear();
                driver.findElement(notesTextarea).sendKeys(notes);
            } catch (Exception e) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(notesTextareaAlt));
                driver.findElement(notesTextareaAlt).clear();
                driver.findElement(notesTextareaAlt).sendKeys(notes);
            }
            System.out.println("[INFO] Step3: Notes filled.");
        }

        /**
         * Clicks "حفظ التعديلات" to finalize the transaction.
         * يضغط "حفظ التعديلات" لإنهاء المعاملة.
         * Expected success: "تم انهاء المعاملة رقم ..."
         */
        public void clickSaveEdits() {
            wait.until(ExpectedConditions.elementToBeClickable(saveEditsBtn));
            driver.findElement(saveEditsBtn).click();
            System.out.println("[INFO] Clicked: حفظ التعديلات (Step 3 — finalize)");
        }

        /**
         * Selects "إعادة توجيه" option from the redirect dropdown if needed.
         * يختار خيار إعادة توجيه من القائمة المنسدلة إذا لزم الأمر.
         *
         * @param option e.g. "إعادة توجيه" or another actor name
         */
        public void selectRedirectOption(String option) {
            wait.until(ExpectedConditions.elementToBeClickable(redirectDropdown));
            driver.findElement(redirectDropdown).click();

            By optionLocator = By.xpath(String.format(redirectOptionXpath, option));
            wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            driver.findElement(optionLocator).click();
            System.out.println("[INFO] Redirect option selected: " + option);
        }
    }
}
