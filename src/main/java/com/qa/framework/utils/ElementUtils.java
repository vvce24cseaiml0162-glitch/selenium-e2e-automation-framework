package com.qa.framework.utils;

import com.qa.framework.base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElementUtils {
    private static final Logger logger = LoggerFactory.getLogger(ElementUtils.class);

    public static void click(By locator) {
        WebElement element = WaitUtils.waitForClickability(locator);
        logger.debug("Clicking on element located by: {}", locator);
        try {
            JavascriptExecutor js = (JavascriptExecutor) DriverFactory.getDriver();
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
            element.click();
        } catch (Exception e) {
            logger.warn("Standard click failed on {}. Retrying with JavaScript click.", locator);
            clickWithJS(element);
        }
    }

    public static void clickWithJS(By locator) {
        WebElement element = WaitUtils.waitForPresence(locator);
        clickWithJS(element);
    }

    public static void clickWithJS(WebElement element) {
        WebDriver driver = DriverFactory.getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", element);
    }

    public static void sendKeys(By locator, String text) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        logger.debug("Sending text '{}' to element located by: {}", text, locator);
        element.clear();
        element.sendKeys(text);
    }

    public static String getText(By locator) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        String text = element.getText().trim();
        logger.debug("Read text '{}' from element located by: {}", text, locator);
        return text;
    }

    public static boolean isDisplayed(By locator) {
        try {
            WebElement element = WaitUtils.waitForVisibility(locator, 3);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static void selectByValue(By locator, String value) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByValue(value);
        logger.debug("Selected value '{}' from dropdown {}", value, locator);
    }

    public static void selectByVisibleText(By locator, String text) {
        WebElement element = WaitUtils.waitForVisibility(locator);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.debug("Selected visible text '{}' from dropdown {}", text, locator);
    }

    public static List<WebElement> getElements(By locator) {
        return WaitUtils.waitForAllVisible(locator);
    }

    public static int getElementCount(By locator) {
        try {
            return DriverFactory.getDriver().findElements(locator).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void scrollToElement(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // --- Actions Class Methods ---
    public static void hoverOverElement(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement element = WaitUtils.waitForVisibility(locator);
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(element).perform();
        logger.debug("Hovered over element located by: {}", locator);
    }

    public static void doubleClick(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement element = WaitUtils.waitForClickability(locator);
        new org.openqa.selenium.interactions.Actions(driver).doubleClick(element).perform();
        logger.debug("Double clicked on element located by: {}", locator);
    }

    public static void rightClick(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement element = WaitUtils.waitForClickability(locator);
        new org.openqa.selenium.interactions.Actions(driver).contextClick(element).perform();
        logger.debug("Right clicked on element located by: {}", locator);
    }

    // --- Alert Handling Methods ---
    public static void acceptAlert() {
        WebDriver driver = DriverFactory.getDriver();
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
        org.openqa.selenium.Alert alert = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
        alert.accept();
        logger.debug("Accepted browser alert");
    }

    public static void dismissAlert() {
        WebDriver driver = DriverFactory.getDriver();
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
        org.openqa.selenium.Alert alert = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
        alert.dismiss();
        logger.debug("Dismissed browser alert");
    }

    public static String getAlertText() {
        WebDriver driver = DriverFactory.getDriver();
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5));
        org.openqa.selenium.Alert alert = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
        return alert.getText();
    }

    // --- Frame Handling Methods ---
    public static void switchToFrame(By frameLocator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement frameElement = WaitUtils.waitForVisibility(frameLocator);
        driver.switchTo().frame(frameElement);
        logger.debug("Switched to iframe: {}", frameLocator);
    }

    public static void switchToFrame(int frameIndex) {
        DriverFactory.getDriver().switchTo().frame(frameIndex);
        logger.debug("Switched to iframe index: {}", frameIndex);
    }

    public static void switchToDefaultContent() {
        DriverFactory.getDriver().switchTo().defaultContent();
        logger.debug("Switched back to default content");
    }

    // --- Window / Tab Handling Methods ---
    public static void switchToWindowByTitle(String expectedTitle) {
        WebDriver driver = DriverFactory.getDriver();
        String currentWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains(expectedTitle)) {
                return;
            }
        }
        driver.switchTo().window(currentWindow);
    }

    public static void closeChildWindowAndReturn(String parentWindowHandle) {
        WebDriver driver = DriverFactory.getDriver();
        driver.close();
        driver.switchTo().window(parentWindowHandle);
    }
}

