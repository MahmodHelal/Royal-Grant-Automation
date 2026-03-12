package pages.step2;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class Step2Pages {

    // ═══════════════════════════════════════════════════════════════════════════
    // TAB 1 — الملخص (Summary - read only)
    // ═══════════════════════════════════════════════════════════════════════════
    public static class Step2SummaryTab {
        private final WebDriver driver;
        private final WebDriverWait wait;

        private final By nextBtn = By.xpath(
                "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");

        public Step2SummaryTab(WebDriver driver) {
            this.driver = driver;
            this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        public boolean isSummaryTabDisplayed() {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(text(),'بيانات المالك') or contains(text(),'الملاك')]")));
                return true;
            } catch (Exception e) { return false; }
        }

        public void clickNext() {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("[INFO] Step2: Clicked التالى (Summary → File Data)");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TAB 2 — بيانات فتح الملف
    // ═══════════════════════════════════════════════════════════════════════════
    public static class FileOpeningDataTab {
        private final WebDriver driver;
        private final WebDriverWait wait;

        // ─── Editable text fields ─────────────────────────────────────────────
        private final By fileNoField            = By.xpath("//input[@name='file_opening_data.file_no']");
        private final By fileOpeningDateField   = By.xpath("//input[@name='file_opening_data.file_opening_date']");
        private final By orderSourceField       = By.xpath("//input[@name='file_opening_data.order_samy_source']");
        private final By hafeezaNumberField     = By.xpath("//input[@name='file_opening_data.hafeeza_number']");
        private final By hafeezaDateField       = By.xpath("//input[@name='file_opening_data.hafeeza_date']");
        private final By hafeezaSourceField     = By.xpath("//input[@name='file_opening_data.hafeeza_source']");
        private final By customizationNoField   = By.xpath("//input[@name='file_opening_data.customization_number']");
        private final By customizationDateField = By.xpath("//input[@name='file_opening_data.customization_date']");
        private final By operationNoField       = By.xpath("//input[@name='file_opening_data.operation_no']");
        private final By operationDateField     = By.xpath("//input[@name='file_opening_data.operation_date']");
        private final By sequenceNoField        = By.xpath("//input[@name='file_opening_data.sequence_no']");
        private final By sequenceDateField      = By.xpath("//input[@name='file_opening_data.sequence_date']");

        // ─── Classification section ───────────────────────────────────────────
        private final By classificationNotesField = By.xpath(
                "//textarea[@name='grants_royal_area_classes.classification_notes']");
        private final By exceptionCheckbox = By.xpath(
                "//input[@name='grants_royal_area_classes.exception']");

        // ─── نوع المنحة الملكية — ant-select ─────────────────────────────────
        private final By grantTypeDropdown = By.xpath(
                "//div[contains(@class,'addTranDiv')]//div[contains(@class,'ant-select-selection--single')]");

        // ─── Navigation ───────────────────────────────────────────────────────
        private final By nextBtn = By.xpath(
                "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");
        private final By saveEditsBtn = By.xpath(
                "//button[contains(@class,'saveEdits') or contains(@class,'editsSaave')]" +
                        "[.//span[contains(text(),'حفظ التعديلات')]]");

        public FileOpeningDataTab(WebDriver driver) {
            this.driver = driver;
            this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        // ─── Text field fillers ───────────────────────────────────────────────
        public void fillFileNumber(String value) {
            fillText(fileNoField, value);
            System.out.println("[INFO] Filled رقم الملف: " + value);
        }

        public void fillOrderSource(String value) {
            fillText(orderSourceField, value);
            System.out.println("[INFO] Filled مصدر الأمر السامي: " + value);
        }

        public void fillHafeezaNumber(String value) {
            fillText(hafeezaNumberField, value);
            System.out.println("[INFO] Filled رقم الحفيظة: " + value);
        }

        public void fillHafeezaSource(String value) {
            fillText(hafeezaSourceField, value);
            System.out.println("[INFO] Filled مصدر الحفيظة: " + value);
        }

        public void fillCustomizationNumber(String value) {
            fillText(customizationNoField, value);
            System.out.println("[INFO] Filled رقم قرار التخصيص: " + value);
        }

        public void fillOperationNo(String value) {
            fillText(operationNoField, value);
            System.out.println("[INFO] Filled رقم عملية قرار التخصيص: " + value);
        }

        public void fillSequenceNo(String value) {
            fillText(sequenceNoField, value);
            System.out.println("[INFO] Filled رقم تسلسل قرار التخصيص: " + value);
        }

        public void fillClassificationNotes(String value) {
            WebElement ta = wait.until(ExpectedConditions.visibilityOfElementLocated(classificationNotesField));
            ta.clear();
            ta.sendKeys(value);
            System.out.println("[INFO] Filled ملاحظات على التصنيف: " + value);
        }

        // ─── Date pickers ─────────────────────────────────────────────────────
        public void selectFileOpeningDate(String hijriDate) {
            selectHijriDate(fileOpeningDateField, hijriDate);
            System.out.println("[INFO] Selected تاريخ فتح الملف: " + hijriDate);
        }

        public void selectHafeezaDate(String hijriDate) {
            selectHijriDate(hafeezaDateField, hijriDate);
            System.out.println("[INFO] Selected تاريخ الحفيظة: " + hijriDate);
        }

        public void selectCustomizationDate(String hijriDate) {
            selectHijriDate(customizationDateField, hijriDate);
            System.out.println("[INFO] Selected تاريخ قرار التخصيص: " + hijriDate);
        }

        public void selectOperationDate(String hijriDate) {
            selectHijriDate(operationDateField, hijriDate);
            System.out.println("[INFO] Selected تاريخ عملية قرار التخصيص: " + hijriDate);
        }

        public void selectSequenceDate(String hijriDate) {
            selectHijriDate(sequenceDateField, hijriDate);
            System.out.println("[INFO] Selected تاريخ تسلسل قرار التخصيص: " + hijriDate);
        }

        // ─── Grant type dropdown (نوع المنحة الملكية) ─────────────────────────
        public void selectGrantType(String optionText) {
            By dropdownLocator = By.xpath(
                    "//div[contains(@class,'addTranDiv')]" +
                            "//div[contains(@class,'ant-select-selection--single')]"
            );

            WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(dropdownLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);

            // Wait for dropdown to be visible (not hidden)
            By menuLocator = By.cssSelector(
                    ".ant-select-dropdown:not(.ant-select-dropdown-hidden) .ant-select-dropdown-menu"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(menuLocator));

            // Find and click the option — skip disabled first item
            By optionLocator = By.xpath(
                    "//div[contains(@class,'ant-select-dropdown') and not(contains(@class,'ant-select-dropdown-hidden'))]" +
                            "//li[contains(@class,'ant-select-dropdown-menu-item')" +
                            " and not(contains(@class,'ant-select-dropdown-menu-item-disabled'))" +
                            " and normalize-space(text())='" + optionText + "']"
            );

            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            System.out.println("[INFO] Selected نوع المنحة الملكية: " + optionText);
        }        // ─── Navigation ───────────────────────────────────────────────────────
        public void clickNext() {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("[INFO] Step2: Clicked التالى (File Data → Notes)");
        }

        public void clickSaveEdits() {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveEditsBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("[INFO] Clicked: حفظ التعديلات");
        }

        // ─── Hijri date picker helper ─────────────────────────────────────────
        /**
         * Opens a HijriDatePicker and selects the given date.
         * @param fieldLocator  the By locator of the readonly date input
         * @param hijriDate     format "YYYY/MM/DD" e.g. "1447/01/10"
         */
        private void selectHijriDate(By fieldLocator, String hijriDate) {
            String[] parts = hijriDate.split("/");
            String year  = parts[0].trim();
            int    month = Integer.parseInt(parts[1].trim()) - 1;
            String day   = String.valueOf(Integer.parseInt(parts[2].trim()));

            // Close any open picker first by clicking elsewhere
            try {
                WebElement body = driver.findElement(By.tagName("body"));
                body.click();
                Thread.sleep(300);
            } catch (Exception ignored) {}

            // Open this picker
            WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fieldLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
            input.click();

            // Wait for THIS picker to open — scope to the open popover only
            By yearSelectLocator = By.cssSelector(
                    ".ant-popover:not(.ant-popover-hidden) select.YearsList__YearSelect-sc-1ovp38j-1"
            );
            wait.until(ExpectedConditions.presenceOfElementLocated(yearSelectLocator));

            // Select year
            new Select(driver.findElement(yearSelectLocator)).selectByValue(year);

            // Select month
            By monthSelectLocator = By.cssSelector(
                    ".ant-popover:not(.ant-popover-hidden) select.MonthsList__MonthSelect-sc-17ilzvv-1"
            );
            new Select(driver.findElement(monthSelectLocator))
                    .selectByValue(String.valueOf(month));

            // Wait for days to re-render then click day — scoped to open popover
            By dayBtn = By.cssSelector(
                    ".ant-popover:not(.ant-popover-hidden) button.MonthDaysView__MonthDayButton-sc-zj9qw0-2[value='" + day + "']"
            );
            wait.until(ExpectedConditions.elementToBeClickable(dayBtn)).click();

            System.out.println("[INFO] HijriDate selected: " + hijriDate +
                    " (year=" + year + ", month0=" + month + ", day=" + day + ")");
        }
        // ─── Internal helper ─────────────────────────────────────────────────
        private void fillText(By locator, String value) {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            el.clear();
            el.sendKeys(value);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TAB 3 — الملاحظات
    // ═══════════════════════════════════════════════════════════════════════════
    public static class Step2NotesTab {
        private final WebDriver driver;
        private final WebDriverWait wait;

        private final By notesTextarea = By.xpath(
                "//textarea[@placeholder='ملاحظات' or contains(@name,'remark') or contains(@name,'notes')]");

        private final By saveEditsBtn = By.xpath(
                "//button[contains(@class,'saveEdits') or contains(@class,'editsSaave')]" +
                        "[.//span[contains(text(),'حفظ التعديلات')]]");

        private final By redirectDropdown = By.xpath(
                "//div[contains(@class,'buttonSelect')]" +
                        "//div[contains(@class,'ant-select-selection--single')]");

        public Step2NotesTab(WebDriver driver) {
            this.driver = driver;
            this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        public void fillNotes(String notes) {
            WebElement ta = wait.until(ExpectedConditions.visibilityOfElementLocated(notesTextarea));
            ta.clear();
            ta.sendKeys(notes);
            System.out.println("[INFO] Step2 Notes filled: " + notes);
        }

        public void clickSaveEdits() {
            WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveEditsBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("[INFO] Clicked: حفظ التعديلات (Step 2 Notes)");
        }

        /**
         * Selects a forwarding option from the "اعادة توجيه" dropdown.
         * @param optionText e.g. "مدير إدارة المنح الملكية"
         */
        public void selectRedirectOption(String optionText) {
            WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(redirectDropdown));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dd);
            dd.click();

            By option = By.xpath(
                    "//li[contains(@class,'ant-select-dropdown-menu-item') and normalize-space(text())='" + optionText + "']");
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            System.out.println("[INFO] Selected اعادة توجيه option: " + optionText);
        }
    }
}