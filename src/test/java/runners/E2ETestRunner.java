package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * E2ETestRunner — نظام المنح الملكية
 * Runs the full E2E workflow across all users (Steps 1 → 2 → 3).
 *
 * ─── Tag Filters ──────────────────────────────────────────────────────────────
 *   mvn test                                          → all @E2E scenarios
 *   mvn test -Dcucumber.filter.tags="@Step1"          → citizen submission only
 *   mvn test -Dcucumber.filter.tags="@Step2"          → grant employee only
 *   mvn test -Dcucumber.filter.tags="@Step3"          → manager only
 *   mvn test -Dcucumber.filter.tags="@smoke"          → smoke suite
 *   mvn test -Dcucumber.filter.tags="@User_EMAD"      → single user
 *
 * ─── Browser & Mode ───────────────────────────────────────────────────────────
 *   mvn test -Dbrowser=firefox
 *   mvn test -Dbrowser=edge
 *   mvn test -Dheadless=true
 *
 * ─── Run a single feature ─────────────────────────────────────────────────────
 *   mvn test -Dcucumber.features="src/test/resources/features/E2E_emad_AllAreas.feature"
 *
 * ─── Reports ──────────────────────────────────────────────────────────────────
 *   HTML   → target/cucumber-reports/e2e-report.html
 *   JSON   → target/cucumber-reports/e2e-report.json
 *   JUnit  → target/cucumber-reports/e2e-report.xml
 *   Allure → allure-results/   (view with: mvn allure:serve)
 */
@CucumberOptions(
        features = {
                "src/test/resources/features/E2E_GrantsWorkflow.feature",
                "src/test/resources/features/E2E_emad_AllAreas.feature",
                "src/test/resources/features/E2E_mahmoud_AllAreas.feature",
                "src/test/resources/features/E2E_mamdouh_AllAreas.feature",
                "src/test/resources/features/E2E_abdullah_AllAreas.feature",
                "src/test/resources/features/E2E_ibrahem_AllAreas.feature",
                "src/test/resources/features/E2E_maldossary_AllAreas.feature",
                "src/test/resources/features/E2E_ownertest_AllAreas.feature",
                "src/test/resources/features/E2E_RandomDistribution.feature"
        },
        glue            = {"stepdefs", "hooks"},
        tags            = "@E2E",
        monochrome      = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/e2e-report.html",
                "json:target/cucumber-reports/e2e-report.json",
                "junit:target/cucumber-reports/e2e-report.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class E2ETestRunner extends AbstractTestNGCucumberTests {

    /**
     * Explicitly disable parallel execution.
     * The E2E workflow is sequential: Step 1 → Step 2 → Step 3.
     * Parallel runs would break cross-step transaction number sharing.
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
