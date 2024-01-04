Feature: Heroekuapp API

  Scenario: Add new book
    Given Add Book payload to add new book
    When User calls "AddBookRequest" with "POST" http request HK
    Then HTTP status of the response is 200
    And field "bookingid" exists in response
    And field "booking" exists in response
    And value for bookingid is an integer

  Scenario: Get newly added book
    Given Add Book payload to add new book
    And User calls "AddBookRequest" with "POST" http request HK
    When User calls "GetBook" by id
    Then HTTP status of the response is 200
    And response contains all required fields

  Scenario: Update existed book
    Given User calls "GetAllBooks" with "GET" http request HK
    And User picks random book id
    And User calls "GetBook" by random id
    And Save values of book before updating
    And Send request to create token for updating book
    When User calls "UpdateBook" with "PUT" http request HK
    Then User calls "GetBook" by random id
    And Verify first name is correctly updated
    And Verify last name is correctly updated
    And Verify total price is correctly updated

  Scenario: Delete book
    Given Add Book payload to add new book
    And User calls "AddBookRequest" with "POST" http request HK
    And User calls "GetBook" by id
    And Send request to create token for updating book
    When User calls "DeleteBook" with "DELETE" http request HK
    And HTTP status of the response is 201


