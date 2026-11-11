package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import org.openqa.selenium.By;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");
    private final By ponyExpressImage = By.className("pony_express");

    public boolean isOrderComplete() {
        return ElementUtils.isDisplayed(completeHeader) && ElementUtils.isDisplayed(ponyExpressImage);
    }

    public String getCompleteHeaderText() {
        return ElementUtils.getText(completeHeader);
    }

    public String getCompleteDescriptionText() {
        return ElementUtils.getText(completeText);
    }

    public ProductsPage clickBackHome() {
        logger.info("Clicking back home button from order complete page");
        ElementUtils.click(backHomeButton);
        return new ProductsPage();
    }
}

