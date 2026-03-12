# Royal Grant Automation — نظام المنح الملكية

End-to-end test automation suite for the Royal Grants (المنح الملكية) system — a Saudi government grant processing application. Built with Selenium WebDriver, Cucumber BDD, and TestNG.

---

## Tech Stack

| Tool | Version |
|------|---------|
| Java | 17 |
| Maven | 3.x |
| Selenium WebDriver | 4.18.1 |
| Cucumber | 7.15.0 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| Allure Reports | 2.25.0 |
| PicoContainer (DI) | 2.15 |

---

## Project Structure

```
src/
├── main/java/pages/              # Page Object Model
│   ├── LoginPage.java
│   ├── HomePage.java
│   ├── AppsPage.java
│   ├── GrantsPortalPage.java
│   ├── LocalGrantsAppPage.java
│   ├── step1/                    # Citizen submission pages
│   │   ├── OwnerDataPage.java
│   │   ├── RoyalDecreePage.java
│   │   ├── NotesPage.java
│   │   └── SummaryPage.java
│   ├── step2/                    # Grant Employee review pages
│   │   └── Step2Pages.java
│   └── step3/                    # Manager finalization pages
│       └── Step3Pages.java
│
└── test/
    ├── java/
    │   ├── runners/              # Cucumber + TestNG runner
    │   ├── stepdefs/             # Gherkin step definitions
    │   ├── hooks/                # Before/After lifecycle hooks
    │   └── core/
    │       ├── driver/           # WebDriver management (ThreadLocal)
    │       ├── context/          # PicoContainer dependency injection
    │       └── config/           # Configuration loader
    └── resources/
        ├── features/             # Gherkin feature files (9 scenarios)
        └── config/
            └── framework.properties
```

---

## Workflow Automated

The suite automates a full 3-step grant application lifecycle:

```
Step 1 — Citizen
  Login → Create transaction → Fill owner data → Royal decree → Notes → Submit

Step 2 — Grant Employee
  Login → View incoming → Fill file opening data → Add notes → Forward

Step 3 — Grant Manager
  Login → View incoming → Add manager notes → Finalize for lottery
```

Transaction numbers are persisted across steps via `target/transaction_numbers.txt`.

---

## Configuration

Edit `src/test/resources/config/framework.properties`:

```properties
browser=chrome          # chrome | firefox | edge
headless=false
timeout=20              # seconds
basePortalUrl=https://geoservices1.syadtech.com/home/Login
baseLocalUrl=http://localhost:8080/#/submissions/grantroyal
screenshots.on.failure=true
execution.tags=@smoke
```

All properties can be overridden at runtime with Maven `-D` flags.

---

## Running Tests

**Run all E2E tests:**
```bash
mvn test
```

**Run a specific step only:**
```bash
mvn test -Dcucumber.filter.tags="@Step1"
mvn test -Dcucumber.filter.tags="@Step2"
mvn test -Dcucumber.filter.tags="@Step3"
```

**Run with a different browser:**
```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

**Run headless:**
```bash
mvn test -Dheadless=true
```

**Combine options:**
```bash
mvn test -Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags="@smoke"
```

---

## Reports

After a test run, reports are available at:

| Report | Path |
|--------|------|
| Cucumber HTML | `target/cucumber-reports/e2e-report.html` |
| Cucumber JSON | `target/cucumber-reports/e2e-report.json` |
| TestNG | `target/surefire-reports/` |
| Allure results | `allure-results/` |

**Generate and open Allure report:**
```bash
mvn allure:serve
```

---

## Feature Files

| Feature File | Description |
|---|---|
| `E2E_GrantsWorkflow.feature` | Main 3-step E2E workflow |
| `E2E_mahmoud_AllAreas.feature` | All grant areas — Mahmoud |
| `E2E_emad_AllAreas.feature` | All grant areas — Emad |
| `E2E_abdullah_AllAreas.feature` | All grant areas — Abdullah |
| `E2E_ownertest_AllAreas.feature` | All grant areas — Owner test |
| `E2E_maldossary_AllAreas.feature` | All grant areas — Al-Dossary |
| `E2E_ibrahem_AllAreas.feature` | All grant areas — Ibrahem |
| `E2E_mamdouh_AllAreas.feature` | All grant areas — Mamdouh |
| `E2E_RandomDistribution.feature` | Random distribution scenario |

---

## Prerequisites

- Java 17+
- Maven 3.6+
- Google Chrome (or Firefox/Edge) installed
- The portal and localhost app must be accessible
