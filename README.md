# Seasalt Selenium Cucumber Tests

Automated UI tests for the Seasalt Cornwall purchase journey, built with Java, Maven, Selenium WebDriver, Cucumber, JUnit Platform, and the Page Object Model pattern.

## What This Project Covers

- Opens the Seasalt Cornwall login page
- Handles optional cookie banners and popups
- Logs in with the test user from the feature file
- Searches for a product
- Selects the first product result
- Adds the product to the basket
- Verifies the product was added successfully
- Proceeds towards checkout
- Enters delivery details when the checkout page asks for them
- Generates Cucumber HTML and JSON reports

## Prerequisites

Install these before running the project locally:

- Git
- Java 17 or later
- Maven 3.6.3 or later
- Google Chrome

Check your installed versions:

```bash
java -version
mvn -version
git --version
```

The project uses WebDriverManager, so you do not need to download ChromeDriver manually.

## Clone The Project

Clone the repository and move into the project folder:

```bash
git clone <repository-url>
cd seasalt-selenium-cucumber-tests
```

Replace `<repository-url>` with the Git URL for this repository.

## Install Dependencies

Maven downloads the required dependencies automatically. To compile the test code without running the browser tests:

```bash
mvn clean test-compile
```

## Run The Tests

Run the full Selenium/Cucumber test suite:

```bash
mvn clean test
```

This opens Chrome and runs the live Seasalt Cornwall purchase-flow scenario defined in:

```text
src/test/resources/features/login.feature
```

## Run A Dry Check

To verify the Cucumber feature and step definitions are wired correctly without opening Chrome:

```bash
mvn test -Dcucumber.execution.dry-run=true
```

## Test Reports

After `mvn clean test` finishes, reports are generated in:

```text
target/cucumber-report.html
target/cucumber-report.json
```

Open `target/cucumber-report.html` in a browser to view the HTML report.

## Project Structure

```text
seasalt-selenium-cucumber-tests
├── pom.xml
├── README.md
└── src
    └── test
        ├── java
        │   └── com
        │       └── seasalt
        │           ├── hooks
        │           │   └── Hooks.java
        │           ├── pages
        │           │   ├── BasePage.java
        │           │   ├── BasketPage.java
        │           │   ├── CheckoutPage.java
        │           │   ├── HomePage.java
        │           │   ├── LoginPage.java
        │           │   ├── ProductDetailsPage.java
        │           │   └── SearchResultsPage.java
        │           ├── runners
        │           │   └── TestRunner.java
        │           ├── stepdefinitions
        │           │   └── SeasaltPurchaseJourneySteps.java
        │           └── utils
        │               └── DriverFactory.java
        └── resources
            └── features
                └── login.feature
```

## Useful Commands

Compile only:

```bash
mvn clean test-compile
```

Run all tests:

```bash
mvn clean test
```

Check dependency and plugin updates:

```bash
mvn versions:display-dependency-updates versions:display-plugin-updates
```

## Troubleshooting

If Chrome does not open, make sure Google Chrome is installed and up to date.

If Maven fails before running tests, check that Java 17+ and Maven 3.6.3+ are active in your terminal.

If the test fails on the website, the site may have changed its layout, blocked the test account, changed product availability, or shown a new popup. Start by checking the locators in:

```text
src/test/java/com/seasalt/pages
```
