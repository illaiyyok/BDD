package com.boi.ccr.mb.testware.rest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boi.ccr.mb.testware.pages.BasePage;
import com.boi.ccr.mb.testware.reporting.LogUtil;
import com.boi.ccr.mb.testware.steps.Steps;
import com.boi.ccr.mb.testware.util.TestUtil;
import com.boi.ccr.mb.testware.util.UsersData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.fasterxml.jackson.databind.ObjectMapper;

public class RESTHelper extends BasePage {
	
	private static final Logger LOG = LoggerFactory.getLogger(RESTHelper.class);
	
	ObjectMapper mapper;
	UsersData users;

	String serviceURL;
	String apiURL;
	String url;
	RestClient restClient;
	CloseableHttpResponse closeableHttpResponse;

	public RESTHelper(WebDriver driver) {
		super(driver);

	}

	public void openRestURL_withoutHeaders() throws Exception {
		serviceURL = props.getProperty("REST_URL");
		apiURL = props.getProperty("serviceURL");
		url = serviceURL + apiURL;
		
		System.out.println("URL------------>"+url);

		restClient = new RestClient();

		closeableHttpResponse = restClient.get(url);
	}
	
	public void openRestURL_withHeaders() throws Exception {
		serviceURL = props.getProperty("REST_URL");
		apiURL = props.getProperty("serviceURL");
		url = serviceURL + apiURL;
		
		System.out.println("URL------------>"+url);

		restClient = new RestClient();

		HashMap<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Content-Type", "application/json");

       

        //If needed we can pass the below headers as well

        //headerMap.put("username", "test");

        //headerMap.put("password", "password");

        //headerMap.put("Auth Token", "1234");      

        closeableHttpResponse =restClient.get(url,headerMap);
	}
	
	public void openPOSTURL(String FirstArray_lastname, String SecondArray_Firstname) throws Exception {
		serviceURL = props.getProperty("REST_URL");
		apiURL = props.getProperty("serviceURL");
		url = serviceURL + apiURL;
		
		System.out.println("URL------------>"+url);

		restClient = new RestClient();

		HashMap<String, String> headerMap = new HashMap<String, String>();

        headerMap.put("Content-Type", "application/json");

       

        //jackson API - Marshling and unmarshling - means JAVA to JSON and JSON to JAVA

        mapper = new ObjectMapper();

        users = new UsersData(FirstArray_lastname,SecondArray_Firstname); //Expected user objects

       

        //object to JSON file
        
        String dir = System.getProperty("user.dir");

        String exepath = dir + "/src/main/java/com/boi/ccr/mb/testware/util/users.json";

        mapper.writeValue(new File(exepath), users);

       

        // Object to JASON in String

       

        String usersJsonString = mapper.writeValueAsString(users);

        System.out.println(usersJsonString);
        LogUtil.log("POST REQUEST.....>"+usersJsonString);
       

        closeableHttpResponse = restClient.post(url, usersJsonString, headerMap);
	}

	public void validtateStatusCode() throws ParseException, IOException {
		
		// a. Status Code
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

		System.out.println("Status Code-------->" + statusCode);

		//Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200);
		Assert.assertEquals("Status Code is not 200", RESPONSE_STATUS_CODE_200, statusCode);	
	}
	
	public void validtatePOSTStatusCode() throws ParseException, IOException {
		
		// a. Status Code
		int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();

		System.out.println("Status Code-------->" + statusCode);

		//Assert.assertEquals(statusCode, RESPONSE_STATUS_CODE_200);
		Assert.assertEquals("Status Code is not 201", RESPONSE_STATUS_CODE_201, statusCode);	
	}
		
