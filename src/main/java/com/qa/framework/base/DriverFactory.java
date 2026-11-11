package com.qa.framework.base;

import com.qa.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void initializeDriver(String browser, boolean headless) {
        if (browser == null || browser.trim().isEmpty()) {
            browser = ConfigReader.getProperty("browser", "chrome");
        }

        String gridUrl = ConfigReader.getProperty("selenium.grid.url");
        WebDriver driver;

        logger.info("Initializing WebDriver - Browser: [{}], Headless: [{}], Grid URL: [{}]", browser, headless, gridUrl);

        try {
            if (gridUrl != null && !gridUrl.trim().isEmpty()) {
                driver = createRemoteDriver(new URL(gridUrl.trim()), browser, headless);
            } else {
                driver = createLocalDriver(browser, headless);
            }
        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Grid URL: {}", gridUrl, e);
            throw new RuntimeException("Malformed Selenium Grid URL", e);
        }

        int pageLoadTimeout = ConfigReader.getIntProperty("page.load.timeout", 30);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        logger.info("WebDriver successfully initialized and registered in ThreadLocal");
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser.toLowerCase().trim()) {
            case "chrome":
                ChromeOptions chromeOptions = getChromeOptions(headless);
                return new ChromeDriver(chromeOptions);

            case "firefox":
                FirefoxOptions firefoxOptions = getFirefoxOptions(headless);
                return new FirefoxDriver(firefoxOptions);

            case "edge":
                EdgeOptions edgeOptions = getEdgeOptions(headless);
                return new EdgeDriver(edgeOptions);

            default:
                logger.warn("Unsupported browser '{}', falling back to Chrome", browser);
                return new ChromeDriver(getChromeOptions(headless));
        }
    }

    private static WebDriver createRemoteDriver(URL gridUrl, String browser, boolean headless) {
        switch (browser.toLowerCase().trim()) {
            case "firefox":
                return new RemoteWebDriver(gridUrl, getFirefoxOptions(headless));
            case "edge":
                return new RemoteWebDriver(gridUrl, getEdgeOptions(headless));
            case "chrome":
            default:
                return new RemoteWebDriver(gridUrl, getChromeOptions(headless));
        }
    }

    private static ChromeOptions getChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--ignore-certificate-errors");

        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--window-size=1920,1080");
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions getEdgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver successfully quit");
            } catch (Exception e) {
                logger.warn("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}

