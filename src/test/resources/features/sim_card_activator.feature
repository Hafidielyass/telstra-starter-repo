Feature: SIM Card Activation
  As a telecommunications provider
  I want to activate SIM cards
  So that customers can use their SIM cards

  Scenario: Successfully activate a SIM card
    When I submit an activation request with ICCID "1255789453849037777" and email "success@example.com"
    Then the activation response should indicate success
    And when I query the record with ID 1
    Then the returned record should have ICCID "1255789453849037777", email "success@example.com", and active status true

  Scenario: Fail to activate a SIM card
    When I submit an activation request with ICCID "8944500102198304826" and email "failure@example.com"
    Then the activation response should indicate failure
    And when I query the record with ID 2
    Then the returned record should have ICCID "8944500102198304826", email "failure@example.com", and active status false
