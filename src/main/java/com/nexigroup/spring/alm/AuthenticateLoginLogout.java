package com.nexigroup.spring.alm;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.nexigroup.spring.alm.infrastructure.Response;
import com.nexigroup.spring.alm.infrastructure.RestConnectorALM;

public class AuthenticateLoginLogout {

	private RestConnectorALM con;
	
	public AuthenticateLoginLogout() {
        con = RestConnectorALM.getInstance();
    }
	public String isAuthenticated() throws Exception {
        String isAuthenticateUrl = con.buildUrl("qcbin/rest/is-authenticated");
        String ret;
        Response response = con.httpGet(isAuthenticateUrl, null, null);
        int responseCode = response.getStatusCode();
        //If already authenticated
        if (responseCode == HttpURLConnection.HTTP_OK) {
            ret = null;
        }
        //If not authenticated - get the address where to authenticate via WWW-Authenticate
        else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            Iterable<String> authenticationHeader =
                    response.getResponseHeaders().get("WWW-Authenticate");
            String newUrl = authenticationHeader.iterator().next().split("=")[1];
            newUrl = newUrl.replace("\"", "");
            newUrl += "/authenticate";
            ret = newUrl;
        }
        //Not OK and not unauthorized - means some kind of error, like 404, or 500
        else {
            throw response.getFailure();
        }
        return ret;
    }
	
	public boolean login(String loginUrl, String username, String password) throws Exception {
        //Create a string that looks like "Basic ((username:password)lt;as bytesgt;)lt;64encodedgt;"
        byte[] credBytes = (username + ":" + password).getBytes();
        String credEncodedString = "Basic " + Base64.encodeBase64String(credBytes);
        Map<String, String> map = new HashMap<String, String>();
        map.put("Authorization", credEncodedString);
        Response response = con.httpGet(loginUrl, null, map);
        boolean ret = response.getStatusCode() == HttpURLConnection.HTTP_OK;
        return ret;
    }
	
	public boolean logout() throws Exception {
        //New that the get operation logs us out by setting authentication cookies to: LWSSO_COOKIE_KEY="" using server response header Set-Cookie
        Response response =
                con.httpGet(con.buildUrl("qcbin/authentication-point/logout"), null, null);
        return (response.getStatusCode() == HttpURLConnection.HTTP_OK);
    }
	public boolean session() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
		Response response =
	            con.httpPost(con.buildUrl("qcbin/rest/site-session"),null,map);
        boolean ret = response.getStatusCode() == HttpURLConnection.HTTP_OK;
        return ret;
		
	}
}
