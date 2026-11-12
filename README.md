# Enterprise End-to-End Selenium Automation Framework

[![Selenium CI/CD Pipeline](https://github.com/vvce24cseaiml0162-glitch/selenium-e2e-automation-framework/actions/workflows/test-automation-ci.yml/badge.svg)](https://github.com/vvce24cseaiml0162-glitch/selenium-e2e-automation-framework/actions/workflows/test-automation-ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![Selenium WebDriver 4](https://img.shields.io/badge/Selenium-4.25.0-brightgreen.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![ExtentReports 5](https://img.shields.io/badge/ExtentReports-5.1.2-blue.svg)](https://www.extentreports.com/)
[![Docker Ready](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](Dockerfile)

A production-grade, enterprise test automation framework built with **Java 17**, **Selenium WebDriver 4**, and **TestNG**, implementing the **Page Object Model (POM)** design pattern with **ExtentReports 5**, **Dockerization**, and automated **GitHub Actions CI/CD**.

---

## 🚀 Key Framework Features

- **Page Object Model (POM)**: Complete separation of page locators, actions, and test assertions.
- **Thread-Safe WebDriver**: Utilizes `ThreadLocal<WebDriver>` for safe parallel execution across browsers and threads.
- **Cross-Browser Support**: Runs seamlessly on Chrome, Firefox, and Microsoft Edge in both headed and headless modes.
- **Data-Driven Testing (DDT)**: Integrated TestNG `@DataProvider` for parameterizing positive and negative authentication test matrices.
- **Automatic Failure Screenshots**: Automatically captures timestamped screenshots upon test failures and embeds them in Base64 format directly into ExtentReports.
- **Rich ExtentReports 5**: Interactive HTML reports featuring execution analytics, visual charts, system metrics, and detailed step logs.
- **Docker & Docker Compose**: Run tests locally or in containers using standalone Google Chrome or a distributed Selenium Grid cluster.
- **CI/CD Integration**: GitHub Actions workflow supporting push triggers, PR validation, and manual `workflow_dispatch` execution with artifact retention.
- **Environment Management**: Dynamic configuration resolution via `config.properties`, System properties (`-D`), and OS environment variables.

---

## 📁 Project Architecture & Directory Structure

```text
selenium-e2e-automation-framework/
├── .github/
│   └── workflows/
│       └── test-automation-ci.yml    # GitHub Actions CI/CD Pipeline
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/qa/framework/
│   │   │       ├── base/
│   │   │       │   ├── BasePage.java         # Base page with common browser helpers
│   │   │       │   └── DriverFactory.java    # ThreadLocal WebDriver manager
│   │   │       ├── config/
│   │   │       │   └── ConfigReader.java     # Config manager (properties, env, CLI)
│   │   │       ├── listeners/
│   │   │       │   └── TestListener.java     # TestNG listener for reporting & screenshots
│   │   │       ├── pages/
│   │   │       │   ├── LoginPage.java
│   │   │       │   ├── ProductsPage.java
│   │   │       │   ├── CartPage.java
│   │   │       │   ├── CheckoutInformationPage.java
│   │   │       │   ├── CheckoutOverviewPage.java
│   │   │       │   └── CheckoutCompletePage.java
│   │   │       ├── reports/
│   │   │       │   └── ExtentReportManager.java # ExtentReports 5 Spark configuration
│   │   │       └── utils/
│   │   │           ├── ElementUtils.java     # Safe element interaction, Actions, Alerts, Frames
│   │   │           ├── ScreenshotUtils.java  # File & Base64 screenshot capture
│   │   │           └── WaitUtils.java        # Explicit wait utilities (WebDriverWait)
│   │   └── resources/
│   │       ├── config.properties             # Global test configurations
│   │       └── logback.xml                   # Console & file logging configuration
│   └── test/
│       ├── java/
│       │   └── com/qa/tests/
│       │       ├── base/
│       │       │   └── BaseTest.java         # Test lifecycle hooks (@BeforeMethod, @AfterMethod)
│       │       ├── LoginTests.java           # Authentication, validations, & @DataProvider tests
│       │       ├── ProductCatalogTests.java  # Inventory listing & sorting tests
│       │       ├── CartWorkflowTests.java    # Cart addition, verification & removal
│       │       └── EndToEndCheckoutTests.java# Complete purchase workflow & form validations
│       └── resources/
│           └── testdata/
│               └── testdata.json             # Test dataset
├── reports/                                  # HTML reports, screenshots, & logs
│   └── ExtentReport.html                     # Interactive HTML execution report
├── testng.xml                                # TestNG Suite runner configuration
├── pom.xml                                   # Maven dependency & surefire configuration
├── Dockerfile                                # Containerized test execution image
├── docker-compose.yml                        # Multi-service grid composition
├── .dockerignore                             # Docker build ignore rules
└── .gitignore                                # Git repository ignore rules
```

---

## 🧪 Test Suite & Coverage (22 Test Executions)

| Test Class | Test Scenario | Type | Description |
| :--- | :--- | :--- | :--- |
| **`LoginTests`** | `testSuccessfulLogin` | Positive | Verifies valid login navigates to inventory page |
| **`LoginTests`** | `testLoginWithLockedOutUser` | Negative | Verifies locked-out user displays expected error banner |
| **`LoginTests`** | `testLoginWithInvalidCredentials` | Negative | Verifies invalid credentials error message |
| **`LoginTests`** | `testLoginWithEmptyUsername` | Validation | Verifies username required validation message |
| **`LoginTests`** | `testLoginWithEmptyPassword` | Validation | Verifies password required validation message |
| **`LoginTests`** | `testLogoutWorkflow` | Workflow | Verifies sidebar burger menu logout workflow |
| **`LoginTests`** | `testDataDrivenLoginValidation (4 runs)` | Data-Driven | Parameterized test for locked, invalid, empty inputs |
| **`ProductCatalogTests`** | `testProductListCount` | Validation | Verifies catalog renders all 6 inventory items |
| **`ProductCatalogTests`** | `testAddAndRemoveProductFromCatalog` | Workflow | Adds & removes product, checks cart badge count updates |
| **`ProductCatalogTests`** | `testSortProductsByNameZtoA` | Functional | Selects sorting dropdown `Z to A` and verifies order |
| **`ProductCatalogTests`** | `testSortProductsByPriceLowToHigh` | Functional | Selects sorting dropdown `Price (low to high)` |
| **`ProductCatalogTests`** | `testSortProductsByPriceHighToLow` | Functional | Selects sorting dropdown `Price (high to low)` |
| **`ProductCatalogTests`** | `testSortProductsByNameAtoZ` | Functional | Selects sorting dropdown `Name (A to Z)` |
| **`CartWorkflowTests`** | `testAddMultipleItemsToCart` | Workflow | Adds multiple items and verifies appearance in cart |
| **`CartWorkflowTests`** | `testRemoveItemFromCartPage` | Functional | Removes item from cart and validates remaining items |
| **`CartWorkflowTests`** | `testContinueShoppingNavigation` | Navigation | Clicks `Continue Shopping` and validates catalog return |
| **`EndToEndCheckoutTests`** | `testPositiveEndToEndPurchaseFlow` | E2E | Full purchase: login → cart → info → tax/subtotal → order complete |
| **`EndToEndCheckoutTests`** | `testCheckoutFormValidationMissingPostalCode` | Validation | Verifies error message when postal code is missing |
| **`EndToEndCheckoutTests`** | `testCheckoutFormValidationMissingFirstName` | Validation | Verifies error message when first name is missing |

---

## 🛠️ Prerequisites

- **Java JDK 17+**
- **Apache Maven 3.8+**
- **Google Chrome** (Selenium 4 automatically manages drivers via Selenium Manager)
- *(Optional)* **Docker & Docker Compose**

---

## ⚡ Local Test Execution

### 1. Run all tests with Maven
```bash
mvn clean test
```

### 2. Run with custom browser or headless mode
```bash
mvn clean test -Dbrowser=chrome -Dheadless=true
```

### 3. Run specific test class
```bash
mvn clean test -Dtest=EndToEndCheckoutTests
```

### 4. Run targeted test methods
```bash
mvn clean test -Dtest=LoginTests#testSuccessfulLogin
```

---

## 🐳 Docker Execution

### 1. Build and Run Standalone Container
```bash
# Build Docker image
docker build -t selenium-e2e-automation:latest .

# Run tests with persistent reports volume
docker run --rm -v $(pwd)/reports:/app/reports selenium-e2e-automation:latest
```

### 2. Run with Docker Compose (Selenium Grid)
```bash
docker-compose up --build --abort-on-container-exit
```

---

## 🔄 GitHub Actions CI/CD Pipeline

The framework includes a GitHub Actions pipeline (`.github/workflows/test-automation-ci.yml`) featuring:
1. **Runner Test Execution**: Automated execution on Ubuntu runners with Google Chrome in headless mode.
2. **Containerized Docker Execution**: Automated building of the Docker image and execution of tests inside the Docker container.
3. **Artifact Retention**: Automatically captures and uploads `reports/ExtentReport.html`, Surefire XML reports, and failure screenshots as downloadable workflow artifacts.
4. **Interactive Dispatch**: Manual workflow triggers with selectable browser, headless mode, and TestNG XML suite options.

---

## 📊 Test Reports & Artifacts

After execution, find the generated reports in:
- **Extent Report**: `reports/ExtentReport.html`
- **Surefire HTML Report**: `target/surefire-reports/index.html`
- **Surefire Test Suite XML**: `target/surefire-reports/testng-results.xml`
- **Execution Logs**: `reports/logs/automation.log`
- **Failure Screenshots**: `reports/screenshots/`
