package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By cookieAcceptButtons = By.cssSelector(
            "#onetrust-accept-btn-handler, button[id*='accept'], button[class*='accept'], " +
            "button[aria-label*='Accept'], .accept-cookie, .cookie-accept"
    );
    private final By popupCloseButton = By.cssSelector(
            "#closeButton, button[aria-label='Close'], button[aria-label*='close'], " +
            ".close, .modal-close, .ins-close-button"
    );

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected boolean isDisplayed(By locator, int seconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void acceptCookiesIfVisible() {
        clickFirstVisibleElement(cookieAcceptButtons, 5);
    }

    public void closePopupIfVisible() {
        clickFirstVisibleElement(popupCloseButton, 4);
    }

    protected void clickFirstVisibleElement(By locator, int timeoutSeconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(locator));

            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (element.isDisplayed() && element.isEnabled()) {
                    try {
                        element.click();
                    } catch (Exception exception) {
                        clickWithJavaScript(element);
                    }
                    return;
                }
            }
        } catch (TimeoutException exception) {
            // Optional banner/popup was not shown. Continue the test.
        }
    }

    protected void clickWithJavaScript(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void submitFormWithJavaScript(WebElement form) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].submit();", form);
    }
}
