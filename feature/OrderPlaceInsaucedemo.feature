###Feature File #####
Feature: Verify The Page Title

@VerifyTitle
          Scenario: Verify the Page Title of given URL
          Then Verify the Page Title
		     
		     
   @LoginToSaucedemo
  Scenario Outline: Login to saucedemo application
    Given Enter username as <username> and password as <password>
    When Click on Login button
    Then Verify that user logged In successfully
    Examples: 
      | username  | password | 
      | standard_user| secret_sauce| 
      
      
    @AddMultipleItemsToCart  
    Scenario Outline: Add Multiple Items To Cart
    Given Enter username as <username> and password as <password>
    And Click on Login button
    When Add Multiple Items to cart
    Then Verify that Items are added to cart
    Examples: 
      | username  | password | 
      | standard_user| secret_sauce| 
    
     @PlaceOrder  
    Scenario Outline: Place Order with Multiple Items
    Given Enter username as <username> and password as <password>
    And Click on Login button
    And Add Multiple Items to cart
    And Verify that Items are added to cart
    And Click on cart icon
    And Click on Checkout button
    And Provide UsSer Information
    And Click on Continue button
    When Click on Finish button
    Then Validate the confirmation Message of order placement
    
    Examples: 
      | username  | password | 
      | standard_user| secret_sauce| 