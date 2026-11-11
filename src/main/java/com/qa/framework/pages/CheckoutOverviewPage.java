package com.qa.framework.pages;

import com.qa.framework.base.BasePage;
import com.qa.framework.utils.ElementUtils;
import com.qa.framework.utils.WaitUtils;
import org.openqa.selenium.By;

public class CheckoutOverviewPage extends BasePage {

    // Locators
    private final By titleSpan = By.cssSelector("[data-test='title'], .title");
    private final By summarySubtotal = By.className("summary_subtotal_label");
    private final By summaryTax = By.className("summary_tax_label");
    private final By summaryTotal = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public boolean isPageLoaded() {
        return WaitUtils.waitForTextToBePresent(titleSpan, "Checkout: Overview", 5);
    }

    public String getSubtotalText() {
        return ElementUtils.getText(summarySubtotal);
    }

    public String getTaxText() {
        return ElementUtils.getText(summaryTax);
    }

    public String getTotalText() {
        return ElementUtils.getText(summaryTotal);
    }

    public CheckoutCompletePage clickFinish() {
        logger.info("Clicking finish order button");
        ElementUtils.click(finishButton);
        CheckoutCompletePage page = new CheckoutCompletePage();
        return page;
    }

    public ProductsPage clickCancel() {
        logger.info("Canceling checkout overview");
        ElementUtils.click(cancelButton);
        ProductsPage page = new ProductsPage();
        page.isPageLoaded();
        return page;
    }
}
