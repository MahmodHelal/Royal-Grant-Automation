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

    // Clear the transaction file at the start of each feature file's first @Step1 scenario.
    // Tracking by feature URI ensures each feature gets a fresh file when running all features
    // together in one Maven run, preventing stale transaction indices from previous features.
    private static String lastStep1FeatureUri = null;

    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        // Clear the file whenever a new feature file's @Step1 scenario starts
        String currentUri = scenario.getUri().toString();
        if (scenario.getSourceTagNames().contains("@Step1")
                && !currentUri.equals(lastStep1FeatureUri)) {
            try {
                java.nio.file.Files.deleteIfExists(
                        java.nio.file.Path.of("target/transaction_numbers.txt")
                );
                log.info("Cleared transaction_numbers.txt for feature: {}", currentUri);
                lastStep1FeatureUri = currentUri;
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