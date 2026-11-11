package com.qa.framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.framework.reports.ExtentReportManager;
import com.qa.framework.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("=================================================");
        logger.info("Test Suite Execution Started: {}", context.getName());
        logger.info("=================================================");
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        logger.info("--- Starting Test: {} ---", testName);
        ExtentTest test = ExtentReportManager.getInstance().createTest(testName, description);
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("--- PASSED Test: {} ---", testName);
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test passed successfully.");
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("--- FAILED Test: {} ---", testName, result.getThrowable());

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
            test.log(Status.FAIL, result.getThrowable());

            // Capture screenshot and embed in report
            String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64();
            ScreenshotUtils.captureScreenshotAsFile(testName);

            if (base64Screenshot != null) {
                test.fail("Screenshot at failure:",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            }
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("--- SKIPPED Test: {} ---", testName);
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test skipped: " + (result.getThrowable() != null ? result.getThrowable().getMessage() : ""));
        }
        ExtentReportManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("=================================================");
        logger.info("Test Suite Execution Finished: {}", context.getName());
        logger.info("=================================================");
        ExtentReportManager.flush();
    }
}

