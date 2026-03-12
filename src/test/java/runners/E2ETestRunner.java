package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * E2ETestRunner — runs the full E2E workflow (Steps 1→2→3).
 *
 * Run with tag filter:
 *   mvn test -Dcucumber.filter.tags="@Step1"
 *   mvn test -Dcucumber.filter.tags="@Step2"
 *   mvn test -Dcucumber.filter.tags="@E2E"
 *   mvn test -Dheadless=true
 */
@CucumberOptions(
        features = "src/test/resources/features/E2E_GrantsWorkflow.feature",
        glue     = {"stepdefs", "hooks"},
        plugin   = {
                "pretty",
                "html:target/cucumber-reports/e2e-report.html",
                "json:target/cucumber-reports/e2e-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@E2E"
)
public class E2ETestRunner extends AbstractTestNGCucumberTests {
}
