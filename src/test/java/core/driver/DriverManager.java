package core.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverManager — manages WebDriver lifecycle using ThreadLocal.
 *
 * ThreadLocal ensures each thread (scenario) has its own isolated driver.
 * This design supports future parallel execution without any changes.
 *
 * ThreadLocal يضمن أن كل thread (scenario) له driver معزول.
 * هذا التصميم يدعم التنفيذ المتوازي في المستقبل بدون أي تغييرات.
 *
 * Usage pattern:
 *   DriverManager.setDriver(DriverFactory.createDriver())  — in @Before
 *   DriverManager.getDriver()                               — in pages / steps
 *   DriverManager.quit()                                    — in @After
 */
public class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
        // Utility class — not instantiable
    }

    /**
     * Returns the WebDriver for the current thread.
     * يرجع الـ WebDriver للـ thread الحالي.
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Sets the WebDriver for the current thread.
     * يضبط الـ WebDriver للـ thread الحالي.
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
        log.debug("WebDriver set for thread: {}", Thread.currentThread().getName());
    }

    /**
     * Quits and removes the WebDriver for the current thread.
     * يغلق ويزيل الـ WebDriver للـ thread الحالي.
     *
     * Always called in @After to ensure browser cleanup between scenarios.
     */
    public static void quit() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("WebDriver quit successfully.");
            } catch (Exception e) {
                log.warn("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
