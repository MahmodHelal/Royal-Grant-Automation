package core.driver;

import core.config.ConfigManager;
import core.enums.BrowserType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DriverFactory — creates WebDriver instances based on configured browser type.
 *
 * Reads: browser=chrome|firefox|edge from framework.properties or Maven -D args.
 * Reads: headless=true|false
 *
 * CRITICAL RULE: No --user-data-dir with fixed paths.
 * Each scenario gets a clean, isolated browser session.
 *
 * القاعدة الحرجة: لا تستخدم --user-data-dir بمسارات ثابتة.
 * كل scenario يحصل على جلسة متصفح نظيفة ومعزولة.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static WebDriver createDriver() {
        String browserName = ConfigManager.get("browser");
        if (browserName == null || browserName.isBlank()) browserName = "chrome";
        boolean headless = ConfigManager.getBool("headless");

        BrowserType browserType;
        try {
            browserType = BrowserType.valueOf(browserName.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Unknown browser '{}', defaulting to CHROME.", browserName);
            browserType = BrowserType.CHROME;
        }

        log.info("Creating driver: {} | headless={}", browserType, headless);
        WebDriver driver = buildDriver(browserType, headless);
        driver.manage().window().maximize();
        log.info("Driver ready.");
        return driver;
    }

    private static WebDriver buildDriver(BrowserType type, boolean headless) {
        switch (type) {
            case FIREFOX: {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (headless) opts.addArguments("-headless");
                return new FirefoxDriver(opts);
            }
            case EDGE: {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (headless) opts.addArguments("--headless=new");
                return new EdgeDriver(opts);
            }
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
/*                opts.addArguments("--start-maximized");
                opts.addArguments("--disable-notifications");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                opts.addArguments("--disable-blink-features=AutomationControlled");
                opts.addArguments("--remote-allow-origins=*");
                opts.addArguments("--disable-web-security");
                opts.addArguments("--user-data-dir=C://ChromeDevSession");
                // ⚠️ NO --user-data-dir — each scenario needs a fresh session*/
                opts.addArguments("--start-maximized");
                opts.addArguments("--disable-notifications");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                // Accept Arabic content properly
//                opts.addArguments("--lang=ar");
                opts.addArguments("--disable-web-security");
                opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);

//                opts.addArguments("--user-data-dir=C://ChromeDevSession");

                return new ChromeDriver(opts);
            }
        }
    }
}
