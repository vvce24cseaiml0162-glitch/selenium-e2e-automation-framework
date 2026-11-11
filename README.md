# Enterprise End-to-End Selenium Automation Framework

A production-grade, scalable Test Automation Framework built with **Java 17**, **Selenium WebDriver 4**, and **TestNG**, implementing the **Page Object Model (POM)** design pattern with **ExtentReports 5**, **Dockerization**, and **GitHub Actions CI/CD**.

---

## 🚀 Key Framework Features

- **Page Object Model (POM)**: Complete separation of page locators, actions, and test assertions.
- **Thread-Safe WebDriver**: Utilizes `ThreadLocal<WebDriver>` for safe parallel execution across browsers and threads.
- **Cross-Browser Support**: Runs seamlessly on Chrome, Firefox, and Microsoft Edge in both headed and headless modes.
- **Automatic Failure Screenshots**: Automatically captures timestamped screenshots upon test failures and embeds them in Base64 format directly into ExtentReports.
- **Rich ExtentReports 5**: Interactive HTML reports featuring execution analytics, visual charts, system metrics, and detailed step logs.
- **Docker & Docker Compose**: Run tests locally or in containers using standalone Google Chrome or a distributed Selenium Grid cluster.
- **CI/CD Integration**: GitHub Actions workflow supporting push triggers, PR validation, and manual `workflow_dispatch` execution with artifact retention.
- **Environment Management**: Dynamic configuration resolution via `config.properties`, System properties (`-D`), and OS environment variables.

---

## 📁 Project Architecture & Directory Structure

```
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
│   │   │           ├── ElementUtils.java     # Safe element interaction helpers
│   │   │           ├── ScreenshotUtils.java  # File & Base64 screenshot capture
│   │   │           └── WaitUtils.java        # Explicit wait utilities
│   │   └── resources/
│   │       ├── config.properties             # Global test configurations
│   │       └── logback.xml                   # Console & file logging configuration
│   └── test/
│       ├── java/
│       │   └── com/qa/tests/
│       │       ├── base/
│       │       │   └── BaseTest.java         # Test lifecycle hooks (@BeforeMethod, @AfterMethod)
│       │       ├── LoginTests.java           # Authentication & validation scenarios
│       │       ├── ProductCatalogTests.java  # Inventory listing & sorting tests
│       │       ├── CartWorkflowTests.java    # Cart addition, verification & removal
│       │       └── EndToEndCheckoutTests.java# Complete purchase workflow & form validations
│       └── resources/
│           └── testdata/
│               └── testdata.json             # Test dataset
├── reports/                                  # HTML reports, screenshots, & logs
├── testng.xml                                # TestNG Suite runner configuration
├── pom.xml                                   # Maven dependency & surefire configuration
├── Dockerfile                                # Containerized test execution image
├── docker-compose.yml                        # Multi-service grid composition
└── .gitignore
```

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

---

## 🐳 Docker Execution

### 1. Build and Run Standalone Container
```bash
# Build Docker image
docker build -t selenium-e2e-automation .

# Run tests with persistent reports volume
docker run --rm -v $(pwd)/reports:/app/reports selenium-e2e-automation
```

### 2. Run with Docker Compose (Selenium Grid)
```bash
docker-compose up --build --abort-on-container-exit
```

---

## 📊 Test Reports & Artifacts

After execution, find the generated reports in:
- **Extent Report**: `reports/ExtentReport.html`
- **Execution Logs**: `reports/logs/automation.log`
- **Screenshots (on failure)**: `reports/screenshots/`
- **Surefire Reports**: `target/surefire-reports/index.html`

