package hooks;

import core.context.TestContext;
import core.driver.DriverFactory;
import core.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        // ✅ امسح الملف بس في أول scenario (Step1)
        if (scenario.getSourceTagNames().contains("@Step1")) {
            try {
                java.nio.file.Files.deleteIfExists(
                        java.nio.file.Path.of("target/transaction_numbers.txt")
                );
                log.info("Cleared transaction_numbers.txt for new run.");
            } catch (Exception ignored) {}
        }

        DriverManager.setDriver(DriverFactory.createDriver());
        log.info("New browser session started.");
    }
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            log.warn("⚠ Scenario FAILED — capturing screenshot: {}", scenario.getName());
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot — " + scenario.getName());
            } catch (Exception e) {
                log.error("Screenshot capture failed: {}", e.getMessage());
            }
        }

        DriverManager.quit();
        ctx.clear();

        log.info("■ Scenario END: {} | Status: {}", scenario.getName(), scenario.getStatus());
        log.info("══════════════════════════════════════════════");
    }
}