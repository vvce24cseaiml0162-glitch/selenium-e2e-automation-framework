package com.qa.framework.utils;

import com.qa.framework.base.DriverFactory;
import com.qa.framework.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);

    public static String captureScreenshotAsFile(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            logger.warn("Driver is null. Unable to capture screenshot.");
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String dir = ConfigReader.getProperty("screenshots.dir", "reports/screenshots");
        String fileName = testName + "_" + timestamp + ".png";
        File destination = new File(dir, fileName);

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);
            logger.info("Screenshot saved successfully to: {}", destination.getAbsolutePath());
            return destination.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }

    public static String captureScreenshotAsBase64() {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to capture Base64 screenshot", e);
            return null;
        }
    }
}

