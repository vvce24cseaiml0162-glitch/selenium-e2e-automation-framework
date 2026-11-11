package com.qa.framework.base;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public String getPageTitle() {
        String title = getDriver().getTitle();
        logger.debug("Page title: {}", title);
        return title;
    }

    public String getCurrentUrl() {
        String url = getDriver().getCurrentUrl();
        logger.debug("Current URL: {}", url);
        return url;
    }

    public void refreshPage() {
        getDriver().navigate().refresh();
        logger.debug("Refreshed the page");
    }

    public void navigateBack() {
        getDriver().navigate().back();
        logger.debug("Navigated back");
    }
}

