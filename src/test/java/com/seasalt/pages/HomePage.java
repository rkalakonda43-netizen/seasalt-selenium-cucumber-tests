package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage {

    private static final String SEARCH_URL = "https://www.seasaltcornwall.com/catalogsearch/result/?q=";

    private final By searchField = By.cssSelector(
            "input.input-search[name='q'], input[name='q'][aria-label='Search our products here'], #search"
    );
    private final By searchButton = By.cssSelector(
            "button[type='submit'], button[aria-label*='Search'], button[title*='Search'], .search-icon"
    );
    private final By searchForm = By.cssSelector("form[action*='catalogsearch'], form#search_mini_form");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchForProduct(String productName) {
        WebElement field = waitForVisible(searchField);
        field.clear();
        setSearchFieldValue(field, productName);

        clickSearchIcon(field);

        if (isSearchResultsLoaded(productName, 5)) {
            return;
        }

        submitVisibleSearchForm();

        if (isSearchResultsLoaded(productName, 5)) {
            return;
        }

        driver.get(SEARCH_URL + URLEncoder.encode(productName, StandardCharsets.UTF_8));
        waitForSearchResults(productName);
    }

    private void submitVisibleSearchForm() {
        for (WebElement form : driver.findElements(searchForm)) {
            if (form.isDisplayed()) {
                submitFormWithJavaScript(form);
                return;
            }
        }
    }

    private void clickSearchIcon(WebElement field) {
        try {
            WebElement form = field.findElement(By.xpath("./ancestor::form[1]"));
            List<WebElement> buttons = form.findElements(searchButton);

            for (WebElement button : buttons) {
                if (button.isDisplayed() && button.isEnabled()) {
                    try {
                        button.click();
                    } catch (Exception exception) {
                        clickWithJavaScript(button);
                    }
                    return;
                }
            }
        } catch (Exception exception) {
            clickFirstVisibleElement(searchButton, 3);
        }
    }

    private void setSearchFieldValue(WebElement field, String productName) {
        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];" +
                        "const value = arguments[1];" +
                        "const setter = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value').set;" +
                        "setter.call(input, value);" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "input.dispatchEvent(new Event('change', { bubbles: true }));",
                field,
                productName
        );
    }

    private boolean isSearchResultsLoaded(String productName, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("q="),
                    ExpectedConditions.titleContains(productName)
            ));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    private void waitForSearchResults(String productName) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("q="),
                ExpectedConditions.titleContains(productName)
        ));
    }
}
