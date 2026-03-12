package core.enums;

/**
 * BrowserType — supported browser types.
 *
 * Set in framework.properties:
 *   browser=chrome
 *   browser=firefox
 *   browser=edge
 *
 * Override at runtime:
 *   mvn test -Dbrowser=firefox -Dheadless=true
 *
 * يُحدد في framework.properties أو يُتجاوز في وقت التشغيل.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE
}
