package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private final By titleSpan = By.cssSelector("[data-test='title'], .title");
    private final By cartItems = By.className("cart_item");
    private final By cartItemNames = By.className("inventory_item_name");
    private final By checkoutButton = By.cssSelector("[data-test='checkout'], #checkout");
    private final By continueShoppingButton = By.cssSelector("[data-test='continue-shopping'], #continue-shopping");

    public boolean isPageLoaded() {
        return WaitUtils.waitForUrlContains("cart.html", 5) || WaitUtils.waitForTextToBePresent(titleSpan, "Your Cart", 5);
    }

    public int getCartItemCount() {
        return ElementUtils.getElementCount(cartItems);
    }

    public boolean isProductInCart(String productName) {
        List<WebElement> names = ElementUtils.getElements(cartItemNames);
        return names.stream().anyMatch(e -> e.getText().trim().equalsIgnoreCase(productName.trim()));
    }

    public CartPage removeProduct(String productName) {
        logger.info("Removing product from cart: {}", productName);
        String removeButtonXPath = String.format("//div[normalize-space()='%s']/ancestor::div[contains(@class,'cart_item')]//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'remove')]", productName);
        ElementUtils.clickWithJS(By.xpath(removeButtonXPath));
        return this;
    }

    public CheckoutInformationPage clickCheckout() {
        logger.info("Proceeding to checkout");
        ElementUtils.clickWithJS(checkoutButton);
        WaitUtils.waitForUrlContains("checkout-step-one.html", 5);
        return new CheckoutInformationPage();
    }

    public ProductsPage clickContinueShopping() {
        logger.info("Navigating back to continue shopping");
        ElementUtils.clickWithJS(continueShoppingButton);
        WaitUtils.waitForUrlContains("inventory.html", 5);
        return new ProductsPage();
    }
}
