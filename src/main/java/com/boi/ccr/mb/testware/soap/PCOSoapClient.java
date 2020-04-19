package com.boi.ccr.mb.testware.soap;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;

public class PCOSoapClient {
	
	public String sendRequest (String action, String request) throws Exception {
		
		//CloseableHttpClient httpclient = createHttpClient();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpPost post = createPost(action);
		post.setEntity(new ByteArrayEntity(request.getBytes()));
		CloseableHttpResponse response = httpclient.execute(post);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			response.getEntity().writeTo(out);
		}finally {
			response.close();
		}
		return out.toString();
	}
	
	public HttpPost createPost (String soapAction){
		HttpPost post = new HttpPost("https://footballpool.dataaccess.eu/info.wso");
		post.setHeader("Content-Type","text/xml;charet=UTF-8");
		post.setHeader("SOAPAction", soapAction);
		return post;
	}
	
	private static CloseableHttpClient createHttpClient() throws Exception{
		//HttpHost proxy = new HttpHost(InetAddress.getByName("webproxy.boigroup.net"));
		
		HttpHost proxy = new HttpHost(InetAddress.getByName("https://footballpool.dataaccess.eu/info.wso"));
		
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, (chain, authType) -> true);
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),NoopHostnameVerifier.INSTANCE);
		
		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLSocketFactory(sslsf).build();
		return httpclient;
	}

}
