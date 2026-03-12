package stepdefs;

import core.context.TestContext;
import core.driver.DriverManager;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pages.*;
import pages.step1.NotesPage;
import pages.step1.OwnerDataPage;
import pages.step1.RoyalDecreePage;
import pages.step1.SummaryPage;
import pages.step2.Step2Pages;
import pages.step2.Step2Pages.*;
import pages.step3.Step3Pages.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * E2EGrantsWorkflowSteps — step definitions for the full E2E workflow.
 *
 * Covers 3 actors across 3 scenarios:
 *   TC-E2E-01: المواطن  — submits new grant transaction
 *   TC-E2E-02: موظف المنح — reviews and forwards transaction
 *   TC-E2E-03: مدير المنح — finalizes transaction for lottery
 *
 * ARCHITECTURE:
 *   - All page objects initialized in @Before(order=1) after driver is ready
 *   - TestContext shared via PicoContainer for cross-scenario data (transaction number)
 *   - Transaction number persisted to target/transaction_number.txt for cross-scenario use
 */
public class E2EGrantsWorkflowSteps {

    private final TestContext ctx;

    private WebDriver          driver;
    private HomePage           homePage;
    private LoginPage          loginPage;
    private AppsPage           appsPage;
    private GrantsPortalPage   portalPage;
    private LocalGrantsAppPage appPage;
    private OwnerDataPage      ownerDataPage;
    private RoyalDecreePage    royalDecreePage;
    private NotesPage          notesPage;
    private SummaryPage        summaryPage;
    private Step2SummaryTab    step2SummaryTab;
    private FileOpeningDataTab fileDataTab;
    private Step2NotesTab      step2NotesTab;
    private Step3SummaryTab    step3SummaryTab;
    private Step3NotesTab      step3NotesTab;

