package com.seasalt.steps;

import com.seasalt.pages.HomePage;
import com.seasalt.pages.LoginPage;
import com.seasalt.utils.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    private final HomePage homePage = new HomePage(DriverFactory.getDriver());
    private final LoginPage loginPage = new LoginPage(DriverFactory.getDriver());

    @Given("user opens the Seasalt Cornwall home page")
    public void userOpensTheSeasaltCornwallHomePage() {
        homePage.openHomePage();
        homePage.acceptCookiesIfVisible();
        homePage.closePopupIfVisible();
    }

    @When("user navigates to the login page from the header")
    public void userNavigatesToTheLoginPageFromTheHeader() {
        homePage.goToLoginPageFromHeader();
    }

    @When("user searches for product {string}")
    public void userSearchesForProduct(String productName) {
        homePage.searchForProduct(productName);
    }

    @When("user selects the product")
    public void userSelectsTheProduct() {
        homePage.selectProduct();
    }

    @When("user adds the product to the basket")
    public void userAddsTheProductToTheBasket() {
        homePage.addProductToBasket();
    }

    @Then("the product should be added to the basket successfully")
    public void theProductShouldBeAddedToTheBasketSuccessfully() {
        assertTrue(
                "Expected the product to be added to the basket successfully.",
                homePage.isProductAddedToBasket()
        );
    }

    @Given("user opens the Seasalt Cornwall login page")
    public void userOpensTheSeasaltCornwallLoginPage() {
        loginPage.openLoginPageDirectly();
        loginPage.closePopupIfVisible();
    }

    @Then("login form should be displayed")
    public void loginFormShouldBeDisplayed() {
        loginPage.waitUntilLoginFormIsVisible();
    }

    @When("user logs in with email {string} and password {string}")
    public void userLogsInWithEmailAndPassword(String email, String password) {
        loginPage.login(email, password);
    }

    @Then("user should see account dashboard or login error message")
    public void userShouldSeeAccountDashboardOrLoginErrorMessage() {
        boolean loginSucceeded = loginPage.isAccountDashboardDisplayed();
        boolean loginFailedWithMessage = loginPage.isLoginErrorDisplayed();

        assertTrue(
                "Expected either successful account dashboard or a visible login error message.",
                loginSucceeded || loginFailedWithMessage
        );
    }

    @Then("search results should be displayed for {string}")
    public void searchResultsShouldBeDisplayedFor(String productName) {
        assertTrue(
                "Expected search results page to be displayed for product: " + productName,
                homePage.isSearchResultsPageDisplayedFor(productName)
        );
    }
}
