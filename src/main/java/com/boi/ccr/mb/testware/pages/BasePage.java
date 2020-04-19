package com.boi.ccr.mb.testware.pages;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.String.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.boi.ccr.mb.testware.config.ApplicationProperties;
import com.boi.ccr.mb.testware.reporting.AllureReporter;
import com.boi.ccr.mb.testware.reporting.LogUtil;
import com.boi.ccr.mb.testware.util.TestUtil;

public class BasePage {

	// REST SERVICE

	public int RESPONSE_STATUS_CODE_200 = 200;

	public int RESPONSE_STATUS_CODE_500 = 500;

	public int RESPONSE_STATUS_CODE_400 = 400;

	public int RESPONSE_STATUS_CODE_401 = 401;

	public int RESPONSE_STATUS_CODE_201 = 201;

	private static final Logger LOG = LoggerFactory.getLogger(BasePage.class);

	protected WebDriver driver;
	protected WebDriverWait wait;

	protected ApplicationProperties props = ApplicationProperties.instance;

	protected static final boolean highlightElementFlag = false;

	private static final String LINK = "//a[contains(text(),'%s']";

	private static final String BUTTON = "//input[@value='%s']";

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	protected boolean isDisplayed(String xpath) {

		try {
			WebElement e1 = findVisible(xpath);
			return e1 != null;

		} catch (Exception e) {
			return false;
		}
	}

	protected void input(By xpath, String text) {
		WebElement inputField = findVisible(xpath);
		inputField.clear();
		inputField.sendKeys(text);
		attachScreenshot();
	}

	protected void input(String xpath, String text) {
		WebElement inputField = findVisible(xpath);
		inputField.clear();
		inputField.sendKeys(text);
		attachScreenshot();
	}

