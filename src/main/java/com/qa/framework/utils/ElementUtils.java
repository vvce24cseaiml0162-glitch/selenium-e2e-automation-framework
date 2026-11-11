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
            element.click();
        } catch (Exception e) {
            logger.warn("Standard click failed on {}. Retrying with JavaScript click.", locator);
            clickWithJS(locator);
        }
    }

    public static void clickWithJS(By locator) {
        WebDriver driver = DriverFactory.getDriver();
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
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
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}

