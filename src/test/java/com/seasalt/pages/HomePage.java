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

    private static final String HOME_URL = "https://www.seasaltcornwall.com/";
    private static final String SEARCH_URL = HOME_URL + "catalogsearch/result/?q=";

    private final By accountIcon = By.cssSelector("a[href*='customer/account/login'], a[href*='customer/account']");
    private final By searchField = By.cssSelector(
            "input.input-search[name='q'], input[name='q'][aria-label='Search our products here'], #search"
    );
    private final By searchButton = By.cssSelector(
            "button[type='submit'], button[aria-label*='Search'], button[title*='Search'], .search-icon"
    );
    private final By searchForm = By.cssSelector("form[action*='catalogsearch'], form#search_mini_form");
    private final By productResultLinks = By.cssSelector(
            "li.product-item a.product-item-link, .product-item-info a.product-item-link"
    );
    private final By addToBasketButton = By.cssSelector(
            "#product-addtocart-button, button[name='add'][type='submit'], button[title='Add to Basket']"
    );
    private final By basketSuccessMessage = By.cssSelector(
            ".message-success, [data-ui-id='message-success'], [role='alert']"
    );
    private final By basketCounter = By.cssSelector(
            ".counter-number, .counter.qty, [data-block='minicart'] .counter-number"
    );
    private final By miniBasket = By.cssSelector(
            "[data-block='minicart'], .minicart-wrapper, a[href*='checkout/cart']"
    );

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openHomePage() {
        driver.get(HOME_URL);
        acceptCookiesIfVisible();
        closePopupIfVisible();
    }

    public void goToLoginPageFromHeader() {
        acceptCookiesIfVisible();
        closePopupIfVisible();
        click(accountIcon);
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

    public boolean isSearchResultsPageDisplayedFor(String productName) {
        return driver.getCurrentUrl().contains("q=")
                || driver.getTitle().toLowerCase().contains(productName.toLowerCase());
    }

    public void selectProduct() {
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

    public void addProductToBasket() {
        WebElement addToBasket = waitForClickable(addToBasketButton);
        scrollIntoView(addToBasket);

        try {
            addToBasket.click();
        } catch (Exception exception) {
            clickWithJavaScript(addToBasket);
        }
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
