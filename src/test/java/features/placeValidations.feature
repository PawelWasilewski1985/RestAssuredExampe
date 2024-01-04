Feature: Validating place API

  Scenario Outline: Verifying whether place is correctly added via API
    Given Add Place payload with "<name>" "<language>" and "<address>"
    When User calls "AddPlaceApi" with "POST" http request
    Then The API call is success with status code 200
    And "status" in response body is "OK"
    And "scope" in response body is "APP"
    And Verify place_id created maps to "<name>" using "GetPlaceApi"

    Examples:
      | name          | language | address         |
      | Best GROWSHOP | polski   | Rawa Mazowiecka |