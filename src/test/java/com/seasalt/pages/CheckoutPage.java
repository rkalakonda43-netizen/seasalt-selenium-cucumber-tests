package com.seasalt.pages;

import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage extends BasePage {

    private final By miniCartButton = By.cssSelector(
            "#action-minicart-dropdown, [data-block='minicart'] a.action.showcart, a[title='View My Bag']"
    );
    private final By miniCartDropdown = By.cssSelector(".block-minicart, #minicart-content-wrapper");
    // Scoped to mini cart to avoid clicking hidden or unrelated cart links
    private final By viewShoppingBagButton = By.cssSelector(
            ".minicart-wrapper a[href*='checkout/cart'], " +
                    ".minicart-wrapper button.viewcart, " +
                    ".minicart-wrapper a.viewcart"
    );
    private final By proceedToCheckoutButton = By.cssSelector(
            "#top-cart-btn-checkout, button[data-role='proceed-to-checkout'], " +
                    "button.action.primary.checkout, button.checkout, button[title*='Checkout'], " +
                    ".checkout-methods-items button, .actions .checkout, [data-action='checkout'], " +
                    "a.action.primary.checkout, a.checkout, a[title*='Checkout'], " +
                    "a[href*='checkout'][class*='checkout']"
    );
    private final By deliveryOption = By.cssSelector(
            "#select-shipping-type-delivery, [aria-label='Select Delivery'], .select-shipping-type-item[id*='delivery']"
    );
    private final By firstNameField = By.cssSelector("input[name='firstname']");
    private final By lastNameField = By.cssSelector("input[name='lastname']");
    private final By streetAddressField = By.cssSelector("input[name='street[0]']");
    private final By cityField = By.cssSelector("input[name='city']");
    private final By postcodeField = By.cssSelector("input[name='postcode']");
    private final By phoneField = By.cssSelector("input[name='telephone']");
    private final By countryDropdown = By.cssSelector("select[name='country_id']");
    private final By shippingMethods = By.cssSelector("input[type='radio'][name*='shipping_method']");
    private final By nextButton = By.cssSelector(
            "button[data-role='opc-continue'], button.continue, button.action.continue, " +
                    "button[type='submit']"
    );
    private final By reviewAndPayButton = By.cssSelector(
            "#shipping-method-buttons-container button[data-role='opc-continue']"
    );

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void clickBasketIcon() {
        dismissInterruptions();

        try {
            WebElement miniCart = waitForClickable(miniCartButton);
            clickElement(miniCart);
            waitForMiniCartToOpenOrBasketPage();
        } catch (TimeoutException exception) {
            navigateToBasketPage();
            wait.until(ExpectedConditions.urlContains("/checkout/cart"));
        }
    }

    public void clickContinueToCheckoutButton() {
        dismissInterruptions();

        WebElement continueToCheckout = waitForProceedToCheckoutButton();
        clickElement(continueToCheckout);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/checkout"),
                ExpectedConditions.visibilityOfElementLocated(firstNameField)
        ));
    }

    public void clickViewShoppingBag() {
        waitForClickable(viewShoppingBagButton).click();
    }

    public void chooseDeliveryOption() {
        dismissInterruptions();

        WebElement delivery = waitForDeliveryOptionIfRequired();
        if (delivery == null) {
            return;
        }

        clickElement(delivery);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.attributeContains(deliveryOption, "class", "active"),
                ExpectedConditions.visibilityOfElementLocated(firstNameField)
        ));
    }

    public void clickReviewAndPayButton() {
        dismissInterruptions();

        WebElement reviewAndPay = wait.until(driver -> findFirstDisplayedAndEnabled(reviewAndPayButton));
        clickElement(reviewAndPay);
    }

    public void proceedThroughCheckout() {
        dismissInterruptions();

        if (isOnBasketPage()) {
            proceedFromBasketIfPossible();
            return;
        }

        try {
            clickBasketIcon();

            if (isOnBasketPage()) {
                return;
            }

            clickViewShoppingBag();
            wait.until(ExpectedConditions.urlContains("/checkout/cart"));
            proceedFromBasketIfPossible();
            return;
        } catch (TimeoutException exception) {
            // Some journeys do not show a minicart drawer after add-to-basket.
        }

        navigateToBasketPage();
        wait.until(ExpectedConditions.urlContains("/checkout/cart"));
        proceedFromBasketIfPossible();
    }

    public void enterDeliveryDetailsIfRequested() {
        dismissInterruptions();

        if (!isDisplayed(firstNameField, 8)) {
            return;
        }

        selectCountryIfVisible("GB");
        type(firstNameField, "Test");
        type(lastNameField, "User");
        type(streetAddressField, "1 Test Street");
        type(cityField, "Truro");
        type(postcodeField, "TR1 2AB");
        type(phoneField, "07123456789");
        selectFirstShippingMethodIfVisible();
        clickNextIfVisible();
    }

    private void selectCountryIfVisible(String countryCode) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement country = shortWait.until(ExpectedConditions.visibilityOfElementLocated(countryDropdown));
            new Select(country).selectByValue(countryCode);
        } catch (Exception exception) {
            // Country may already be selected or hidden for existing customer addresses.
        }
    }

    private void selectFirstShippingMethodIfVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement shippingMethod = shortWait.until(driver -> driver.findElements(shippingMethods).stream()
                    .filter(WebElement::isDisplayed)
                    .filter(WebElement::isEnabled)
                    .findFirst()
                    .orElse(null));

            if (!shippingMethod.isSelected()) {
                shippingMethod.click();
            }
        } catch (TimeoutException exception) {
            // Shipping method may already be selected or loaded later in checkout.
        }
    }

    private void clickNextIfVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement next = shortWait.until(driver -> driver.findElements(nextButton).stream()
                    .filter(WebElement::isDisplayed)
                    .filter(WebElement::isEnabled)
                    .findFirst()
                    .orElse(null));

            scrollIntoView(next);
            try {
                next.click();
            } catch (Exception exception) {
                clickWithJavaScript(next);
            }
        } catch (TimeoutException exception) {
            // Continue button may not be required when delivery details are already saved.
        }
    }

    private WebElement waitForProceedToCheckoutButton() {
        try {
            return wait.until(driver -> findFirstDisplayedAndEnabled(proceedToCheckoutButton));
        } catch (TimeoutException exception) {
            navigateToBasketPage();
            wait.until(ExpectedConditions.urlContains("/checkout/cart"));
            return wait.until(driver -> findFirstDisplayedAndEnabled(proceedToCheckoutButton));
        }
    }

    private WebElement waitForDeliveryOptionIfRequired() {
        wait.until(driver -> findFirstDisplayedAndEnabled(deliveryOption) != null
                || findFirstDisplayedAndEnabled(firstNameField) != null
                || findFirstDisplayedAndEnabled(reviewAndPayButton) != null);

        return findFirstDisplayedAndEnabled(deliveryOption);
    }

    private void clickElement(WebElement element) {
        scrollIntoView(element);
        try {
            element.click();
        } catch (Exception exception) {
            clickWithJavaScript(element);
        }
    }

    private void waitForMiniCartToOpenOrBasketPage() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        shortWait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(miniCartDropdown),
                ExpectedConditions.visibilityOfElementLocated(viewShoppingBagButton),
                ExpectedConditions.urlContains("/checkout/cart")
        ));
    }

    private WebElement findFirstDisplayedAndEnabled(By locator) {
        return driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .filter(WebElement::isEnabled)
                .findFirst()
                .orElse(null);
    }

    private boolean isOnBasketPage() {
        return driver.getCurrentUrl().contains("/checkout/cart");
    }

    private void proceedFromBasketIfPossible() {
        try {
            clickContinueToCheckoutButton();
        } catch (TimeoutException exception) {
            // Basket may already have moved on, or checkout may be unavailable for the current cart.
        }
    }

    private void navigateToBasketPage() {
        URI currentUri = URI.create(driver.getCurrentUrl());
        driver.get(currentUri.resolve("/checkout/cart").toString());
    }

}
