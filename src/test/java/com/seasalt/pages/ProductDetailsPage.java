package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends BasePage {

    private final By addToBasketButton = By.cssSelector(
            "#product-addtocart-button, button[name='add'][type='submit'], button[title='Add to Basket']"
    );

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void addProductToBasket() {
        WebElement addToBasket = waitForClickable(addToBasketButton);
        scrollIntoView(addToBasket);

        try {
            addToBasket.click();
        } catch (Exception exception) {
            clickWithJavaScript(addToBasket);
        }
    }
}