	public String validateJSON(String FirstArray_lastname, String SecondArray_Firstname) throws ParseException, IOException {
					// JAVA String
					String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
					System.out.println("STRING RESPONSE-------->" + responseString);

					// Response JSON String
					JSONObject responseJson = new JSONObject(responseString);
					System.out.println("Response JSON from API-------->" + responseJson);
					LOG.info("Got JSON response:" + responseJson);
					LogUtil.log("Got JSON response:" + responseJson);
					// Validate JSON response SINGLE Value Assertion (Attribute)
					String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page");

					System.out.println("value of Per Page Value is --->" + perPageValue);
					LOG.info("value of Per Page Value is --->:" + perPageValue);
					LogUtil.log("value of Per Page Value is --->:" + perPageValue);
					Assert.assertEquals(Integer.parseInt(perPageValue), 3);

					// Validate JSON response SINGLE Value Assertion (Attribute)

					String totalValue = TestUtil.getValueByJPath(responseJson, "/total");
					System.out.println("value of Total is ---> " + totalValue);
					LOG.info("value of Total is --->:" + totalValue);
					LogUtil.log("value of Total is --->:" + totalValue);

					Assert.assertEquals(Integer.parseInt(totalValue), 12);

					/// Validate JSON response MULTI Value Assertion (Attribute) - JSON ARRAY (Get
					/// the value from JSON Array)

					String lastName = TestUtil.getValueByJPath(responseJson, "/data[0]/last_name");
					System.out.println("value of First Data Array Last Name is --->" + lastName);

					Assert.assertEquals("Expected value not met", FirstArray_lastname, lastName);	

					String firstName = TestUtil.getValueByJPath(responseJson, "/data[1]/first_name");
					System.out.println("value of Second Data Array First Name is --->" + firstName);

					Assert.assertEquals("Expected value not met", SecondArray_Firstname, firstName);
					
					return responseString;
	}
	
	// validate JSON with headers
	
	public String validateJSON_withHeaders(String FirstArray_lastname, String SecondArray_Firstname) throws ParseException, IOException {
		// JAVA String
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		System.out.println("STRING RESPONSE-------->" + responseString);

		// Response JSON String
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("Response JSON from API-------->" + responseJson);
		LOG.info("Got JSON response:" + responseJson);
		LogUtil.log("Got JSON response:" + responseJson);
		// Validate JSON response SINGLE Value Assertion (Attribute)
		String perPageValue = TestUtil.getValueByJPath(responseJson, "/per_page");

		System.out.println("value of Per Page Value is --->" + perPageValue);
		LOG.info("value of Per Page Value is --->:" + perPageValue);
		LogUtil.log("value of Per Page Value is --->:" + perPageValue);
		Assert.assertEquals(Integer.parseInt(perPageValue), 3);

		// Validate JSON response SINGLE Value Assertion (Attribute)

		String totalValue = TestUtil.getValueByJPath(responseJson, "/total");
		System.out.println("value of Total is ---> " + totalValue);
		LOG.info("value of Total is --->:" + totalValue);
		LogUtil.log("value of Total is --->:" + totalValue);

		Assert.assertEquals(Integer.parseInt(totalValue), 12);

		/// Validate JSON response MULTI Value Assertion (Attribute) - JSON ARRAY (Get
		/// the value from JSON Array)

		String lastName = TestUtil.getValueByJPath(responseJson, "/data[0]/last_name");
		System.out.println("value of First Data Array Last Name is --->" + lastName);

		Assert.assertEquals("Expected value not met", FirstArray_lastname, lastName);	

		String firstName = TestUtil.getValueByJPath(responseJson, "/data[1]/first_name");
		System.out.println("value of Second Data Array First Name is --->" + firstName);

		Assert.assertEquals("Expected value not met", SecondArray_Firstname, firstName);
		
		//Validate Header Array

        Header[] headerArray = closeableHttpResponse.getAllHeaders();

        HashMap<String, String> allHeaders = new HashMap<String, String>();

        for(Header header:headerArray){

                        allHeaders.put(header.getName(), header.getValue());
        }
        System.out.println("Response Headers Array from API-------->"+allHeaders);		
		return responseString;
}
	
	
	
	public void postSuccessResponse() throws JsonParseException, JsonMappingException, IOException {
		//JSON String

        String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(),"UTF-8");

       

        JSONObject responseJson = new JSONObject(responseString);

        System.out.println("The response from API is"+responseJson);
        LogUtil.log("POST RESPONSE.....>"+responseJson);

       

        //JASON to JAVA Object - UnMarshalling

        UsersData userResponseObj= mapper.readValue(responseString, UsersData.class); //actual user object

        System.out.println(userResponseObj);

       

        //System.out.println(users.getName().equals(userResponseObj.getName()));                               

        Assert.assertTrue(users.getName().equals(userResponseObj.getName()));
        LogUtil.log("NAME.....>"+userResponseObj.getName());
       

        //System.out.println(users.getJob().equals(userResponseObj.getJob()));

       

        Assert.assertTrue(users.getJob().equals(userResponseObj.getJob()));
        LogUtil.log("JOB.....>"+userResponseObj.getJob());
       

        System.out.println(userResponseObj.getId());
        LogUtil.log("ID.....>"+userResponseObj.getJob());

        System.out.println(userResponseObj.getCreatedAt());
        LogUtil.log("CREATED AT.....>"+userResponseObj.getCreatedAt());
	}

}
