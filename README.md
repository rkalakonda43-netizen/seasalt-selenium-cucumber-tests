# Seasalt Cornwall Login Automation - Java Selenium POM

This is a Maven, Java, Selenium WebDriver, Cucumber, and Page Object Model project for the Seasalt Cornwall login journey.

## What this project covers

- Open Seasalt Cornwall login page directly
- Handle optional popups
- Wait for email and password fields
- Enter email and password
- Click Sign In
- Verify either login success or visible login error
- Generate a Cucumber HTML report

## Project structure

```text
seasalt-login-pom
├── pom.xml
├── README.md
└── src
    └── test
        ├── java
        │   └── com
        │       └── seasalt
        │           ├── pages
        │           │   ├── BasePage.java
        │           │   ├── HomePage.java
        │           │   └── LoginPage.java
        │           ├── runners
        │           │   └── TestRunner.java
        │           ├── steps
        │           │   ├── Hooks.java
        │           │   └── LoginSteps.java
        │           └── utils
        │               └── DriverFactory.java
        └── resources
            └── features
                └── login.feature
```

## Requirements

Install these before running:

- Java 17 or above
- Maven
- Google Chrome
- VS Code extensions:
  - Extension Pack for Java
  - Cucumber
  - Maven for Java

## How to run from terminal

Open the project folder in VS Code, then run:

```bash
mvn clean test
```

## Report location

After the test finishes, open:

```text
target/cucumber-report.html
```

## Important note

The website may block test credentials, show extra popups, or change element locators. If that happens, update the locators in:

```text
src/test/java/com/seasalt/pages/LoginPage.java
src/test/java/com/seasalt/pages/HomePage.java
```
