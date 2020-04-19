package com.boi.ccr.mb.testware.util;

import org.json.JSONArray;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import com.boi.ccr.mb.testware.pages.BasePage;

public class TestUtil extends BasePage{
	
	public static long IMPLICIT_WAIT = 10;

	public TestUtil(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public static String getValueByJPath(JSONObject responsejson, String jpath) {

		Object obj = responsejson;

		for (String s : jpath.split("/"))

			if (!s.isEmpty())

				if (!(s.contains("[") || s.contains("]")))

					obj = ((JSONObject) obj).get(s);

				else if (s.contains("[") || s.contains("]"))

					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0]))
							.get(Integer.parseInt(s.split("\\[")[1].replace("]", "")));

		return obj.toString();

	}

}
