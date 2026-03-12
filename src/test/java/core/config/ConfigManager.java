package core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager — loads framework configuration from framework.properties.
 *
 * Supports override via Maven system properties:
 *   mvn test -Dbrowser=chrome -Dheadless=true
 *
 * يدعم التجاوز عبر Maven system properties.
 *
 * Usage:
 *   ConfigManager.get("browser")          → "chrome"
 *   ConfigManager.get("basePortalUrl")    → portal URL
 *   ConfigManager.getInt("timeout")       → 20
 *   ConfigManager.getBool("headless")     → false
 */
public class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config/framework.properties")) {
            if (is != null) {
                properties.load(is);
                log.info("framework.properties loaded successfully.");
            } else {
                log.warn("framework.properties not found. Using defaults.");
            }
        } catch (IOException e) {
            log.error("Failed to load framework.properties: {}", e.getMessage());
        }
    }

    private ConfigManager() {}

    /**
     * Gets a property value. System property takes precedence over file.
     * يأخذ قيمة property. الـ system property له أولوية على الـ file.
     */
    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        return properties.getProperty(key, "");
    }

    public static int getInt(String key) {
        String value = get(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Invalid int for key '{}': '{}'. Returning 0.", key, value);
            return 0;
        }
    }

    public static boolean getBool(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
