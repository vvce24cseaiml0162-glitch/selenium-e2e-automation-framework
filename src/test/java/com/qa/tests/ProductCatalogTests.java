package com.qa.tests;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.pages.LoginPage;
import com.qa.framework.pages.ProductsPage;
import com.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductCatalogTests extends BaseTest {
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

    @Test(priority = 1, description = "Verify product inventory list count")
    public void testProductListCount() {
        int count = productsPage.getProductCount();
        Assert.assertEquals(count, 6, "Expected 6 products in catalog, but found: " + count);
    }

    @Test(priority = 2, description = "Verify adding and removing product updates the cart badge")
    public void testAddAndRemoveProductFromCatalog() {
        String product = "Sauce Labs Backpack";
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Initial cart badge should be 0");

        productsPage.addProductToCart(product);
        int badgeAfterAdd = productsPage.waitForCartBadgeCount(1, 3);
        Assert.assertEquals(badgeAfterAdd, 1, "Cart badge count did not update to 1");

        productsPage.removeProductFromInventory(product);
        int badgeAfterRemove = productsPage.waitForCartBadgeCount(0, 3);
        Assert.assertEquals(badgeAfterRemove, 0, "Cart badge count did not reset to 0");
    }

    @Test(priority = 3, description = "Verify sorting products by Name Z to A")
    public void testSortProductsByNameZtoA() {
        productsPage.selectSortOption("za");
        String firstItem = productsPage.getFirstProductTitle();
        Assert.assertEquals(firstItem, "Test.allTheThings() T-Shirt (Red)", "Sorting Z to A failed");
    }

    @Test(priority = 4, description = "Verify sorting products by Price Low to High")
    public void testSortProductsByPriceLowToHigh() {
        productsPage.selectSortOption("lohi");
        String firstItem = productsPage.getFirstProductTitle();
        Assert.assertEquals(firstItem, "Sauce Labs Onesie", "Sorting Low to High failed");
    }
}
