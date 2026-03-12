package pages.step1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 2 — بيانات الأمر السامي (Royal Decree Data)
// Step indicator: <div name="order_data" class="ant-steps-item-active">
// ═══════════════════════════════════════════════════════════════════════════════
public class RoyalDecreePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // رقم الأمر السامي — free text input
    private By decreeNumberField = By.xpath(
            "//input[@name='order_samy_data.order_samy_number']");

    // تاريخ الأمر السامي — readonly, opens HijriDatePicker on click
    private By decreeDateField = By.xpath(
            "//input[@name='order_samy_data.order_samy_date']");

    // ─── HijriDatePicker popup ────────────────────────────────────────────────
    private By datePickerContainer = By.xpath(
            "//*[contains(@class,'HijriDatePicker') or contains(@class,'hijri-date')]");
    private By datePickerYearSelect = By.xpath(
            "//select[contains(@class,'YearSelect')]");
    private By datePickerMonthSelect = By.xpath(
            "//select[contains(@class,'MonthSelect')]");
    // Day buttons: <button value="1" class="MonthDayButton">
    private String datePickerDayBtnXpath =
            "//button[@value='%s' and contains(@class,'MonthDayButton')]";

    // ─── مساحة المنحة — Ant Design dropdown (NOT a text input) ───────────────
    // NOTE: This is an ant-select dropdown, NOT an input field
    // ملاحظة: هذا dropdown وليس حقل نص
    private By areaDropdown = By.xpath(
            "//div[not(contains(@class,'addTranDiv'))]" +
                    "//div[contains(@class,'ant-select-selection--single')]" +
                    "[following::label[contains(text(),'مساحة')] or " +
                    "preceding::label[contains(text(),'مساحة')]]");

    // Fallback area dropdown — find by position in wizard step 2
    private By areaDropdownAlt = By.xpath(
            "//div[@name='order_data' or ancestor::div[@name='order_data']]" +
                    "//div[contains(@class,'ant-select-selection--single')]");

    // Area dropdown option by text
    private String areaOptionXpath =
            "//li[@role='option'][contains(text(),'%s')]";

    // ─── Navigation ───────────────────────────────────────────────────────────
    private By nextBtn = By.xpath(
            "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");
    private By prevBtn = By.xpath(
            "//button[contains(@class,'prev')][.//span[contains(text(),'السابق')]]");

    public RoyalDecreePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillDecreeNumber(String number) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(decreeNumberField));
        driver.findElement(decreeNumberField).clear();
        driver.findElement(decreeNumberField).sendKeys(number);
    }

    /**
     * Selects the Hijri date by clicking the readonly date field
     * and choosing year/month/day from the popup.
     * <p>
     * يختار التاريخ الهجري بالضغط على حقل التاريخ readonly
     * واختيار السنة/الشهر/اليوم من النافذة المنبثقة.
     *
     * @param hijriDate format: "YYYY/MM/DD" e.g. "1447/01/09"
     */
    public void selectDecreeDate(String hijriDate) {

        String[] parts = hijriDate.split("/");
        String day = String.valueOf(Integer.parseInt(parts[2])); // remove leading zero

        WebElement dateInput = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.name("order_samy_data.order_samy_date")
                )
        );

        dateInput.click();

        By dayBtn = By.xpath("//button[@value='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayBtn)).click();

        System.out.println("[INFO] Decree date selected: " + hijriDate);
    }
    /**
     * Selects the grant area from dropdown.
     * يختار مساحة المنحة من القائمة المنسدلة.
     * <p>
     * Options: "مساحة ٢٠٠ × ٢٠٠" | "مساحة ١٠٠ × ١٠٠" | "مساحة ٥٠ × ٥٠" | etc.
     */
    public void selectGrantArea(String areaText) {
        // Click dropdown to open
        try {
            wait.until(ExpectedConditions.elementToBeClickable(areaDropdown));
            driver.findElement(areaDropdown).click();
        } catch (Exception e) {
            driver.findElement(areaDropdownAlt).click();
        }

        // Select option
        By option = By.xpath(String.format(areaOptionXpath, areaText));
        wait.until(ExpectedConditions.visibilityOfElementLocated(option));
        driver.findElement(option).click();
        System.out.println("[INFO] Grant area selected: " + areaText);
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        driver.findElement(nextBtn).click();
        System.out.println("[INFO] Clicked: التالى (Tab 2 → Tab 3)");
    }
}
