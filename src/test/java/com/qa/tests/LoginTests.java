package com.qa.tests;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.ProductsPage;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed");

        String username = ConfigReader.getProperty("default.username");
        String password = ConfigReader.getProperty("default.password");

        ProductsPage productsPage = loginPage.loginAs(username, password);
        Assert.assertTrue(productsPage.isPageLoaded(), "Products page did not load after successful login");
        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"), "URL does not contain inventory.html");
    }

    @Test(priority = 2, description = "Verify login failure with locked out user")
    public void testLoginWithLockedOutUser() {
        LoginPage loginPage = new LoginPage();
        String lockedUser = ConfigReader.getProperty("locked.username");
        String password = ConfigReader.getProperty("default.password");

        loginPage.enterUsername(lockedUser)
                 .enterPassword(password)
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sorry, this user has been locked out"),
                "Expected error message not matched. Found: " + loginPage.getErrorMessage());
    }

    @Test(priority = 3, description = "Verify login failure with invalid username and password")
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterUsername("invalid_user_99")
                 .enterPassword("secret_invalid")
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for invalid credentials");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Expected error message not matched. Found: " + loginPage.getErrorMessage());
    }

    @Test(priority = 4, description = "Verify validation error when username is empty")
    public void testLoginWithEmptyUsername() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterPassword("secret_sauce")
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for empty username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Expected error message not matched. Found: " + loginPage.getErrorMessage());
    }

    @Test(priority = 5, description = "Verify validation error when password is empty")
    public void testLoginWithEmptyPassword() {
        LoginPage loginPage = new LoginPage();
        loginPage.enterUsername("standard_user")
                 .clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message was not displayed for empty password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Expected error message not matched. Found: " + loginPage.getErrorMessage());
    }

    @Test(priority = 6, description = "Verify user can successfully logout from the application")
    public void testLogoutWorkflow() {
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = loginPage.loginAs(ConfigReader.getProperty("default.username"),
                ConfigReader.getProperty("default.password"));

        Assert.assertTrue(productsPage.isPageLoaded(), "Products page did not load");
        LoginPage backToLoginPage = productsPage.logout();

        Assert.assertTrue(backToLoginPage.isLoginPageDisplayed(), "User was not returned to login page after logout");
    }

    @DataProvider(name = "invalidLoginCredentials")
    public Object[][] getInvalidLoginData() {
        return new Object[][] {
            {"locked_out_user", "secret_sauce", "Sorry, this user has been locked out"},
            {"invalid_user_99", "secret_invalid", "Username and password do not match"},
            {"", "secret_sauce", "Username is required"},
            {"standard_user", "", "Password is required"}
        };
    }

    @Test(priority = 7, dataProvider = "invalidLoginCredentials", description = "Data-driven verification of login failure scenarios")
    public void testDataDrivenLoginValidation(String username, String password, String expectedErrorMessage) {
        LoginPage loginPage = new LoginPage();
        if (!username.isEmpty()) {
            loginPage.enterUsername(username);
        }
        if (!password.isEmpty()) {
            loginPage.enterPassword(password);
        }
        loginPage.clickLoginExpectingFailure();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedErrorMessage),
                String.format("Expected '%s' in error message but got '%s'", expectedErrorMessage, loginPage.getErrorMessage()));
    }
}

