package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.base.DriverFactory;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {

    // Locators
    private final By titleSpan = By.cssSelector("[data-test='title'], .title");
    private final By inventoryItems = By.className("inventory_item");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By sortSelect = By.cssSelector("[data-test='product-sort-container']");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutSidebarLink = By.id("logout_sidebar_link");
    private final By itemNames = By.className("inventory_item_name");

    public boolean isPageLoaded() {
        return WaitUtils.waitForUrlContains("inventory.html", 5) || WaitUtils.waitForTextToBePresent(titleSpan, "Products", 5);
    }

    public int getProductCount() {
        return ElementUtils.getElementCount(inventoryItems);
    }

    public String getFirstProductTitle() {
        List<WebElement> items = WaitUtils.waitForAllVisible(itemNames);
        return items.isEmpty() ? "" : items.get(0).getText().trim();
    }

    public ProductsPage addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);
        String addToCartButtonSelector = String.format("//div[normalize-space()='%s']/ancestor::div[contains(@class,'inventory_item')]//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add to cart')]", productName);
        ElementUtils.click(By.xpath(addToCartButtonSelector));
        return this;
    }

    public ProductsPage removeProductFromInventory(String productName) {
        logger.info("Removing product from inventory: {}", productName);
        String removeButtonSelector = String.format("//div[normalize-space()='%s']/ancestor::div[contains(@class,'inventory_item')]//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'remove')]", productName);
        ElementUtils.click(By.xpath(removeButtonSelector));
        return this;
    }

    public int getCartBadgeCount() {
        try {
            List<WebElement> badges = DriverFactory.getDriver().findElements(shoppingCartBadge);
            if (!badges.isEmpty() && badges.get(0).isDisplayed()) {
                String text = badges.get(0).getText().trim();
                return Integer.parseInt(text);
            }
        } catch (Exception ignored) {}
        return 0;
    }

    public int waitForCartBadgeCount(int expectedCount, int timeoutSeconds) {
        try {
            if (expectedCount == 0) {
                WaitUtils.waitForInvisibility(shoppingCartBadge, timeoutSeconds);
                return 0;
            } else {
                WaitUtils.waitForTextToBePresent(shoppingCartBadge, String.valueOf(expectedCount), timeoutSeconds);
                return expectedCount;
            }
        } catch (Exception e) {
            return getCartBadgeCount();
        }
    }

    public CartPage openCart() {
        logger.info("Opening shopping cart");
        ElementUtils.clickWithJS(shoppingCartLink);
        CartPage cartPage = new CartPage();
        cartPage.isPageLoaded();
        return cartPage;
    }

    public ProductsPage selectSortOption(String sortOptionValue) {
        logger.info("Sorting products by value: {}", sortOptionValue);
        ElementUtils.selectByValue(sortSelect, sortOptionValue);
        return this;
    }

    public LoginPage logout() {
        logger.info("Logging out from application");
        ElementUtils.click(burgerMenuButton);
        WaitUtils.waitForVisibility(logoutSidebarLink, 5);
        ElementUtils.clickWithJS(logoutSidebarLink);
        WaitUtils.waitForVisibility(By.id("login-button"), 5);
        return new LoginPage();
    }
}
