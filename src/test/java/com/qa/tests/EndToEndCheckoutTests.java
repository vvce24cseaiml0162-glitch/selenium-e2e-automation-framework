package com.qa.tests;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.*;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EndToEndCheckoutTests extends BaseTest {
    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginToApp() {
        LoginPage loginPage = new LoginPage();
        productsPage = loginPage.loginAs(
                ConfigReader.getProperty("default.username"),
                ConfigReader.getProperty("default.password")
        );
        Assert.assertTrue(productsPage.isPageLoaded(), "Products page not loaded");
    }

    @Test(priority = 1, description = "Complete positive End-to-End checkout and purchase workflow")
    public void testPositiveEndToEndPurchaseFlow() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Fleece Jacket";

        // Step 1: Add items
        productsPage.addProductToCart(item1)
                    .addProductToCart(item2);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge should show 2");

        // Step 2: Open Cart
        CartPage cartPage = productsPage.openCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items");

        // Step 3: Checkout Information
        CheckoutInformationPage infoPage = cartPage.clickCheckout();
        Assert.assertTrue(infoPage.isPageLoaded(), "Checkout info page should be loaded");

        infoPage.fillInformation("John", "Doe", "94016");
        CheckoutOverviewPage overviewPage = infoPage.clickContinue();

        // Step 4: Checkout Overview & Verification
        Assert.assertTrue(overviewPage.isPageLoaded(), "Checkout overview page should be loaded");
        Assert.assertTrue(overviewPage.getSubtotalText().contains("79.98"),
                "Subtotal does not match expected items cost ($29.99 + $49.99 = $79.98)");
        Assert.assertTrue(overviewPage.getTotalText().contains("86.38"),
                "Total with tax does not match ($79.98 + $6.40 = $86.38)");

        // Step 5: Finish Order
        CheckoutCompletePage completePage = overviewPage.clickFinish();
        Assert.assertTrue(completePage.isOrderComplete(), "Order completion page not displayed");
        Assert.assertEquals(completePage.getCompleteHeaderText(), "Thank you for your order!",
                "Order success header text mismatch");

        // Step 6: Return Home
        ProductsPage finalProductsPage = completePage.clickBackHome();
        Assert.assertTrue(finalProductsPage.isPageLoaded(), "Did not return to products page after finishing order");
        Assert.assertEquals(finalProductsPage.getCartBadgeCount(), 0, "Cart should be empty after completing purchase");
    }

    @Test(priority = 2, description = "Verify checkout validation when Postal Code is missing")
    public void testCheckoutFormValidationMissingPostalCode() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = productsPage.openCart();
        CheckoutInformationPage infoPage = cartPage.clickCheckout();

        infoPage.fillInformation("Jane", "Doe", "");
        infoPage.clickContinueExpectingFailure();

        Assert.assertTrue(infoPage.getErrorMessage().contains("Postal Code is required"),
                "Expected postal code required error, found: " + infoPage.getErrorMessage());
    }

    @Test(priority = 3, description = "Verify checkout validation when First Name is missing")
    public void testCheckoutFormValidationMissingFirstName() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        CartPage cartPage = productsPage.openCart();
        CheckoutInformationPage infoPage = cartPage.clickCheckout();

        infoPage.fillInformation("", "Smith", "10001");
        infoPage.clickContinueExpectingFailure();

        Assert.assertTrue(infoPage.getErrorMessage().contains("First Name is required"),
                "Expected first name required error, found: " + infoPage.getErrorMessage());
    }
}

