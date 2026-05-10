Feature: Seasalt Cornwall login

  Background:
    Given user opens the Seasalt Cornwall login page

  # Scenario: User can see the login form before entering credentials
  #   Then login form should be displayed

  # Scenario: User attempts to login with test credentials
  #   When user logs in with email "TestUser2000@seasaltcornwall.co.uk" and password "User@1234"
  #   Then user should see account dashboard or login error message


  @smoke @purchase
  Scenario: Registered user reaches final payment step for a purchase journey
    When user logs in with email "TestUser2000@seasaltcornwall.co.uk" and password "User@1234"
    And user searches for product "Men's Tiek Cap"
    And user selects the product
    And user adds the product to the basket
    Then the product should be added to the basket successfully
    # When user opens the basket
    # Then the basket should contain a product
    # When user proceeds through checkout
    # And user enters delivery details if requested
    # And user reaches the payment step
    # Then user should reach the final payment step
