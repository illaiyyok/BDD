package com.boi.ccr.mb.testware.steps;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.boi.ccr.mb.testware.config.ApplicationProperties;
import com.boi.ccr.mb.testware.pages.BasePage;
import com.boi.ccr.mb.testware.pages.LoginPage;
import com.boi.ccr.mb.testware.reporting.LogUtil;
import com.boi.ccr.mb.testware.rest.RESTHelper;
import com.boi.ccr.mb.testware.rest.RestClient;
import com.boi.ccr.mb.testware.soap.PCOSoapClient;
import com.boi.ccr.mb.testware.soap.RequestHelper;
import com.boi.ccr.mb.testware.util.TestUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Steps {

	private static final String SELENIUM_GRID = "http://10.64.10.231:4444/wd/hub";

	private static final Logger LOG = LoggerFactory.getLogger(Steps.class);
	private ApplicationProperties props = ApplicationProperties.instance;

	private WebDriver driver;

	private BasePage basePage;

	private PCOSoapClient client;

	private RestClient restClient;

	public Steps() {
		driver = createDriver();
		basePage = new BasePage(driver);
		client = new PCOSoapClient();
		restClient = new RestClient();
	}

	@AfterStories
	public void tearDown() {
		driver.quit();

	}

	@BeforeScenario(uponType = ScenarioType.EXAMPLE)
	public void beforeScenarion() {
		// ......
	}

	@AfterScenario
	public void afterScenario() throws InterruptedException {

	}

	public Object[] getSteps() {
		return new Object[] { this, new MB_LoginPage(), new PCOSteps(), new RESTSteps()};
	}

	public class MB_LoginPage {
		private LoginPage loginPage = new LoginPage(driver);

		// Negative TestCase
		@Given("that Gmail Application is up and running")
		public void giveThatMBApplicationIsRunning() {
			System.out.println("Inside first story.........................");

		}

		@When("I open the login page")
		public void whenIOpenTheLoginPage() {
			loginPage.open();
		}

		@Then("I enter $username and $password for $Scenario")
		public void whenIEnterInvalidUsernameAndPassword(String username, String password, String Scenarion) {
			loginPage.login(username, password);
		}

		@Then("I should get $errormessage")
		public void CustomisedErrorMessage(String errormessage) {
			loginPage.validateErrorMessage(errormessage);
		}
	}

	public class PCOSteps {

		protected String response;

		@Given("that MB SOAP Endpoint is accessible Scenario")
		public void givenThatSOAPEndpointIsAccessible() {

		}

		@When("I send SOAP All Cards $Country Information request")
		public void whenISOAPAllCardsInformationRequest(String Country) throws Exception {
			String soapAction = "https://footballpool.dataaccess.eu";
			RequestHelper helper = new RequestHelper("soap/countryRequest.xml");
			helper.setParameter("soap.countryName", Country);
			String request = helper.getRequest();
			LOG.info("Sending Request:" + request);
			LogUtil.log("Sending Request:" + request);
			response = client.sendRequest(soapAction, request);

		}

		@Then("I receive all Cards Information success response")
		public void thenIWillGetSuccessResponse() {
			LOG.info("Got response:" + response);
			LogUtil.log("Got response:" + response);
			if (response.contains("<m:sName>England</m:sName>")) {
				// LogUtil.
				LOG.info("GOT RESPONSE --->: PASS");
				LogUtil.log("GOT RESPONSE --->: PASS");
			} else {
				Assert.assertThat("THE STATUS is failed", false, is(1));
			}
			assertTrue("The response is not ok" + response, response.contains("<m:sName>England</m:sName>"));
		}

	}

	public class RESTSteps {

		String serviceURL;
		String apiURL;
		String url;
		RestClient restClient;
		CloseableHttpResponse closeableHttpResponse;
		RESTHelper helper = new RESTHelper(driver);

		protected String response;

		@Given("that REST API Endpoint is accessible")
		public void givenThatRESTAPIEndpointIsAccessible() {			
			LOG.info("End Ponint is running");
			LogUtil.log("End Ponint is running");
			System.out.println("End Ponint is running");
		}

		@When("I open the URL and validate the status code")
		public void thenIOpenTheURLAndValidateStatusCode() throws Exception {

			helper.openRestURL_withoutHeaders();

			// validate status Code
			helper.validtateStatusCode();		
		}	
		
		@When("I open the URL with headers and validate the status code")
		public void thenIOpenTheURLWithHeadersAndValidateStatusCode() throws Exception {

			helper.openRestURL_withHeaders();

			// validate status Code
			helper.validtateStatusCode();		
		}	
		
		
		@When("I open the URL and validate the POST Method $FirstArray_lastname and $Job status code")
		public void thenIOpenTheURLAndValidatePOSTMethodStatusCode(String FirstArray_lastname, String Job) throws Exception {
			String my_lname = FirstArray_lastname;
			String my_fname = Job;
			helper.openPOSTURL(my_lname, my_fname);

			// validate status Code
			helper.validtatePOSTStatusCode();		
		}
		
		@Then("I get a success response and validate $FirstArray_lastname and $SecondArray_Firstname JSON file")
		public void thenIWillGetSuccessResponse(String FirstArray_lastname, String SecondArray_Firstname) throws ParseException, IOException {
			String my_lname = FirstArray_lastname;
			String my_fname = SecondArray_Firstname;
			String response = helper.validateJSON(my_lname, my_fname);
			LOG.info("Got response:"+response);
			LogUtil.log("Got String response:"+response);
			
		}
		
		@Then("I get a success response and validate $FirstArray_lastname and $SecondArray_Firstname JSON file with Headers")
		public void thenIWillGetSuccessResponseWithHeaders(String FirstArray_lastname, String SecondArray_Firstname) throws ParseException, IOException {
			String my_lname = FirstArray_lastname;
			String my_fname = SecondArray_Firstname;
			String response = helper.validateJSON_withHeaders(my_lname, my_fname);
			LOG.info("Got response:"+response);
			LogUtil.log("Got String response:"+response);
			
		}
		
		
		@Then("I got a POST success response and validate the file")
		public void thenIGotPOSTSuccessResponseAndValidateTheFile() throws ParseException, IOException {
			helper.postSuccessResponse();
			
		}	

	}

	private String browserExePath() {
		String dir = System.getProperty("user.dir");
		System.out.println(dir);
		String exepath = dir + "/src/main/resources/browserExe/";
		return exepath;
	}

	private WebDriver createDriver() {

		WebDriver webDriver = null;
		String browserName = props.getBrowser();
		if (props.getRuntimeEnvironment().toLowerCase().trim().indexOf("local") > -1) {
			if ("chrome".equalsIgnoreCase(browserName)) {

				System.setProperty("webdriver.chrome.driver", browserExePath() + "chromedriver.exe");
				ChromeOptions o = new ChromeOptions();
				o.addArguments("disable-extensions");
				o.addArguments("--start-maximized");
				try {
					webDriver = new ChromeDriver(o);
				}

				catch (Exception e)

				{

					LOG.error("Browser not supported: " + browserName);
					LogUtil.log("Browser not supported: " + browserName);
					throw new IllegalStateException("Browser not supported: " + browserName);

				}

			}

			else if ("FF".equalsIgnoreCase(browserName))

			{

				System.setProperty("webdriver.firefox.marionette", browserExePath() + "geckodriver.exe");

				try

				{

					webDriver = new FirefoxDriver();

				}

				catch (Exception e)

				{

					LOG.error("Browser not supported: " + browserName);
					LogUtil.log("Browser not supported: " + browserName);
					throw new IllegalStateException("Browser not supported: " + browserName);

				}

			}

		}

		else if (props.getRuntimeEnvironment().toLowerCase().trim().indexOf("grid") > -1)

		{

			if ("chrome".equalsIgnoreCase(browserName))

			{

				DesiredCapabilities chromeCaps = DesiredCapabilities.chrome();

				chromeCaps.setCapability("version", "57");

				try

				{

					webDriver = new RemoteWebDriver(new URL(SELENIUM_GRID), chromeCaps);

				}

				catch (MalformedURLException e)

				{

					LOG.error("Error connecting to Chrome SeleniumGrid!", e);
					LogUtil.log("Browser not supported: " + browserName);
					throw new IllegalStateException("Could not connect to Chrome grid: " + SELENIUM_GRID);

				}

			}

			else if ("IE".equalsIgnoreCase(browserName))

			{

				DesiredCapabilities internetExplorerCaps = DesiredCapabilities.internetExplorer();

				internetExplorerCaps.setCapability("version", "11");

				try

				{

					webDriver = new RemoteWebDriver(new URL(SELENIUM_GRID), internetExplorerCaps);

				}

				catch (MalformedURLException e) {

					LOG.error("Error connecting to IE SeleniumGrid!", e);
					LogUtil.log("Error connecting to IE SeleniumGrid!"+e);
					throw new IllegalStateException("Could not connect to IE grid: " + SELENIUM_GRID);

				}

			}

		}

		return webDriver;

	}
}
