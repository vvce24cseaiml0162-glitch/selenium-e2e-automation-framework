package com.qa.framework.utils;

import com.qa.framework.base.DriverFactory;
import com.qa.framework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static int getDefaultWaitTimeout() {
        return ConfigReader.getIntProperty("explicit.wait", 10);
    }

    private static WebDriverWait getWait(int timeoutInSeconds) {
        WebDriver driver = DriverFactory.getDriver();
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public static WebElement waitForPresence(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return waitForPresence(locator, getDefaultWaitTimeout());
    }

    public static WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(By locator) {
        return waitForVisibility(locator, getDefaultWaitTimeout());
    }

    public static WebElement waitForClickability(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickability(By locator) {
        return waitForClickability(locator, getDefaultWaitTimeout());
    }

    public static boolean waitForInvisibility(By locator, int timeoutInSeconds) {
        try {
            return Boolean.TRUE.equals(getWait(timeoutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator)));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitForInvisibility(By locator) {
        return waitForInvisibility(locator, getDefaultWaitTimeout());
    }

    public static List<WebElement> waitForAllVisible(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static List<WebElement> waitForAllVisible(By locator) {
        return waitForAllVisible(locator, getDefaultWaitTimeout());
    }

    public static boolean waitForUrlContains(String fraction, int timeoutInSeconds) {
        try {
            return Boolean.TRUE.equals(getWait(timeoutInSeconds).until(ExpectedConditions.urlContains(fraction)));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean waitForUrlContains(String fraction) {
        return waitForUrlContains(fraction, getDefaultWaitTimeout());
    }

    public static boolean waitForTextToBePresent(By locator, String text, int timeoutInSeconds) {
        try {
            return Boolean.TRUE.equals(getWait(timeoutInSeconds).until(ExpectedConditions.textToBePresentInElementLocated(locator, text)));
        } catch (Exception e) {
            return false;
        }
    }
}

