package com.nexigroup.spring.jira;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.nexigroup.spring.jira.infrastructure.*;

public class ProjectClient {

	private RestConnectorJIRA con;

	public ProjectClient() {
		con = RestConnectorJIRA.getInstance();
	}

	public Map<String, Map<String, String>> getAllProjects() throws Exception {
		String defectsUrl = con.buildUrl("rest/api/2/project");
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		Response response = con.httpGet(defectsUrl, null, map);
		JSONArray projects = new JSONArray(new String(response.getResponseData()));
		Map<String, Map<String, String>> projectsMap = new HashMap<>();
		for (int i = 0; i < projects.length(); i++) {
			JSONObject project = projects.getJSONObject(i);
			System.out.println(project.toString(1));
			Map<String, String> field = new HashMap<>();
			field.put("id", project.getString("id"));
			field.put("key", project.getString("key"));
			field.put("name", project.getString("name"));
			projectsMap.put(project.getString("key"), field);

		}
		return projectsMap;
	}

	public Map<String, String[]> getProject(String key) throws Exception {
		String defectsUrl = con.buildUrl("rest/api/2/project/" + key);
		Map<String, String> map = new HashMap<String, String>();
		map.put("Content-Type", "application/json");
		map.put("Accept", "application/json");
		Response response = con.httpGet(defectsUrl, null, map);
//		System.out.println(new String(response.getResponseData()));

		JSONObject project = new JSONObject(new String(response.getResponseData()));
		System.out.println(project.toString(1));
		Map<String, String[]> projectMap = new HashMap<>();
		projectMap.put("id", new String[] {project.getString("id")});
		projectMap.put("key", new String[] {project.getString("key")});
		projectMap.put("name", new String[] {project.getString("name")});
		JSONArray issueTypes = project.getJSONArray("issueTypes");
		String[] issueTypeArray = new String[issueTypes.length()];
		for (int i = 0 ; i < issueTypes.length(); i++) {
			JSONObject issueType = issueTypes.getJSONObject(i);
			issueTypeArray[i] = issueType.getString("id")+"@"+issueType.getString("name");
			
		}
		projectMap.put("issueTypes", issueTypeArray);
		return projectMap;
	}
}