	public WebElement getWebElement(String Locator) {

		WebElement element = null;

		try

		{

			if (Locator.startsWith("/") || Locator.startsWith("("))

			{

				element = driver.findElement(By.xpath(Locator)); // xpath

			}

			else if (Locator.startsWith("css="))

			{

				Locator = Locator.substring(4);

				element = driver.findElement(By.cssSelector(Locator));

			}

			else if (Locator.startsWith("id="))

			{

				Locator = Locator.substring(3);

				element = driver.findElement(By.id(Locator));

			}

			else if (Locator.startsWith("name="))

			{

				Locator = Locator.substring(5);

				element = driver.findElement(By.name(Locator));

			}

			else if (Locator.startsWith("linkText="))

			{

				Locator = Locator.substring(9);

				element = driver.findElement(By.linkText(Locator));

			}

			else if (Locator.startsWith("className="))

			{

				Locator = Locator.substring(10);

				element = driver.findElement(By.className(Locator));

			}

			else if (Locator.startsWith("tagName="))

			{

				Locator = Locator.substring(10);

				element = driver.findElement(By.tagName(Locator));

			}

			else {

				LogUtil.log("ERROR: Invalid Locator : " + Locator);

				Assert.fail("Invalid Locator : " + Locator);

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: getWebElement: Exception occured in identifying the object- GetWebElement : '" + Locator
					+ "'");

			Assert.fail("Unable to locate the Web Element");

		}

		// Set highlightElementFlag=false if web element need not be highlighted

		if (highlightElementFlag)

		{

			highlightElement(element);

		}

		return element;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Get the list of web elements based on the locator
	 * 
	 * @param - Locator
	 * 
	 */

	protected List<WebElement> getWebElements(String Locator) {

		List<WebElement> elements = null;

		try

		{

			if (Locator.startsWith("/") || Locator.startsWith("("))

			{

				elements = driver.findElements(By.xpath(Locator));

			}

			else if (Locator.startsWith("css="))

			{

				Locator = Locator.substring(4);

				elements = driver.findElements(By.cssSelector(Locator));

			}

			else if (Locator.startsWith("id="))

			{

				Locator = Locator.substring(3);

				elements = driver.findElements(By.id(Locator));

			}

			else if (Locator.startsWith("name="))

			{

				Locator = Locator.substring(5);

				elements = driver.findElements(By.name(Locator));

			}

			else if (Locator.startsWith("linkText="))

			{

				Locator = Locator.substring(8);

				elements = driver.findElements(By.linkText(Locator));

			}

			else if (Locator.startsWith("className="))

			{

				Locator = Locator.substring(10);

				elements = driver.findElements(By.className(Locator));

			}

			else if (Locator.startsWith("tagName="))

			{

				Locator = Locator.substring(10);

				elements = driver.findElements(By.tagName(Locator));

			}

			else {

				LogUtil.log("ERROR: Invalid Locator : " + Locator);

				Assert.fail("Invalid Locator : " + Locator);

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: getWebElements: Exception occured in identifying the object- GetWebElements : "
					+ Locator + "'");

			Assert.fail("Unable to locate the Web Elements");

		}

		return elements;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Highlight the web element
	 * 
	 * @param - WebElement
	 * 
	 */

	protected void highlightElement(WebElement element)

	{

		try {

			for (int i = 0; i < 5; i++) {

				JavascriptExecutor executor = (JavascriptExecutor) driver;

				executor.executeScript(
						"arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);

				executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");

			}

		} catch (Exception ex) {

			// No Action

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Enter a value in text field
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print
	 *          incase of error, Text - Text to enter in text field
	 * 
	 */

	protected void inputElement(String Locator, String ObjName, String Text) {

		try {

			WebElement element = getWebElement(Locator);

			if (element.isDisplayed() && element.isEnabled())

			{

				element.clear();

				element.sendKeys(Text);

			} else {

				attachScreenshot();

				LogUtil.log("ERROR: " + ObjName + " input element not displayed/enabled");

				Assert.fail(ObjName + " input element not displayed/enabled");

			}

		} catch (Exception ex) {

			attachScreenshot();

			LogUtil.log("ERROR: inputElement: Exception occurred in entering a value in the text field '" + ObjName
					+ "'. Exception Message - " + ex);

			Assert.fail("Unable to enter the value in '" + ObjName + "' text field ");

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Click on a WebElement
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error
	 * 
	 */

	public void clickElement(String Locator, String ObjName)

	{

		try

		{

			WebElement element = getWebElement(Locator);

			if (element.isDisplayed())

			{

				JavascriptExecutor executor = (JavascriptExecutor) driver;

				executor.executeScript("arguments[0].click();", element);

				Thread.sleep(1000);

			}

			else {

				attachScreenshot();

				LogUtil.log("ERROR: " + ObjName + " is not displayed to click");

				Assert.fail(ObjName + " is not displayed to click");

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: clickElement: Exception occurred in clicking the element '" + ObjName
					+ "'. Exception Message - " + ex);

			Assert.fail("Unable to click the element '" + ObjName + "'");

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Find the status of the WebElement
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error,
	 * 
	 *          StatusCheck - Can be "PRESENT", "DISPLAYED", "ENABLED", "SELECTED"
	 * 
	 */

	public boolean find_ElementStatus(String Locator, String ObjName, String StatusCheck)

	{

		boolean status = false;

		try

		{

			switch (StatusCheck.toUpperCase()) {

			case "PRESENT":

				List<WebElement> elements = getWebElements(Locator);

				if (elements.size() > 0)

					status = true;

				break;

			case "DISPLAYED":

				if (getWebElement(Locator).isDisplayed())

					status = true;

				break;

			case "ENABLED":

				if (getWebElement(Locator).isEnabled())

					status = true;

				break;

			case "SELECTED":

				if (getWebElement(Locator).isSelected())

					status = true;

				break;

			default:

				LogUtil.log("ERROR: Provide Valid Status to Verify the Element");

				Assert.fail("Provide Valid Status to Verify the Element");

				break;

			}

		} catch (Exception ex) {

			attachScreenshot();

			LogUtil.log("ERROR: find_ElementStatus: Exception occurred in verifying the '" + StatusCheck
					+ "' status of the element '" + ObjName + "'. Exception Message - " + ex);

			Assert.fail("Unable to verify the '" + StatusCheck + "' status of the element '" + ObjName + "'");

		}

		return status;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Verify whether all the values are displayed in dropdown list
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error,
	 * 
	 *          DropdownValues - Mention the list of all expected dropdown values
	 *          with comma seperator (without space in between commas). For Example:
	 *          Borrower,Co-Borrower
	 * 
	 */

	protected void verifyDropdownValues(String Locator, String ObjName, String DropdownValues)

	{

		try

		{

			WebElement element = getWebElement(Locator);

			boolean found;

			Select select = new Select(element);

			List<WebElement> options = select.getOptions();

			List<String> actual_DropValues = new ArrayList<String>();

			List<String> actual_DropValues_Temp = new ArrayList<String>();

			List<String> expected_DropValues = new ArrayList<String>(
					Arrays.asList(DropdownValues.toUpperCase().split(",")));

			for (WebElement droplistElement : options)

			{

				if (!(droplistElement.getText().trim().isEmpty()))

				{

					actual_DropValues.add(droplistElement.getText().trim().toUpperCase());

				}

			}

			actual_DropValues_Temp.addAll(actual_DropValues);

			actual_DropValues.removeAll(expected_DropValues);

			expected_DropValues.removeAll(actual_DropValues_Temp);

			if (actual_DropValues.isEmpty() && expected_DropValues.isEmpty())

			{

				LogUtil.log(ObjName + " - Dropdown Values Matched");

			}

			else

			{

				attachScreenshot();

				LogUtil.log("ERROR: " + ObjName + " - Dropdown Values Not Matched. Expected Values: "
						+ expected_DropValues + ". Actual Values: " + actual_DropValues);

				Assert.fail(ObjName + " - Dropdown Values Not Matched. Expected Values: " + expected_DropValues
						+ ". Actual Values: " + actual_DropValues);

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: verifyDropdownValues: Exception occured in verifying values in the dropdown list "
					+ ObjName + "'. Exception Message - " + ex);

			Assert.fail("Unable to verify the list of values in the dropdown list " + ObjName + "'");

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Get the default value displayed in the dropdown list
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error
	 * 
	 */

	protected String getSelectedValue_Dropdown(String Locator, String ObjName)

	{

		String selectedValue = null;

		try

		{

			WebElement element = getWebElement(Locator);

			Select select = new Select(element);

			selectedValue = select.getFirstSelectedOption().getText();

		} catch (Exception ex) {

			attachScreenshot();

			LogUtil.log(
					"ERROR: getSelectedValue_Dropdown: Exception occured in verifying the default value from dropdown list - '"
							+ ObjName + "'. Exception Message - " + ex);

			Assert.fail("Unable to verify the default value from dropdown list - '" + ObjName + "'");

		}

		return selectedValue;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Select a value from dorpdown list
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error,
	 * 
	 *          DropdownValue - Value to be selected from dropdown list
	 * 
	 */

	protected void select_DropdownValue(String Locator, String ObjName, String DropdownValue)

	{

		try

		{

			boolean found = false;

			Select select = new Select(getWebElement(Locator));

			List<WebElement> elements = select.getOptions();

			for (WebElement selectElement : elements)

			{

				if (selectElement.getText().equals(DropdownValue))

				{

					select.selectByVisibleText(DropdownValue);

					found = true;

					break;

				}

			}

			if (!found)

			{

				attachScreenshot();

				LogUtil.log("ERROR: The value '" + DropdownValue + "' is not found from the dropdown list '" + ObjName
						+ "'. Test data should be case-sensitive, so verify the test data.");

				Assert.fail("The value '" + DropdownValue + "' is not found from the dropdown list '" + ObjName
						+ "'. Test data should be case-sensitive, so verify the test data.");

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: select_DropdownValue: Exception occured in selecting the value '" + DropdownValue
					+ "' from '" + ObjName
					+ "' dropdown field. Test data should be case-sensitive, so verify the test data. Exception Message - "
					+ ex);

			Assert.fail("Unable to select the value '" + DropdownValue + "' from '" + ObjName
					+ "' dropdown field. Test data should be case-sensitive, so verify the test data");

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Select a radio button value
	 * 
	 * @param - Locator - WebElement locator, ObjName - Readable name to print in
	 *          case of an error,
	 * 
	 *          Select_Option - Value to be selected from radio button list
	 * 
	 */

	protected void select_RadioButton(String Locator, String ObjName, String Select_Option)

	{

		try

		{

			boolean found = false;

			String value;

			List<WebElement> elements = getWebElements(Locator);

			JavascriptExecutor executor = (JavascriptExecutor) driver;

			WebElement button;

			for (int i = 0; i < elements.size(); i++)

			{

				value = elements.get(i).getText().toUpperCase();

				if (value.equals(Select_Option.toUpperCase()))

				{

					button = driver.findElement(By.xpath(Locator + "[" + (i + 1) + "]/input"));

					highlightElement(button);

					executor.executeScript("arguments[0].click();", button);

					attachScreenshot();

					Thread.sleep(500);

					found = true;

					break;

				}

			}

			if (found = false)

			{

				attachScreenshot();

				LogUtil.log("ERROR: The value '" + Select_Option + "' is not found from the radio button list '"
						+ ObjName + "'");

				Assert.fail(
						"The value '" + Select_Option + "' is not found from the radio button list '" + ObjName + "'");

			}

		}

		catch (Exception ex)

		{

			attachScreenshot();

			LogUtil.log("ERROR: select_RadioButton: Exception occured in selecting a radio button option " + ObjName
					+ " - " + Select_Option + "'. Exception Message - " + ex);

			Assert.fail("Unable to select the radio button option " + ObjName + " - " + Select_Option + "'");

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Scroll down to the element
	 * 
	 * @param - Locator, ObjName
	 * 
	 */

	public void scrollToElement(String Locator, String ObjName) {

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		WebElement element = getWebElement(Locator);

		try {

			executor.executeScript("arguments[0].scrollIntoView(true);", element);

		}

		catch (Exception ex) {

			LogUtil.log("ERROR: scrollToElement: Exception occured while scrolling down to the " + ObjName
					+ " element. Exception Message - " + ex);

			attachScreenshot();

		}

	}

	/**
	 * 
	 * @author –ALAGURAJA
	 * 
	 * @Description: Wait for the element to be visible
	 * 
	 * @param - Locator: In 'By' format; ObjName - Meaningful Name; WaitType -
	 *          Clickable, Visible; TimeoutSeconds - In Seconds
	 * 
	 */

	public WebElement waitUntilElement(By Locator, String ObjName, String WaitType, int TimeoutSeconds)

	{

		setImplicitWait(0);

		WebElement element = null;

		WebDriverWait wait = new WebDriverWait(driver, TimeoutSeconds);

		try

		{

			switch (WaitType.toUpperCase())

			{

			case "VISIBLE":

				element = wait.until(ExpectedConditions.visibilityOfElementLocated(Locator));

				break;

			case "CLICKABLE":

				element = wait.until(ExpectedConditions.elementToBeClickable(Locator));

				break;

			case "PRESENCE":

				element = wait.until(ExpectedConditions.presenceOfElementLocated(Locator));

				break;

			default:

				LogUtil.log("ERROR: Invalid Wait Type - " + WaitType + ". Wait Type can be Clickable, Visible");

				break;

			}

			setImplicitWait(TestUtil.IMPLICIT_WAIT);

		}

		catch (Exception ex) {

			setImplicitWait(TestUtil.IMPLICIT_WAIT);

			LogUtil.log("ERROR: waitUntilElement: Timeout Exception occured while waiting for the object '" + ObjName
					+ "' to be " + WaitType + ". Exception Message - " + ex);

		}

		return element;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: To change timeout value of implicit wait
	 * 
	 * @param - TimeoutSeconds - In Seconds
	 * 
	 */

	public void setImplicitWait(long TimeoutSeconds)

	{

		driver.manage().timeouts().implicitlyWait(TimeoutSeconds, TimeUnit.SECONDS);

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Get the list of header values in a table
	 * 
	 * @param - Locator
	 * 
	 */

	protected Set<String> getTableHeaders(String TableLocator)

	{

		Set<String> setHeader = new HashSet<String>();

		try

		{

			WebElement headerRow = getWebElement(TableLocator + "/tr[1]");

			List<WebElement> allHeaderValues = headerRow.findElements(By.tagName("th"));

			for (WebElement hdr : allHeaderValues)

			{

				if (!(hdr.getText().trim().isEmpty()))

				{

					setHeader.add(hdr.getText().trim().toUpperCase());

				}

			}

		} catch (Exception ex) {

			attachScreenshot();

			LogUtil.log(
					"ERROR: getTableHeaders: Exception occured in identifying the table headers. Exception Message : "
							+ ex);

			Assert.fail("Unable to identify the table headers");

		}

		return setHeader;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Get the position of column in a table
	 * 
	 * @param - Locator - WebElement locator, HeaderName - Header column name
	 * 
	 */

	public int getHeader_ColumnPoition(String Locator, String HeaderName)

	{

		int columnPosition = 0;

		boolean found = false;

		try

		{

			WebElement headerRow = getWebElement(Locator + "/tr[1]");

			List<WebElement> allHeaderValues = headerRow.findElements(By.tagName("th"));

			for (WebElement hdr : allHeaderValues)

			{

				columnPosition++;

				if (hdr.getText().toUpperCase().equals(HeaderName.toUpperCase())) {

					found = true;

					break;

				}

			}

			if (found = false) {

				attachScreenshot();

				LogUtil.log("ERROR: Invalid Header Name: " + HeaderName);

				Assert.fail("Invalid Header Name: " + HeaderName);

			}

		} catch (Exception ex) {

			attachScreenshot();

			LogUtil.log(
					"ERROR: getHeader_ColumnPoition: Exception occured in getting the column poistion of the header: "
							+ HeaderName + "'. Exception Message - " + ex);

			Assert.fail("Unable to fetch the column poistion of the header: " + HeaderName + "'");

		}

		return columnPosition;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Get the current system date
	 * 
	 * @param - Format - Example: dd/MM/yyyy
	 * 
	 */

	public static String getCurrentDate(String Format)

	{

		// Create object of SimpleDateFormat class and decide the format

		DateFormat dateFormat = new SimpleDateFormat(Format);

		// get current date time with Date()

		Date date = new Date();

		// Format the date

		String currentDate = dateFormat.format(date);

		return currentDate;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Convert String to Date Format
	 * 
	 * @param - Value - Date to be converted; Format - Example: dd/MM/yyyy
	 * 
	 */

	public Date convertTo_DateFormat(String Value, String Format)

	{

		// String -> Date = SimpleDateFormat.parse(String);

		// Date -> String = SimpleDateFormat.format(date);

		Date date = null;

		DateFormat dateFormat = new SimpleDateFormat(Format);

		// Change String to Date format

		try

		{

			date = dateFormat.parse(Value);

		}

		catch (Exception ex) {

			LogUtil.log(
					"ERROR: convertTo_DateFormat: Exception occured in converting the string to date. Exception Message - "
							+ ex);

			Assert.fail("Unable to convert the string to date");

		}

		return date;

	}

	/**
	 * * @author – ALAGURAJA
	 * 
	 * @Description: Convert Date to Desired Month Format
	 * 
	 * @param - Value - Date to be converted; DateFormat - Example: dd/MM/yyyy;
	 *          MonthFormat - Example: MMM / MMMM
	 * 
	 */

	public String covertMonthFormat(String Value, String DateFormat, String MonthFormat)

	{

		Date date = convertTo_DateFormat(Value, DateFormat);

		SimpleDateFormat sdf = new SimpleDateFormat(MonthFormat);

		return sdf.format(date);

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Add or reduce number of days to the given date
	 * 
	 * @param - Value - Date to which the number should be added/reduced; Format -
	 *          Example: dd/MM/yyyy;
	 * 
	 *          Type - DAY OR MONTH; no_of_days (int) - Number of days to be
	 *          added/reduced
	 * 
	 */

	public String addDays_ToDate(String Value, String Format, String DateType, int no_of_days_months)

	{

		String sdate = null;

		Date getDate, date = null;

		DateFormat dateFormat = new SimpleDateFormat(Format);

		getDate = convertTo_DateFormat(Value, Format);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(getDate);

		if (DateType.toUpperCase().equals("DAY"))

		{

			// To subtract, provide the detail in negative number. i.e., -5

			calendar.add(Calendar.DATE, no_of_days_months);

			date = calendar.getTime();

		}

		else if (DateType.toUpperCase().equals("MONTH"))

		{

			// To subtract, provide the detail in negative number. i.e., -5

			calendar.add(Calendar.MONTH, no_of_days_months);

			date = calendar.getTime();

		}

		// Change Date to String format

		try

		{

			sdate = dateFormat.format(date);

		}

		catch (Exception ex) {

			LogUtil.log(
					"ERROR: addDays: Exception occured in converting the date to string. Exception Message - " + ex);

			Assert.fail("Unable to convert the date to string");

		}

		return sdate;

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Compare whether the 'VerifyDate' is between the date range
	 *               'Date1' and 'Date2'
	 * 
	 * @param - VerifyDate - Date to be verified; minDate - Minimum Date Range;
	 *          maxDate - Maximum Date Range
	 * 
	 */

	public boolean compareDate(String minDate, String maxDate, String VerifyDate)

	{

		Date minimumDate = convertTo_DateFormat(minDate, "dd/MM/yyyy");

		Date maximumDate = convertTo_DateFormat(maxDate, "dd/MM/yyyy");

		Date verifyDate = convertTo_DateFormat(VerifyDate, "dd/MM/yyyy");

		return ((verifyDate.compareTo(minimumDate) >= 0) && (verifyDate.compareTo(maximumDate) <= 0));

	}

	public void select_Date_FromCalendar(String DOB, String Month_Locator, String Year_Locator, String Table_Locator)

	{

		String[] dteValue = DOB.split("/");

		String dteYear = dteValue[2];

		select_DropdownValue(Year_Locator, "CALENDAR_YEAR", dteYear);

		String dteMonth = covertMonthFormat(DOB, "dd/MM/yyyy", "MMM");

		select_DropdownValue(Month_Locator, "CALENDAR_MONTH", dteMonth);

		// To eliminate '0' before the date 10

		int iDate = Integer.parseInt(dteValue[0]);

		String dteDate = Integer.toString(iDate);

		String datePicker;

		boolean found = false;

		List<WebElement> dteTable_Row = getWebElements(Table_Locator);

		List<WebElement> dteTable_CellData = null;

		row: for (int i = 1; i <= dteTable_Row.size(); i++)

		{

			dteTable_CellData = getWebElements(Table_Locator + "[" + i + "]/td");

			for (int j = 1; j <= dteTable_CellData.size(); j++)

			{

				datePicker = getWebElement(Table_Locator + "[" + i + "]/td[" + j + "]").getText().trim();

				if (dteDate.equals(datePicker))

				{

					getWebElement(Table_Locator + "[" + i + "]/td[" + j + "]").click();

					found = true;

					break row;

				}

			}

		}

	}

	/**
	 * 
	 * @author – ALAGURAJA
	 * 
	 * @Description: Generate Random String - Alphabetic or Numeric
	 * 
	 * @param - Length - Number of Characters; UseLetters - Use Letters in String;
	 *          UseNumbers - Use Numbers in String
	 * 
	 */

	public String generateRandomText(int Length, boolean UseLetters, boolean UseNumbers)

	{

		String generatedString = RandomStringUtils.random(Length, UseLetters, UseNumbers);

		return generatedString;

	}

	protected void click(String xpath) {

		findVisible(xpath).click();

		attachScreenshot();

	}

	protected WebElement findVisible(String xpath) {

		return findVisible(By.xpath(xpath));

	}

	protected void sleep(long millis) {

		try {

			Thread.sleep(millis);

		} catch (InterruptedException e) {

			LOG.debug("Sleep interrupted", e);

		}

	}

	protected void click(By Locator) {

		findVisible(Locator).click();

	}

	protected WebElement findVisible(By locator) {

		for (int i = 0, attempts = 3; i < attempts; i++) {

			try {

				List<WebElement> elements = driver.findElements(locator);

				for (WebElement element : elements) {

					if (element.isDisplayed()) {

						return element;

					}

				}

				sleep(1000);

			} catch (Exception e) {

				LOG.debug("Could not find element on page: " + locator + e);

				sleep(1000);

			}

		}

		throw new IllegalStateException("Could not find element: " + locator);

	}

	public void attachScreenshot() {

		byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

		AllureReporter.attachScreenshot(screenshot);

	}

	public void clickButton(String buttonName) {

		click(format(BUTTON, buttonName));

	}

	public void clickLink(String linkName) {

		click(format(LINK, linkName));

	}

	protected List<WebElement> findAllVisible(By locator) {

		List<WebElement> res = new ArrayList<WebElement>();

		for (int i = 0, attempts = 3; i < attempts; i++) {

			try {

				List<WebElement> elements = driver.findElements(locator);

				for (WebElement element : elements) {

					if (element.isDisplayed()) {

						res.add(element);

					}

				}

				if (res.isEmpty()) {

					sleep(1000);

				} else {

					return res;

				}

			} catch (Exception e) {

				LOG.debug("Could not find element on page: " + locator + e);

				sleep(1000);

			}

		}

		throw new IllegalStateException("Could not find element: " + locator);

	}
}
