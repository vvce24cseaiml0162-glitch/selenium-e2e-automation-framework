package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageContainer = By.cssSelector("[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public boolean isLoginPageDisplayed() {
        try {
            return WaitUtils.waitForVisibility(loginButton, 5) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        ElementUtils.sendKeys(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        ElementUtils.sendKeys(passwordInput, password);
        return this;
    }

    public void clickLoginExpectingFailure() {
        logger.info("Clicking login button expecting failure");
        ElementUtils.click(loginButton);
    }

    public ProductsPage clickLogin() {
        logger.info("Clicking login button");
        ElementUtils.click(loginButton);
        ProductsPage page = new ProductsPage();
        page.isPageLoaded();
        return page;
    }

    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        WaitUtils.waitForVisibility(errorMessageContainer);
        String error = ElementUtils.getText(errorMessageContainer);
        logger.info("Error message displayed: {}", error);
        return error;
    }

    public boolean isErrorMessageDisplayed() {
        return ElementUtils.isDisplayed(errorMessageContainer);
    }
}
