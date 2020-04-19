Narrative:
Scenarios validating that the REST API functinality Without headers is works or not

Scenario: Verify that user is able to get the REST API Reponse without headers
Meta:
@id Without_Headers_001

Given that REST API Endpoint is accessible
When I open the URL and validate the status code
Then I get a success response and validate <FirstArray_lastname> and <SecondArray_Firstname> JSON file

Examples:
|FirstArray_lastname|SecondArray_Firstname|
|Bluth|Janet|