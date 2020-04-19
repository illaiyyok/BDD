Narrative:
Scenarios validating that the login functionality displays proper error message with invalid data

Scenario: Verify that user is able to login with invalid Username and Valid Password
Meta:
@id Login_Page_001

Given that Gmail Application is up and running
When I open the login page
Then I enter <username> and <password> for <Scenario>
And I should get <errormessage>

Examples:
|username|password|errormessage|Scenario|
|alagu.rar@gmail.com|alagu|The user name or password provided is incorrect.|Invalid Username and Password|

Scenario: Verify that user is able to login with valid Username and invalid Password
Meta:
@id Login_Page_002

Given that Gmail Application is up and running
When I open the login page
Then I enter <username> and <password> for <Scenario>
And I should get <errormessage>

Examples:
|username|password|errormessage|Scenario|
|alagu.rar@gmail.com|alagu|The user name or password provided is incorrect.|Valid Username and Invalid Password|