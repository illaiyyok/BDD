package com.boi.ccr.mb.testware.rest;

import java.io.IOException;

import java.net.InetAddress;

import java.security.KeyManagementException;

import java.security.KeyStoreException;

import java.security.NoSuchAlgorithmException;

import java.util.HashMap;

import java.util.Map;

import org.apache.http.Header;

import org.apache.http.HttpHost;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.conn.ssl.NoopHostnameVerifier;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import org.apache.http.ssl.SSLContextBuilder;

import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

public class RestClient {

	// 1. GET Method without HEADERS

	public CloseableHttpResponse get(String url) throws Exception {

		 CloseableHttpClient httpClient = HttpClients.createDefault();

		//CloseableHttpClient httpClient = createHttpClient();

		HttpGet httpGet = new HttpGet(url); // http get request

		CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet); // git the GET url

		return closeableHttpResponse;

	}

	// 2. GET Method  WITH HEADERS

	public CloseableHttpResponse get(String url, HashMap<String, String> headerMap) throws Exception {

		CloseableHttpClient httpClient = HttpClients.createDefault();

		//CloseableHttpClient httpClient = createHttpClient();

		HttpGet httpGet = new HttpGet(url); // http get request

		// Before Clicking the GET Request, we need to insert headers

		for (Map.Entry<String, String> entry : headerMap.entrySet()) {

			httpGet.addHeader(entry.getKey(), entry.getValue());

		}

		CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet); // git the GET url

		return closeableHttpResponse;

	}

	// 3. POST Method

	public CloseableHttpResponse post(String url, String entityString, HashMap<String, String> headerMap)
			throws Exception {

		//CloseableHttpClient httpClient = createHttpClient();
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url); // http POST request

		httpPost.setEntity(new StringEntity(entityString)); // SetEntity is used to define the payload

		// for Headers

		// Before Clicking the GET Request, we need to insert headers

		for (Map.Entry<String, String> entry : headerMap.entrySet()) {

			httpPost.addHeader(entry.getKey(), entry.getValue());

		}

		CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);

		return closeableHttpResponse;

	}

	private static CloseableHttpClient createHttpClient() throws Exception {

		HttpHost proxy = new HttpHost(InetAddress.getByName("webproxy"), 8080);

		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);

		SSLContextBuilder builder = new SSLContextBuilder();

		builder.loadTrustMaterial(null, (chain, authType) -> true);

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),

				NoopHostnameVerifier.INSTANCE);

		CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner)

				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSSLSocketFactory(sslsf).build();

		return httpclient;

	}

}