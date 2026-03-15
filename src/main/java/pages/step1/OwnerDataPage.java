package pages.step1;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 1 — بيانات المالك (Owner Data)
// Step indicator: <div name="ownerData" class="ant-steps-item-active">
// ═══════════════════════════════════════════════════════════════════════════════
public class OwnerDataPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ─── Radio buttons for owner type ────────────────────────────────────────
    // name="ownerData.owner_type" | values: 1=فرد, 2=قطاع حكومي, 3=قطاع خاص, 4=أخرى
    private By radioFard = By.xpath("//input[@name='ownerData.owner_type'][@value='1']");
    private By radioGov = By.xpath("//input[@name='ownerData.owner_type'][@value='2']");
    private By radioPrivate = By.xpath("//input[@name='ownerData.owner_type'][@value='3']");
    private By radioOther = By.xpath("//input[@name='ownerData.owner_type'][@value='4']");

    // ─── Owner search (autocomplete) ─────────────────────────────────────────
    // Search by National ID
    private By ownerSearchField = By.xpath(
            "//div[contains(@class,'owner_ssn')]//input[contains(@class,'ant-select-search__field')]");

    // ─── Add owner button ─────────────────────────────────────────────────────
    // <button name="ownerData.add_owner">
    private By addOwnerBtn = By.xpath("//button[@name='ownerData.add_owner']");
    // Fallback
    private By addOwnerBtnAlt = By.xpath(
            "//button[contains(@class,'add-btnT')][.//text()[contains(.,'اضافة') or contains(.,'إضافة')]]");

    // ─── Owners table ─────────────────────────────────────────────────────────
    private By ownersTableBody = By.xpath(
            "//table[contains(@class,'list_table_edit')]//tbody");
    private By firstOwnerRow = By.xpath(
            "//table[contains(@class,'list_table_edit')]//tbody/tr[1]");

    // ─── Optional checkboxes ──────────────────────────────────────────────────
    private By hasRepresenter = By.xpath("//input[@name='ownerData.has_representer']");
    private By ownerIsDeceased = By.xpath("//input[@name='ownerData.owner_is_deceased']");

    // ─── Navigation buttons ───────────────────────────────────────────────────
    // "التالى" — note: ى not ي
    private By nextBtn = By.xpath(
            "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");
    private By closeBtn = By.xpath(
            "//button[contains(@class,'cancel')][.//span[contains(text(),'إغلاق')]]");
    private By saveDraftBtn = By.xpath(
            "//button[contains(@class,'saveDraft')]");

    // ─── Step indicator ───────────────────────────────────────────────────────
    private By ownerDataStepActive = By.xpath(
            "//div[@name='ownerData'][contains(@class,'ant-steps-item-active')]");

    public OwnerDataPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isOwnerDataTabActive() {
        try {
            return driver.findElement(ownerDataStepActive).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Selects owner type by Arabic label.
     * يختار نوع المالك بالاسم العربي.
     *
     * @param type "فرد" | "قطاع حكومي" | "قطاع خاص" | "أخرى"
     */
    public void selectOwnerType(String type) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@name='ownerData' and contains(@class,'active')]")
        ));
        String value;

        switch (type) {
            case "فرد":
                value = "1";
                break;
            case "قطاع حكومي":
                value = "2";
                break;
            case "قطاع خاص":
                value = "3";
                break;
            default:
                value = "4";
                break;
        }

        By radioLabel = By.xpath("//label[.//span[text()='" + type + "']]");

        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(radioLabel)
        );

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", element
        );

        System.out.println("[INFO] Owner type selected: " + type);
    }
    /**
     * Searches for owner by national ID in autocomplete field.
     * يبحث عن المالك بالرقم القومي في حقل الإكمال التلقائي.
     */
    public void searchOwner(String nationalId) {
        wait.until(ExpectedConditions.elementToBeClickable(ownerSearchField));
        driver.findElement(ownerSearchField).sendKeys(nationalId);

        // Wait for autocomplete dropdown
        By dropdownOption = By.xpath(
                "//li[contains(@class,'ant-select-dropdown-menu-item')]");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOption));
            List<WebElement> options = driver.findElements(dropdownOption);
            if (!options.isEmpty()) {
                options.get(0).click();
                System.out.println("[INFO] Owner selected from autocomplete.");
            }
        } catch (Exception e) {
            System.out.println("[WARN] No autocomplete options found for: " + nationalId);
        }
    }

    /**
     * Clicks the add owner button.
     * يضغط زر إضافة المالك.
     */
    public void clickAddOwner() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(addOwnerBtn));
            driver.findElement(addOwnerBtn).click();
        } catch (Exception e) {
            driver.findElement(addOwnerBtnAlt).click();
        }
        System.out.println("[INFO] Clicked: اضافة (owner)");
    }

    public void fillOwnerData(
            String name,
            String gender,
            String ssn,
            String issuer,
            String phone,
            String mobile,
            String email,
            String address,
            String postalCode
    ) throws InterruptedException {


        // wait modal open
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".ant-modal-content")
        ));

        // name
        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("name")
        ));
        nameInput.clear();
        nameInput.sendKeys(name);

        // gender
        By genderRadio = By.xpath("//label[.//span[text()='" + gender + "']]");
        wait.until(ExpectedConditions.elementToBeClickable(genderRadio)).click();

        // ssn
        WebElement ssnInput = driver.findElement(By.name("ssn"));
        ssnInput.clear();
        ssnInput.sendKeys(ssn);

        // issuer
        WebElement issuerInput = driver.findElement(By.name("nationalid_issuer_name"));
        issuerInput.clear();
        issuerInput.sendKeys(issuer);

        driver.findElement(By.xpath("//label[text()='نوع الهوية']/following::div[contains(@class,'ant-select-selection')][1]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(),'هوية وطنية')]")
        )).click();

        // NATIONALITY
        driver.findElement(By.xpath("//label[text()='الجنسية']/following::div[contains(@class,'ant-select-selection')][1]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(),'سعودي')]")
        )).click();
        // phone
        WebElement phoneInput = driver.findElement(By.name("phone"));
        phoneInput.clear();
        phoneInput.sendKeys(phone);

        // mobile
        WebElement mobileInput = driver.findElement(By.name("mobile"));
        mobileInput.clear();
        mobileInput.sendKeys(mobile);

        // email
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.clear();
        emailInput.sendKeys(email);

