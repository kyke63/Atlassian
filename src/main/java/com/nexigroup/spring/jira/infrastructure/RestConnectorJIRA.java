package com.nexigroup.spring.jira.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

import java.util.Set;

/**
 * This class keeps the state of the connection for the examples. This class is
 * a thus sharing state singleton. All examples get the instance in their
 * default constructors - (cookies, server url).
 *
 * Some simple methods are implemented to get commonly used paths.
 *
 */
public class RestConnectorJIRA {

	protected Map<String, String> cookies;

	protected String serverUrl;
	protected String user;
	protected String password;

	public RestConnectorJIRA init(String serverUrl, String user, String password) {

		this.serverUrl = serverUrl;
		this.user = user;
		this.password = password;

		return this;
	}

	private RestConnectorJIRA() {
	}

	private static RestConnectorJIRA instance = new RestConnectorJIRA();

	public static RestConnectorJIRA getInstance() {
		return instance;
	}

	/**
	 * @param path on the server to use
	 * @return a url on the server for the path parameter
	 */
	public String buildUrl(String path) {

		return String.format("%1$s/%2$s", serverUrl, path);
	}

	public Response httpPut(String url, byte[] data, Map<String, String> headers) throws Exception {

		return doHttp("PUT", url, null, data, headers);
	}

	public Response httpPost(String url, byte[] data, Map<String, String> headers) throws Exception {

		return doHttp("POST", url, null, data, headers);
	}

	public Response httpDelete(String url, Map<String, String> headers) throws Exception {

		return doHttp("DELETE", url, null, null, headers);
	}

	public Response httpGet(String url, String queryString, Map<String, String> headers) throws Exception {

		return doHttp("GET", url, queryString, null, headers);
	}

	/**
	 * @param type        http operation: get post put delete
	 * @param url         to work on
	 * @param queryString
	 * @param data        to write, if a writable operation
	 * @param headers     to use in the request
	 * @param cookies     to use in the request and update from the response
	 * @return http response
	 * @throws Exception
	 */
	private Response doHttp(String type, String url, String queryString, byte[] data, Map<String, String> headers)
			throws Exception {

		if ((queryString != null) && !queryString.isEmpty()) {

			url += "?" + queryString;
		}

		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        byte[] credBytes = (user + ":" + password).getBytes();
        String credEncodedString = "Basic " + Base64.encodeBase64String(credBytes);
        headers.put("Authorization", credEncodedString);
		con.setRequestMethod(type);

		prepareHttpRequest(con, headers, data);
		con.connect();
		Response ret = retrieveHtmlResponse(con);

		return ret;
	}

	/**
	 * @param con          connection to set the headers and bytes in
	 * @param headers      to use in the request, such as content-type
	 * @param bytes        the actual data to post in the connection.
	 * @param cookieString the cookies data from clientside, such as lwsso,
	 *                     SiteSession, jsession etc.
	 * @throws java.io.IOException
	 */
	private void prepareHttpRequest(HttpURLConnection con, Map<String, String> headers, byte[] bytes)
			throws IOException {

		String contentType = null;

		// send data from headers
		if (headers != null) {

			// Skip the content-type header - should only be sent
			// if you actually have any content to send. see below.
			contentType = headers.remove("Content-Type");

			Iterator<Entry<String, String>> headersIterator = headers.entrySet().iterator();
			while (headersIterator.hasNext()) {
				Entry<String, String> header = headersIterator.next();
				con.setRequestProperty(header.getKey(), header.getValue());
			}
		}

		// If there's data to attach to the request, it's handled here.
		// Note that if data exists, we take into account previously removed
		// content-type.
		if ((bytes != null) && (bytes.length > 0)) {

			con.setDoOutput(true);

			// warning: if you add content-type header then you MUST send
			// information or receive error.
			// so only do so if you're writing information...
			if (contentType != null) {
				con.setRequestProperty("Content-Type", contentType);
			}

			OutputStream out = con.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		}
	}

	/**
	 * @param con that is already connected to its url with an http request, and
	 *            that should contain a response for us to retrieve
	 * @return a response from the server to the previously submitted http request
	 * @throws Exception
	 */
	private Response retrieveHtmlResponse(HttpURLConnection con) throws Exception {

		Response ret = new Response();

		ret.setStatusCode(con.getResponseCode());
		ret.setResponseHeaders(con.getHeaderFields());

		InputStream inputStream;
		// select the source of the input bytes, first try 'regular' input
		try {
			inputStream = con.getInputStream();
		}

		/*
		 * If the connection to the server somehow failed, for example 404 or 500,
		 * con.getInputStream() will throw an exception, which we'll keep. We'll also
		 * store the body of the exception page, in the response data.
		 */
		catch (Exception e) {

			inputStream = con.getErrorStream();
			ret.setFailure(e);
		}

		// This actually takes the data from the previously set stream
		// (error or input) and stores it in a byte[] inside the response
		ByteArrayOutputStream container = new ByteArrayOutputStream();

		byte[] buf = new byte[1024];
		int read;
		while ((read = inputStream.read(buf, 0, 1024)) > 0) {
			container.write(buf, 0, read);
		}

		ret.setResponseData(container.toByteArray());

		return ret;
	}
}
