package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends BasePage {

    private final By productResultLinks = By.cssSelector(
            "li.product-item a.product-item-link, .product-item-info a.product-item-link"
    );

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public void selectFirstProduct() {
        WebElement product = wait.until(driver -> driver.findElements(productResultLinks).stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .findFirst()
                .orElse(null));

        scrollIntoView(product);
        try {
            product.click();
        } catch (Exception exception) {
            clickWithJavaScript(product);
        }
    }
}
