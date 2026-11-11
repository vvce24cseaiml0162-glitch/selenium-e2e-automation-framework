package com.qa.tests;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.CartPage;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.ProductsPage;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CartWorkflowTests extends BaseTest {
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

    @Test(priority = 1, description = "Verify adding multiple items to cart and verifying in cart page")
    public void testAddMultipleItemsToCart() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bike Light";

        productsPage.addProductToCart(item1)
                    .addProductToCart(item2);

        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge count is not 2");

        CartPage cartPage = productsPage.openCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page is not loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart item count is not 2");
        Assert.assertTrue(cartPage.isProductInCart(item1), "Item 1 is missing in cart");
        Assert.assertTrue(cartPage.isProductInCart(item2), "Item 2 is missing in cart");
    }

    @Test(priority = 2, description = "Verify removing item from within the cart page")
    public void testRemoveItemFromCartPage() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bolt T-Shirt";

        productsPage.addProductToCart(item1)
                    .addProductToCart(item2);

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Initial cart count should be 2");

        cartPage.removeProduct(item1);
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart count should be 1 after removing 1 item");
        Assert.assertFalse(cartPage.isProductInCart(item1), "Removed item should not be present in cart");
        Assert.assertTrue(cartPage.isProductInCart(item2), "Remaining item should be present in cart");
    }

    @Test(priority = 3, description = "Verify continue shopping navigates back to products catalog")
    public void testContinueShoppingNavigation() {
        CartPage cartPage = productsPage.openCart();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page not loaded");

        ProductsPage returnedProductsPage = cartPage.clickContinueShopping();
        Assert.assertTrue(returnedProductsPage.isPageLoaded(), "Products page not loaded after clicking Continue Shopping");
    }
}

