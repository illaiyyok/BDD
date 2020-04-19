Narrative:
Scenarios validating that the REST API functinality With headers is works or not

Scenario: Verify that user is able to get the REST API Reponse with headers
Meta:
@id With_Headers_001

Given that REST API Endpoint is accessible
When I open the URL with headers and validate the status code
Then I get a success response and validate <FirstArray_lastname> and <SecondArray_Firstname> JSON file with Headers

Examples:
|FirstArray_lastname|SecondArray_Firstname|
|Bluth|Janet|