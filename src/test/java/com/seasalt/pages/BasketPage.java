package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BasketPage extends BasePage {

    private final By basketSuccessMessage = By.cssSelector(
            ".message-success, [data-ui-id='message-success'], [role='alert']"
    );
    private final By basketCounter = By.cssSelector(
            ".counter-number, .counter.qty, [data-block='minicart'] .counter-number"
    );
    private final By miniBasket = By.cssSelector(
            "[data-block='minicart'], .minicart-wrapper"
    );

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductAddedToBasket() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.textToBePresentInElementLocated(basketSuccessMessage, "basket"),
                    ExpectedConditions.textToBePresentInElementLocated(basketSuccessMessage, "cart"),
                    ExpectedConditions.visibilityOfElementLocated(basketCounter),
                    ExpectedConditions.visibilityOfElementLocated(miniBasket),
                    ExpectedConditions.urlContains("checkout/cart")
            ));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
