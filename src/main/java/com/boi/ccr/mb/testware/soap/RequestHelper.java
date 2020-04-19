package com.boi.ccr.mb.testware.soap;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class RequestHelper {
	
	private String template;
	
	public RequestHelper(String path) throws IOException {
		template = readRequest(path);
	}
	
	public void setParameter(String name, String value){
		template = template.replaceAll(name, value);
	}
	
	public String getRequest(){
		return template;
	}
	
	private String readRequest(String request) throws IOException {
		InputStream in = PCOSoapClient.class.getClassLoader().getResourceAsStream(request);
		byte[] buffer = new byte[1024 * 1024 *5];
		IOUtils.read(in,buffer);
		String xml = new String(buffer);
		return xml.trim();				
	}

}
