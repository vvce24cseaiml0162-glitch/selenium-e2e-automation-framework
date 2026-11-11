package com.qa.tests.base;

import com.qa.framework.base.DriverFactory;
import com.qa.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("") String browser, @Optional("") String headlessStr) {
        if (browser == null || browser.trim().isEmpty()) {
            browser = ConfigReader.getProperty("browser", "chrome");
        }

        boolean headless;
        if (headlessStr != null && !headlessStr.trim().isEmpty()) {
            headless = Boolean.parseBoolean(headlessStr.trim());
        } else {
            headless = ConfigReader.getBooleanProperty("headless", true);
        }

        logger.info("Setting up test method with Browser: [{}], Headless: [{}]", browser, headless);
        DriverFactory.initializeDriver(browser, headless);

        String appUrl = ConfigReader.getProperty("app.url", "https://www.saucedemo.com/");
        logger.info("Navigating to application URL: {}", appUrl);
        DriverFactory.getDriver().get(appUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        logger.info("Tearing down WebDriver instance");
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}

