Narrative:
Scenarios validating that the REST API functinality POST Method is works or not

Scenario: Verify that user is able to get the REST API POST Reponse
Meta:
@id POST_METHOD_001

Given that REST API Endpoint is accessible
When I open the URL and validate the POST Method <FirstArray_lastname> and <Job> status code
Then I got a POST success response and validate the file

Examples:
|FirstArray_lastname|Job|
|ilai|job1|