package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By completeHeader = By.cssSelector("[data-test='complete-header'], .complete-header");
    private final By completeText = By.cssSelector("[data-test='complete-text'], .complete-text");
    private final By backHomeButton = By.cssSelector("[data-test='back-to-products'], #back-to-products");
    private final By ponyExpressImage = By.cssSelector("[data-test='pony-express'], .pony_express");

    public boolean isOrderComplete() {
        return ElementUtils.isDisplayed(completeHeader);
    }

    public String getCompleteHeaderText() {
        return ElementUtils.getText(completeHeader);
    }

    public String getCompleteDescriptionText() {
        return ElementUtils.getText(completeText);
    }

    public ProductsPage clickBackHome() {
        logger.info("Clicking back home button from order complete page");
        ElementUtils.clickWithJS(backHomeButton);
        WaitUtils.waitForUrlContains("inventory.html", 5);
        return new ProductsPage();
    }
}

