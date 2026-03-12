package pages.step1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 3 — ملاحظات (Notes)
// Step indicator: <div name="approvalSubmissionNotes" class="ant-steps-item-active">
// ═══════════════════════════════════════════════════════════════════════════════
public class NotesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // "اضافة" button to add a new note row
    // IMPORTANT: different from the "اضافة" in Tab 1 (owner)
    // Context: inside notes table area, NOT the next/prev buttons area
    private By addNoteBtn = By.xpath(
            "//div[@name='approvalSubmissionNotes' or " +
                    "ancestor::*[@name='approvalSubmissionNotes']]" +
                    "//button[.//text()[contains(.,'اضافة') or contains(.,'إضافة')]]");

    // Fallback: any add button not in navigation area
    private By addNoteBtnAlt = By.xpath(
            "//button[contains(@class,'ant-btn')]" +
                    "[not(contains(@class,'next') or contains(@class,'prev') or contains(@class,'cancel'))]" +
                    "[.//span[contains(text(),'اضافة') or contains(text(),'إضافة')]]");

    // Note textarea — appears after clicking "اضافة"
    // name="notes.notes[0].notes"
    private By noteTextarea = By.xpath(
            "//textarea[@name='notes.notes[0].notes']");
    // Fallback by class
    private By noteTextareaAlt = By.xpath(
            "//textarea[contains(@class,'attachContainerNote')]");

    // ─── Navigation ───────────────────────────────────────────────────────────
    private By nextBtn = By.xpath(
            "//button[contains(@class,'next')][.//span[contains(text(),'التالى')]]");

    public NotesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Clicks "اضافة" to create a new note row.
     */
    public void clickAddNote() {

        By addNoteBtn = By.xpath("//button[.//span[text()='اضافة']]");

        wait.until(ExpectedConditions.elementToBeClickable(addNoteBtn)).click();

        System.out.println("[INFO] Clicked: اضافة (note row)");
    }
    /**
     * Fills the note textarea.
     * ملاحظة: النظام يحفظ تلقائياً (auto-save active) — Bug #827
     */
    public void fillNoteText(String noteText) {

        By noteTextarea = By.xpath("//textarea");

        WebElement textarea = wait.until(
                ExpectedConditions.visibilityOfElementLocated(noteTextarea)
        );

        textarea.clear();
        textarea.sendKeys(noteText);

        System.out.println("[INFO] Note text filled.");
    }

    public void clickNext() {
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        driver.findElement(nextBtn).click();
        System.out.println("[INFO] Clicked: التالى (Tab 3 → Tab 4)");
    }
}