// open date picker
        WebElement dob = driver.findElement(By.name("date_of_birth"));
        dob.click();

// select day 10
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@value='10']")
        )).click();

        // address
        WebElement addressInput = driver.findElement(By.name("address"));
        addressInput.clear();
        addressInput.sendKeys(address);

        // postal code
        WebElement postalInput = driver.findElement(By.name("deliverd_post"));
        postalInput.clear();
        postalInput.sendKeys(postalCode);
        String idImagePath = System.getProperty("id.image.path",
                "D:\\المنح الملكية\\FIRSTWORKFLOW USERS.jpg");
        driver.findElement(By.id("file")).sendKeys(idImagePath);
        Thread.sleep(1500);

        System.out.println("[INFO] Owner data filled successfully.");
    }
    public void confirmOwner() {

        By confirmBtn = By.xpath("//button[contains(@class,'btn-success')]");

        WebElement confirm = wait.until(
                ExpectedConditions.visibilityOfElementLocated(confirmBtn)
        );

        // scroll to button
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", confirm
        );

        wait.until(ExpectedConditions.elementToBeClickable(confirm));

        // JS click to avoid interception
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", confirm
        );

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ant-modal-root")
        ));

        System.out.println("[INFO] Owner confirmed and modal closed.");
    }    /**
     * Verifies at least one owner row exists in the table.
     * يتحقق من وجود صف مالك واحد على الأقل في الجدول.
     */
    public boolean isOwnerAdded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstOwnerRow));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Handles the owner validation error if it appears after clicking التالى.
     * لو ظهر error "يرجي مراجعة بيانات المالك"، يضغط تعديل ويكمل البيانات.
     *
     * @return true if error appeared and was handled, false if no error
     */
    public boolean handleOwnerValidationErrorIfPresent(
            String idIssuer,
            String idType,       // e.g. "هوية وطنية"
            String nationality,  // e.g. "سعودي"
            String phone,
            String mobile,
            String email,
            String address,
            String postalCode
    ) throws InterruptedException {

        // Check if ANY owner validation error appeared (wait max 3 seconds)
        By errorToast  = By.xpath("//*[contains(text(),'يرجي مراجعة بيانات المالك')]");
        By errorToast2 = By.xpath("//*[contains(text(),'يجب التأكد من رقم الهاتف مكون من ٩ ارقام')]");

        boolean errorFound;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.visibilityOfElementLocated(errorToast),
                            ExpectedConditions.visibilityOfElementLocated(errorToast2)
                    ));
            errorFound = true;
        } catch (Exception e) {
            errorFound = false;
        }

        if (!errorFound) return false;

        System.out.println("[INFO] Owner validation error detected — clicking تعديل");

        // Click تعديل on the first owner row
        By editBtn = By.xpath(
                "(//button[contains(text(),'تعديل')])[1]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(editBtn)).click();

        // Wait for modal to open
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".ant-modal-content")
        ));

        // Fill missing fields
        WebElement issuerInput = driver.findElement(
                By.name("nationalid_issuer_name"));
        issuerInput.clear();
        issuerInput.sendKeys(idIssuer);

        // نوع الهوية dropdown
        driver.findElement(By.xpath(
                "//label[text()='نوع الهوية']/following::div[contains(@class,'ant-select-selection')][1]"
        )).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(),'" + idType + "')]")
        )).click();

        // الجنسية dropdown
        driver.findElement(By.xpath(
                "//label[text()='الجنسية']/following::div[contains(@class,'ant-select-selection')][1]"
        )).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[contains(text(),'" + nationality + "')]")
        )).click();

        // رقم الهاتف
        WebElement phoneInput = driver.findElement(By.name("phone"));
        phoneInput.clear();
        phoneInput.sendKeys(phone);

        // رقم الجوال
        WebElement mobileInput = driver.findElement(By.name("mobile"));
        mobileInput.clear();
        mobileInput.sendKeys(mobile);

        // البريد الإلكتروني
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.clear();
        emailInput.sendKeys(email);

        // العنوان
        WebElement addressInput = driver.findElement(By.name("address"));
        addressInput.clear();
        addressInput.sendKeys(address);

        // الرقم البريدي
        WebElement postalInput = driver.findElement(By.name("deliverd_post"));
        postalInput.clear();
        postalInput.sendKeys(postalCode);

        // تاريخ الميلاد — HijriDatePicker (readonly input, opens popover on click)
        By dobField = By.xpath("//input[@name='date_of_birth']");
        WebElement dobInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(dobField));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", dobInput);
        dobInput.click();

