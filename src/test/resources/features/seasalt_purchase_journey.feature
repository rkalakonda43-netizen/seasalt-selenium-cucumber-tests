Feature: Seasalt Cornwall purchase journey

  Background:
    Given user opens the Seasalt Cornwall login page

  @smoke @purchase
  Scenario: Registered user reaches final payment step for a purchase journey
    When user logs in with email "TestUser2000@seasaltcornwall.co.uk" and password "User@1234"
    And user searches for product "Men's Tiek Cap"
    And user selects the product
    And user adds the product to the basket
    And Click on the basket icon
    And Click on the Continue to checkout button
    And user chooses DELIVERY option
    And click on REVIEW AND PAY button
    # And user enters test card details "5555 4444 3333 1111", expiry "03/30" and cvv "737"
    # And Click on the PLACE ORDER button
    # And Wait for 2 minutes
