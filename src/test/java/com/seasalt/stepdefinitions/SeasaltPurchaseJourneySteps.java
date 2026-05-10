package com.seasalt.stepdefinitions;

import com.seasalt.pages.BasketPage;
import com.seasalt.pages.CheckoutPage;
import com.seasalt.pages.HomePage;
import com.seasalt.pages.LoginPage;
import com.seasalt.pages.ProductDetailsPage;
import com.seasalt.pages.SearchResultsPage;
import com.seasalt.utils.DriverFactory;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.Duration;

public class SeasaltPurchaseJourneySteps {

    private final HomePage homePage = new HomePage(DriverFactory.getDriver());
    private final LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
    private final SearchResultsPage searchResultsPage = new SearchResultsPage(DriverFactory.getDriver());
    private final ProductDetailsPage productDetailsPage = new ProductDetailsPage(DriverFactory.getDriver());
    private final BasketPage basketPage = new BasketPage(DriverFactory.getDriver());
    private final CheckoutPage checkoutPage = new CheckoutPage(DriverFactory.getDriver());

    @Given("user opens the Seasalt Cornwall login page")
    public void userOpensTheSeasaltCornwallLoginPage() {
        loginPage.openLoginPageDirectly();
    }

    @When("user logs in with email {string} and password {string}")
    public void userLogsInWithEmailAndPassword(String email, String password) {
        loginPage.login(email, password);
    }

    @When("user searches for product {string}")
    public void userSearchesForProduct(String productName) {
        homePage.searchForProduct(productName);
    }

    @When("user selects the product")
    public void userSelectsTheProduct() {
        searchResultsPage.selectFirstProduct();
    }

    @When("user adds the product to the basket")
    public void userAddsTheProductToTheBasket() {
        productDetailsPage.addProductToBasket();
    }

    @When("Click on the basket icon")
    public void clickOnTheBasketIcon() {
        checkoutPage.clickBasketIcon();
    }

    @When("Click on the Continue to checkout button")
    public void clickOnTheContinueToCheckoutButton() {
        checkoutPage.clickContinueToCheckoutButton();
    }

    @When("user chooses DELIVERY option")
    public void userChoosesDeliveryOption() {
        checkoutPage.chooseDeliveryOption();
    }

    @When("Wait for {int} minutes")
    public void waitForMinutes(int minutes) throws InterruptedException {
        Thread.sleep(Duration.ofMinutes(minutes).toMillis());
    }

    @When("click on REVIEW AND PAY button")
    public void clickOnReviewAndPayButton() {
        checkoutPage.clickReviewAndPayButton();
    }

}
