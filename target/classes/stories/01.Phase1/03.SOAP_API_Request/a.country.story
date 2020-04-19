Narrative:
Scenarios validating that the API functinality is works or not

Scenario: Verify that user is able to get the Country Information
Meta:
@id Country_Info_001

Given that MB SOAP Endpoint is accessible Scenario
When I send SOAP All Cards <Country> Information request
Then I receive all Cards Information success response

Examples:
|Country|
|England|