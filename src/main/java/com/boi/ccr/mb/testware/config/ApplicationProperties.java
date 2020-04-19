package com.boi.ccr.mb.testware.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boi.ccr.mb.testware.reporting.LogUtil;

public class ApplicationProperties {
	
	private static final Logger LOG = LoggerFactory.getLogger(ApplicationProperties.class);
	
	public static ApplicationProperties instance = new ApplicationProperties();
	
	private Properties props;
	
	private ApplicationProperties(){
		props = new Properties();
		try(InputStream in = getClass().getClassLoader().getResourceAsStream("mb.properties")){
			props.load(in);
		}catch (IOException e){
			LOG.error("Error loading application.properties",e);
			LogUtil.log("Error loading application.properties "+e);
			throw new RuntimeException("cannot load mb.properties");
		}
	}
	
public String getStoriesFilter(){
	return getProperty("stories.filter");
}

public String getRuntimeEnvironment(){
	return getProperty("runtime.environment");
}
public String getProperty(String name){
	if(!StringUtils.isBlank(System.getenv(name))){
		return System.getenv(name).trim();
	}
	
	if(!StringUtils.isBlank(System.getProperty(name))){
		return System.getProperty(name).trim();
	}
	return props.getProperty(name).trim();
}

public String getApplicationURL(){
	return getProperty("mb.url");
}
public String getBrowser(){
	System.out.println("Inside Application Properties getBrowser Fun ");
	return getProperty("mb.browser");
}

public String getMBUser(){
	return getProperty("mb.user");
}

public String getMBPass(){
	return getProperty("mb.pass");
}

public String getSOAPUser(){
	return getProperty("soap.user");
}

public String getSOAPPass(){
	return getProperty("soap.pass");
}


}