// Wait for THIS picker's popover to open
        By yearSelect = By.cssSelector(
                ".ant-popover:not(.ant-popover-hidden) select.YearsList__YearSelect-sc-1ovp38j-1");
        wait.until(ExpectedConditions.presenceOfElementLocated(yearSelect));

        new Select(driver.findElement(yearSelect)).selectByValue("1410"); // السنة

        By monthSelect = By.cssSelector(
                ".ant-popover:not(.ant-popover-hidden) select.MonthsList__MonthSelect-sc-17ilzvv-1");
        new Select(driver.findElement(monthSelect)).selectByValue("1"); // محرم = 1

        By dayBtn = By.cssSelector(
                ".ant-popover:not(.ant-popover-hidden) button.MonthDaysView__MonthDayButton-sc-zj9qw0-2[value='10']");
        wait.until(ExpectedConditions.elementToBeClickable(dayBtn)).click();


        System.out.println("[INFO] Date of birth selected.");

// صورة الهوية — hidden file input, use sendKeys directly (no click on button)
        By fileInput = By.cssSelector("input[type='file'][id='file']");
        WebElement fileEl = driver.findElement(fileInput);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.display='block';", fileEl);
        String idImagePath = System.getProperty("id.image.path",
                "D:\\المنح الملكية\\FIRSTWORKFLOW USERS.jpg");
        fileEl.sendKeys(idImagePath);

        Thread.sleep(1000); // انتظر رفع الملف
        System.out.println("[INFO] ID image uploaded.");
        Thread.sleep(500);

        // Click نعم to confirm
        By yesBtn = By.xpath("//button[.//span[text()='نعم'] or text()='نعم']");
        wait.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();

        // Wait for modal to close
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".ant-modal-root")
        ));



        System.out.println("[INFO] Owner data completed and modal closed.");
        return true;
    }


    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        driver.findElement(nextBtn).click();
        System.out.println("[INFO] Clicked: التالى (Tab 1 → Tab 2)");
    }


}