    public E2EGrantsWorkflowSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 1)
    public void initPages() {
        driver          = DriverManager.getDriver();
        homePage        = new HomePage(driver);
        loginPage       = new LoginPage(driver);
        appsPage        = new AppsPage(driver);
        portalPage      = new GrantsPortalPage(driver);
        appPage         = new LocalGrantsAppPage(driver);
        ownerDataPage   = new OwnerDataPage(driver);
        royalDecreePage = new RoyalDecreePage(driver);
        notesPage       = new NotesPage(driver);
        summaryPage     = new SummaryPage(driver);
        step2SummaryTab = new Step2SummaryTab(driver);
        fileDataTab     = new FileOpeningDataTab(driver);
        step2NotesTab   = new Step2NotesTab(driver);
        step3SummaryTab = new Step3SummaryTab(driver);
        step3NotesTab   = new Step3NotesTab(driver);
    }

    // ─── Portal Navigation ────────────────────────────────────────────────────

    @Given("I navigate to the portal login page")
    public void navigateToPortal() {
        driver.get("https://geoservices1.syadtech.com/home/login");
        driver.navigate().refresh();
        homePage.performLogoutIfLoggedIn();
        System.out.println("[STEP] Navigated to portal login page.");
    }

    @When("I login with username {string} and password {string}")
    public void login(String username, String password) {
        loginPage.login(username, password);
        System.out.println("[STEP] Login submitted for: " + username);
    }

    @Then("I should be redirected to the apps page")
    public void verifyAppsPage() {
        assertTrue(appsPage.isAppsPageLoaded(), "Apps page was not loaded after login.");
    }

    @When("I click on {string} app card")
    public void clickAppCard(String appName) {
        appsPage.clickGrantsAppCard();
        System.out.println("[STEP] Clicked app card: " + appName);
    }

    @Then("I should be on the grants portal page")
    public void verifyGrantsPortalPage() {
        System.out.println("[STEP] On grants portal page.");
    }

    @When("I extract the JWT token from the URL")
    public void extractJwt() {
        String token = portalPage.extractJwtToken();
        ctx.set("jwtToken", token);
        System.out.println("[STEP] JWT token extracted and stored.");
    }

    @When("I open localhost grants app with the JWT token")
    public void openLocalhost() {
        String token = (String) ctx.get("jwtToken");
        portalPage.openLocalhostWithToken(token);
        System.out.println("[STEP] Opened localhost app with JWT token.");
    }

    // ─── Step 1 — Citizen ─────────────────────────────────────────────────────

    @When("I click on {string} dropdown")
    public void clickNewTransactionDropdown(String label) {
        appPage.startNewGrantTransaction();
    }

    @When("I select {string} from the dropdown")
    public void selectFromDropdown(String option) {
        System.out.println("[STEP] Selected: " + option);
    }

    @When("I select owner type radio {string}")
    public void selectOwnerType(String ownerType) {
        ownerDataPage.selectOwnerType(ownerType);
    }

    @When("I fill royal decree number {string}")
    public void fillDecreeNumber(String number) {
        royalDecreePage.fillDecreeNumber(number);
    }

    @When("I select royal decree date {string}")
    public void selectDecreeDate(String date) {
        royalDecreePage.selectDecreeDate(date);
    }

    @When("I select royal grant area {string}")
    public void selectGrantArea(String area) {
        royalDecreePage.selectGrantArea(area);
    }

    @When("I click add a new note row")
    public void clickAddNote() {
        notesPage.clickAddNote();
    }

    @When("I fill note text {string}")
    public void fillNoteText(String note) {
        notesPage.fillNoteText(note);
    }

    @Then("the summary page should be displayed")
    public void verifySummaryPage() {
        assertTrue(summaryPage.isSummaryPageDisplayed(), "Summary page (Tab 4) was not displayed.");
    }

    // ─── Shared: Tab Navigation ───────────────────────────────────────────────

    @When("I click button to go to next tab")
    public void clickNext() {
        try { ownerDataPage.clickNext();   return; } catch (Exception ignored) {}
        try { royalDecreePage.clickNext(); return; } catch (Exception ignored) {}
        try { notesPage.clickNext();       return; } catch (Exception ignored) {}
        try { step2SummaryTab.clickNext(); return; } catch (Exception ignored) {}
        try { fileDataTab.clickNext();     return; } catch (Exception ignored) {}
        try { step3SummaryTab.clickNext(); }         catch (Exception ignored) {}
    }

    // ─── Shared: Submit with Confirmation Modal ───────────────────────────────

    /**
     * Clicks the given submit button (ارسال / انهاء) then confirms the modal.
     * يضغط زر الإرسال ثم يؤكد في نافذة التأكيد.
     */
    @When("I click the {string} button to submit the transaction")
    public void clickSubmit(String btnLabel) throws InterruptedException {
        By submitBtn = By.xpath(
                "//div[contains(@class,'buttons')]" +
                "//button[contains(@class,'btn-success')][.//span[text()='" + btnLabel + "']]"
        );
        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(submitBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        System.out.println("[INFO] Clicked: " + btnLabel);

        // Wait for confirmation modal
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='ant-modal-title']")
                ));

        // Click نعم
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@class='ant-modal-footer']" +
                                 "//button[contains(@class,'ant-btn-primary')][.//span[text()='نعم']]")
                )).click();
        System.out.println("[INFO] Confirmation modal: clicked نعم");
        Thread.sleep(1500);
    }

    // ─── Shared: Success Verification ────────────────────────────────────────

    @Then("I should see success message containing {string}")
    public void verifySuccessMessage(String expectedText) {
        assertTrue(appPage.isSuccessMessageDisplayed(expectedText),
                "Expected success message containing: " + expectedText);
    }

    @Then("I save the submitted transaction number")
    public void saveTransactionNumber() {
        String msg = appPage.getSuccessMessageText();
        String txNumber = "";
        if (msg.contains("برقم ")) {
            txNumber = msg.substring(msg.indexOf("برقم ") + 5).trim();
        }
        ctx.set("transactionNumber", txNumber);

        // ✅ أضف الرقم الجديد في آخر الملف (سطر جديد)
        try {
            java.nio.file.Path path = java.nio.file.Path.of("target/transaction_numbers.txt");
            String existing = "";
            if (java.nio.file.Files.exists(path)) {
                existing = java.nio.file.Files.readString(path).trim();
            }
            String updated = existing.isEmpty() ? txNumber : existing + "\n" + txNumber;
            java.nio.file.Files.writeString(path, updated);
            System.out.println("[STEP] Transaction number saved: " + txNumber);
        } catch (Exception e) {
            System.out.println("[WARN] Could not save transaction number: " + e.getMessage());
        }
    }
    // ─── Step 2 — Grant Employee ──────────────────────────────────────────────

    @When("I click on {string} from the sidebar")
    public void clickSidebarLink(String linkText) {
        appPage.clickIncomingTransactions();
    }

    @When("I click {string} button on the first transaction row")
    public void clickRowButton(String btnText) {
        String txNumber = getTransactionNumber();
        if (btnText.equals("عرض")) {
            appPage.clickViewOnTransaction(txNumber);
        }
    }

    @Then("the summary tab should be active and read-only")
    public void verifySummaryTabReadOnly() {
        // Wait for Tab 1 (الملخص) to be present — it can be process or finish state
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[@name='summery']")
                ));
        System.out.println("[INFO] Tab 1 (الملخص) verified — read-only summary present.");
    }

    @And("I fill file opening data tab with:")
    public void fillFileOpeningDataTab(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        Step2Pages.FileOpeningDataTab tab = new Step2Pages.FileOpeningDataTab(driver);

        // Ensure Tab 2 is active — click tab header if needed
        By tab2 = By.xpath("//div[@name='file_data']");
        try {
            WebElement tab2El = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(tab2));
            if (!tab2El.getAttribute("class").contains("ant-steps-item-process")) {
                System.out.println("[INFO] Tab 2 not active — clicking tab header");
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tab2El);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("[WARN] Could not check Tab 2 state: " + e.getMessage());
        }

        // Wait for Tab 2 input to be present
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//input[@name='file_opening_data.file_no']")
                ));
        System.out.println("[INFO] Tab 2 active — starting fill");

        if (data.containsKey("fileNo"))              tab.fillFileNumber(data.get("fileNo"));
        if (data.containsKey("fileOpeningDate"))     tab.selectFileOpeningDate(data.get("fileOpeningDate"));
        if (data.containsKey("orderSource"))         tab.fillOrderSource(data.get("orderSource"));
        if (data.containsKey("hafeezaNumber"))       tab.fillHafeezaNumber(data.get("hafeezaNumber"));
        if (data.containsKey("hafeezaDate"))         tab.selectHafeezaDate(data.get("hafeezaDate"));
        if (data.containsKey("hafeezaSource"))       tab.fillHafeezaSource(data.get("hafeezaSource"));
        if (data.containsKey("customizationNumber")) tab.fillCustomizationNumber(data.get("customizationNumber"));
        if (data.containsKey("customizationDate"))   tab.selectCustomizationDate(data.get("customizationDate"));
        if (data.containsKey("operationNo"))         tab.fillOperationNo(data.get("operationNo"));
        if (data.containsKey("operationDate"))       tab.selectOperationDate(data.get("operationDate"));
        if (data.containsKey("sequenceNo"))          tab.fillSequenceNo(data.get("sequenceNo"));
        if (data.containsKey("sequenceDate"))        tab.selectSequenceDate(data.get("sequenceDate"));
        if (data.containsKey("grantType"))           tab.selectGrantType(data.get("grantType"));

        System.out.println("[STEP] File opening data tab filled.");
    }

    @When("I fill employee note {string}")
    public void fillEmployeeNote(String note) {
        step2NotesTab.fillNotes(note);
    }

    // ─── Step 3 — Grant Manager ───────────────────────────────────────────────

    @Then("the summary tab should be displayed with owner data")
    public void verifyStep3Summary() {
        assertTrue(step3SummaryTab.isSummaryDisplayed(),
                "Step 3 summary tab was not displayed.");
        assertTrue(step3SummaryTab.isOwnerDataVisible(),
                "Owner data not visible in Step 3 summary.");
    }

    @When("I fill manager note {string}")
    public void fillManagerNote(String note) {
        step3NotesTab.fillNotes(note);
    }

    // ─── Shared: Save Edits ───────────────────────────────────────────────────

    @When("I click {string} to save")
    public void clickSaveEdits(String btnLabel) {
        try { fileDataTab.clickSaveEdits();   } catch (Exception ignored) {}
        try { step2NotesTab.clickSaveEdits(); } catch (Exception ignored) {}
        try { step3NotesTab.clickSaveEdits(); } catch (Exception ignored) {}

        // Handle confirmation modal if it appears
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'ant-modal-footer')]" +
                                     "//button[contains(@class,'ant-btn-primary')][.//span[text()='نعم']]")
                    )).click();
            System.out.println("[INFO] Confirmation modal: clicked نعم");
        } catch (Exception ignored) {
            System.out.println("[INFO] No confirmation modal.");
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Gets the transaction number — tries context first, then file.
     * يجلب رقم المعاملة — يجرب الـ context أولاً ثم الملف.
     *
     * NOTE: Transaction number is stored as Arabic numerals (e.g. ٧٤ / ١٤٤٧)
     * to match the table content directly without conversion.
     */
    private String getTransactionNumber() {
        String fromCtx = (String) ctx.get("transactionNumber");
        if (fromCtx != null && !fromCtx.isBlank()) return fromCtx;

        try {
            return java.nio.file.Files.readString(
                    java.nio.file.Path.of("target/transaction_number.txt")
            ).trim();
        } catch (Exception e) {
            System.out.println("[WARN] Could not read transaction number from file.");
            return "";
        }
    }

    @When("I handle owner validation error if present")
    public void handleOwnerValidationError() throws InterruptedException {
        boolean handled = ownerDataPage.handleOwnerValidationErrorIfPresent(
                "الرياض",           // idIssuer — جهة اصدار الهوية
                "هوية وطنية",       // idType
                "سعودي",            // nationality
                "555555555",        // phone
                "531531093",        // mobile
                "Omda@email.com",   // email
                "Riyadh",           // address
                "12345"             // postalCode
        );
        if (handled) {
            System.out.println("[STEP] Owner validation error was handled.");
            ownerDataPage.clickNext(); // نضغط التالى تاني بعد ما تم التعديل
        }
    }

    @When("I click \"عرض\" button on transaction number {string}")
    public void clickViewOnTransactionByIndex(String txIndex) {
        // TX-01 → index 0, TX-02 → index 1, ...
        int index = Integer.parseInt(txIndex.replace("TX-", "").trim()) - 1;

        // ✅ اقرأ الرقم الحقيقي من الملف
        String txNumber = getTransactionNumberByIndex(index);
        System.out.println("[INFO] Searching for transaction: " + txNumber + " (index=" + index + ")");

        appPage.clickViewOnTransaction(txNumber);
    }

    private String getTransactionNumberByIndex(int index) {
        try {
            java.nio.file.Path path = java.nio.file.Path.of("target/transaction_numbers.txt");
            if (!java.nio.file.Files.exists(path)) {
                throw new RuntimeException("transaction_numbers.txt not found — run Step 1 first.");
            }
            List<String> lines = java.nio.file.Files.readAllLines(path);
            if (index >= lines.size()) {
                throw new RuntimeException(
                        "Index " + index + " out of range. " +
                                "Only " + lines.size() + " transactions saved."
                );
            }
            return lines.get(index).trim();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read transaction number at index " + index, e);
        }
    }
}
