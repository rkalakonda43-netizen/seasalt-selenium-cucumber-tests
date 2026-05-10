package com.seasalt.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "https://www.seasaltcornwall.com/customer/account/login/";

    private final By loginForm = By.cssSelector(".login-container form#login-form");
    private final By emailField = By.cssSelector(".login-container form#login-form input[name='login[username]']");
    private final By passwordField = By.cssSelector(".login-container form#login-form input[name='login[password]']");
    private final By signInButton = By.cssSelector(".login-container form#login-form button[name='send']");
    private final By accountDashboard = By.cssSelector(".account, .dashboard, .account-dashboard");
    private final By errorMessage = By.cssSelector(".message-error, .messages .error, div[data-ui-id='message-error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openLoginPageDirectly() {
        driver.get(LOGIN_URL);
        acceptCookiesIfVisible();
        closePopupIfVisible();
    }

    public void waitUntilLoginFormIsVisible() {
        waitForVisible(emailField);
        waitForVisible(passwordField);
    }

    public void enterEmail(String email) {
        type(emailField, email);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickSignIn() {
        WebElement form = waitForVisible(loginForm);
        scrollIntoView(form);

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            shortWait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        } catch (TimeoutException exception) {
            submitFormWithJavaScript(form);
        }
    }

    public void login(String email, String password) {
        acceptCookiesIfVisible();
        closePopupIfVisible();
        waitUntilLoginFormIsVisible();
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }

    public boolean isLoginErrorDisplayed() {
        return isDisplayed(errorMessage, 5);
    }

    public boolean isAccountDashboardDisplayed() {
        return isDisplayed(accountDashboard, 10);
    }
}
