package com.nexigroup.spring.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nexigroup.spring.jira.infrastructure.Response;
import com.nexigroup.spring.jira.infrastructure.RestConnectorJIRA;

public class UserClient {
	
	private RestConnectorJIRA con;
	
	public UserClient() {
		con = RestConnectorJIRA.getInstance();
	}

	
	public String searchUser(String user, Integer maxResults, Integer startAt) throws Exception {
		String defectsUrl = con.buildUrl("rest/api/latest/user?username="+user );
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		Response response = con.httpGet(defectsUrl,null, map);
		JSONObject result = new JSONObject(new String(response.getResponseData()));
		return new String(response.getResponseData());		
	}

	//GET /rest/api/2/groupuserpicker
	
}
