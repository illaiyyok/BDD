package com.boi.ccr.mb.testware.pages;

import org.junit.Assert;

import org.junit.Rule;

import org.junit.rules.ErrorCollector;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.ie.InternetExplorerDriver;

import org.openqa.selenium.remote.CapabilityType;

import org.openqa.selenium.remote.DesiredCapabilities;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import java.util.List;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boi.ccr.mb.testware.config.ApplicationProperties;

public class LoginPage extends BasePage {

	private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);

	public static final String USER_ID = "//input[@id='Email']";

	public static final String PASSWORD = "//input[@id='Password']";

	//public static final String LOGON_BTN = "//a[text()='Sign In']";
	
	public static final String LOGON_BTN = "//button[@type='submit']";
	
	//input[@value='%s']

	public static final String ERROR_VALIDATION = "//div[@class='validation-summary-errors']/ul/li";

	public static final String LOGOUT_BTN = "//a[text()='Log Out']";

	public LoginPage(WebDriver driver) {

		super(driver);

	}

//	@Rule
//	public ErrorCollector collector = new ErrorCollector();

	public void open()

	{
		String browserName = props.getBrowser();

		driver.manage().window().maximize();

		driver.manage().deleteAllCookies();

		// driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT,
		// TimeUnit.SECONDS);

		if (browserName.equalsIgnoreCase("IE") || browserName.equalsIgnoreCase("chrome")
				|| (browserName.equalsIgnoreCase("Firefox")))

		{

			driver.get(props.getApplicationURL());

			handleWebsenseBlockPage();

		}

		else

		{

			handleWebsenseBlockPage();

		}

		attachScreenshot();

	}

	private void handleWebsenseBlockPage() {

		for (int i = 0, attempts = 5; i < attempts; i++) {

			try {

				sleep(2000);

				if (driver.getPageSource().contains("ws_blockoption")) {

					wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.name("ws_blockoption")));

					WebElement el = driver.findElement(By.xpath("//input[@type='submit'][@value='Continue']"));

					el.submit();

				} else {

					return;

				}

			} catch (Exception e) {

				LOG.debug("Error with WebSense page!", e);

			}

		}

	}

	public void enterUsernameAndPassword() {

		login(props.getMBUser(), props.getMBPass());

	}

//	public void enterAdminUsernameAndPassword() {
//
//		login(props.getMBAdminUser(), props.getMBAdminPass());
//
//	}

	public String getErrorMessage() {

		WebElement Validation = driver.findElement(By.xpath(ERROR_VALIDATION));

		String message = Validation.getText();

		return message;

	}

	public void validateErrorMessage(String message) {

		List<WebElement> actualErrorMessages = findAllVisible(By.xpath(ERROR_VALIDATION));

		for (WebElement errormessage : actualErrorMessages) {

			if (errormessage.getText().equalsIgnoreCase(message)) {

				return;

			}

		}

		fail("Could not find message: " + message);

	}

	public void click_LoginButton() {

		click(LOGON_BTN);

	}

	public void loginApplication(String user, String pass) throws InterruptedException {

		if (user.isEmpty() || pass.isEmpty())

		{

			user = props.getMBUser();

			pass = props.getMBPass();

		}

		inputElement(USER_ID, "Login Username", user);

		inputElement(PASSWORD, "Login Password", pass);

		attachScreenshot();

		clickElement(LOGON_BTN, "Sign In");

	}

	public void click_directLoginButton() {

		WebElement username = driver.findElement(By.xpath(USER_ID));

		WebElement password = driver.findElement(By.xpath(PASSWORD));

		username.clear();

		password.clear();

		click(LOGON_BTN);

	}

	public void login(String user, String pass) {

		input(USER_ID, user);

		input(PASSWORD, pass);

		click(LOGON_BTN);

		//clickElement(LOGON_BTN, "Sign In");

		attachScreenshot();

	}

	public boolean isFieldDisplayed(String fieldName) {

		String xpath = null;

		if ("Username".equalsIgnoreCase(fieldName)) {

			xpath = USER_ID;

		} else if ("Password".equalsIgnoreCase(fieldName)) {

			xpath = PASSWORD;

		} else {

			throw new IllegalArgumentException("Login Page Field not supported: " + fieldName);

		}

		return isDisplayed(xpath);

	}

	public boolean isButtonDisplayed(String buttonName) {

		String xpath = null;

		if ("Login".equalsIgnoreCase(buttonName)) {

			xpath = LOGON_BTN;

		} else {

			throw new IllegalArgumentException("Login Page Button not supported: " + buttonName);

		}

		return isDisplayed(xpath);

	}

}
