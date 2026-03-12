package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * GrantsPortalPage — صفحة بوابة المنح (geoservices1.syadtech.com)
 *
 * PURPOSE: Extract JWT token from URL after clicking app card,
 *          then navigate to localhost:8080 with that token.
 *
 * الهدف: استخراج JWT token من الـ URL بعد الضغط على بطاقة التطبيق،
 *        ثم الانتقال إلى localhost:8080 بهذا الـ token.
 */
public class GrantsPortalPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Base URL for localhost app
    private static final String LOCALHOST_BASE = "http://localhost:8080/#/submissions/grantroyal?tk=";

    public GrantsPortalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Extracts JWT token from the current URL after redirect.
     * يستخرج JWT token من الـ URL الحالي بعد الـ redirect.
     *
     * URL pattern: .../gisv2/#/submissions/grantroyal?tk=eyJ...
     *
     * @return JWT token string
     */
    public String extractJwtToken() {
        // أولاً جرب تسحبه من localStorage — موجود من وقت login
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String token = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(Exception.class)
                .until(d -> {
                    // جرب localStorage أولاً
                    String t = (String) js.executeScript(
                            "return localStorage.getItem('token') || " +
                                    "localStorage.getItem('tk') || " +
                                    "localStorage.getItem('jwt') || " +
                                    "localStorage.getItem('access_token');"
                    );
                    if (t != null && !t.isEmpty()) return t;

                    // لو مش في localStorage، جرب الـ URL
                    String url = d.getCurrentUrl();
                    if (url.contains("tk=")) {
                        return url.substring(url.indexOf("tk=") + 3);
                    }
                    return null;
                });

        System.out.println("[INFO] JWT token extracted. Length: " + token.length());
        return token;
    }    /**
     * Fallback: Extract token from localStorage if not in URL.
     * احتياطي: استخراج الـ token من localStorage إذا لم يكن في الـ URL.
     */
    private String extractTokenFromLocalStorage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String token = (String) js.executeScript("return localStorage.getItem('token');");
        if (token != null && !token.isEmpty()) {
            System.out.println("[INFO] JWT token extracted from localStorage.");
            return token;
        }
        throw new RuntimeException("[ERROR] Could not extract JWT token from URL or localStorage.");
    }

    /**
     * Opens localhost grants app with the provided JWT token.
     * يفتح تطبيق المنح على localhost بالـ JWT token المقدم.
     *
     * @param token JWT token string
     */
    public void openLocalhostWithToken(String token) {
        String localUrl = LOCALHOST_BASE + token;
        System.out.println("[INFO] Navigating to localhost: " + localUrl.substring(0, 60) + "...");
        driver.get(localUrl);

        // Wait for localhost app to load (انتظر تحميل التطبيق)
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("localhost"),
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(@class,'ant-layout') or contains(@class,'addTranDiv')]"))
        ));
        System.out.println("[INFO] Localhost app loaded successfully.");
    }

    /**
     * Full flow: extract token then navigate to localhost.
     * الفلو الكامل: استخرج الـ token ثم انتقل إلى localhost.
     */
    public void transferToLocalhost() {
        String token = extractJwtToken();
        openLocalhostWithToken(token);
    }
}
