package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;

public class CheckoutInformationPage extends BasePage {

    // Locators
    private final By titleSpan = By.cssSelector("[data-test='title'], .title");
    private final By firstNameInput = By.cssSelector("[data-test='firstName'], #first-name");
    private final By lastNameInput = By.cssSelector("[data-test='lastName'], #last-name");
    private final By postalCodeInput = By.cssSelector("[data-test='postalCode'], #postal-code");
    private final By continueButton = By.cssSelector("[data-test='continue'], #continue");
    private final By cancelButton = By.cssSelector("[data-test='cancel'], #cancel");
    private final By errorMessageContainer = By.cssSelector("[data-test='error']");

    public boolean isPageLoaded() {
        return WaitUtils.waitForUrlContains("checkout-step-one.html", 5) || WaitUtils.waitForTextToBePresent(titleSpan, "Checkout: Your Information", 5);
    }

    public CheckoutInformationPage fillInformation(String firstName, String lastName, String postalCode) {
        WaitUtils.waitForVisibility(firstNameInput, 5);
        logger.info("Entering checkout information: {} {}, {}", firstName, lastName, postalCode);
        if (firstName != null && !firstName.isEmpty()) {
            ElementUtils.sendKeys(firstNameInput, firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            ElementUtils.sendKeys(lastNameInput, lastName);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            ElementUtils.sendKeys(postalCodeInput, postalCode);
        }
        return this;
    }

    public CheckoutOverviewPage clickContinue() {
        logger.info("Clicking continue button on checkout information page");
        ElementUtils.clickWithJS(continueButton);
        CheckoutOverviewPage page = new CheckoutOverviewPage();
        page.isPageLoaded();
        return page;
    }

    public void clickContinueExpectingFailure() {
        logger.info("Clicking continue expecting validation error");
        ElementUtils.clickWithJS(continueButton);
    }

    public String getErrorMessage() {
        WaitUtils.waitForVisibility(errorMessageContainer);
        String error = ElementUtils.getText(errorMessageContainer);
        logger.info("Checkout validation error displayed: {}", error);
        return error;
    }

    public CartPage clickCancel() {
        logger.info("Canceling checkout information form");
        ElementUtils.clickWithJS(cancelButton);
        CartPage page = new CartPage();
        page.isPageLoaded();
        return page;
    }
}
