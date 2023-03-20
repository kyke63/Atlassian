package com.nexigroup.spring.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.internal.FieldsAreNonnullByDefault;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nexigroup.spring.alm.Defects;
import com.nexigroup.spring.alm.Entities;
import com.nexigroup.spring.alm.Entity;
import com.nexigroup.spring.config.MappingConfig;
import com.nexigroup.spring.config.ServerConfig;
import com.nexigroup.spring.jira.IssueClient;
import com.nexigroup.spring.jira.SearchClient;
import com.nexigroup.spring.jira.SearchResult;
import com.nexigroup.spring.jira.UserClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PeriodicTask {

	@Autowired
	private MappingConfig mappingConfig;
	@Autowired
	private ServerConfig server;

	@Scheduled(cron = "${cron-string}")
	public void everyFiveSeconds() {
		log.info("Periodic task: " + new Date());
		log.info("--->" + mappingConfig.getSeverityMap());
		log.info("--->" + mappingConfig.getPriorityMap());
		log.info("--->" + mappingConfig.getFields());
		log.info("--->" + mappingConfig.getQc_jira_mapper());
		if (server.getAlmLogin()) {

			String JIRAProject = server.getJiraProject();

			SearchClient searchClient = new SearchClient();

			Calendar maxExportDate = getMaxExportDate();

			Defects defect = new Defects();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			try {
				Entities defects = defect.getDefects(
						"{last-modified [ >= '" + formatter.format(maxExportDate.getTime()) + "' ]}",
						"last-modified[DESC]");
				defects.getEntities().forEach((key, value) -> {
					String qrString = "project = \"" + JIRAProject + "\" And  \"ID QC\" ~ \"DEF"
							+ value.getField("id").getValue(0) + "\" ";
					try {
						SearchResult res = searchClient.searchJql(qrString, 1, 0);
						if (res.getTotal() > 0) {
							updateIssue(value, res);
							System.out.println("update " + res.getTotal());
						} else {
							createIssue(value);
							System.out.println("create ");
						}
					} catch (Exception e) {
						log.error("error on defect " + value.getField("id").getValue(0), e);
					}

				});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void updateIssue(Entity issueALM, SearchResult issuesJira) {
		UserClient userClient = new UserClient();
		IssueClient issueClient = new IssueClient();
		issuesJira.getIssues().forEach(issue -> {
			log.info(issue.toString());
			JSONObject json = new JSONObject();
			JSONObject fields = new JSONObject();
			mappingConfig.getQc_jira_mapper().forEach((key, value) -> {
				if ((issueALM.getField(key) != null) && !issueALM.getField(key).getValue(0).isEmpty()) {
					String qcValue = issueALM.getField(key).getValue(0);
					Map<String, String> fieldConfig = (Map<String, String>) mappingConfig.getFields().get(value);
					if ("description".equals(key)) {
						qcValue = convertHtml(qcValue).toString();
					}
					Object jiraValue = convertToJira(issueALM,  key, value , userClient);
					if ("user".equals(fieldConfig.get("type"))) {
						log.info(((JSONObject)jiraValue).toString(4));
						jiraValue = ((JSONObject)jiraValue).getString("name");
					}
					if (!qcValue.equals(jiraValue)) {
						fields.put(fieldConfig.get("fieldId"), jiraValue);
					}
				}
			});
			if (!fields.isEmpty()) {
				Map<String, String> fieldConfig = (Map<String, String>) mappingConfig.getFields().get("export date");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				fields.put(fieldConfig.get("fieldId"),formatter.format((new Date(System.currentTimeMillis()))));
				json.put("fields", fields);
			}
		});
		
	}

	private void createIssue(Entity issue) {
		IssueClient issueClient = new IssueClient();
		UserClient userClient = new UserClient();
		JSONObject json = new JSONObject();
		JSONObject fields = new JSONObject();
		fields.put("project", (new JSONObject()).put("key", server.getJiraProject()));
		fields.put("issuetype", (new JSONObject()).put("name", "Bug"));
		fields.put("customfield_11000", "DEF" + issue.getField("id").getValue(0));
		mappingConfig.getQc_jira_mapper().forEach((key, value) -> {
			if (!issue.getField(key).getValue(0).isEmpty() && !"assignee".equals(key)) {
				log.info("QC field name    -> " + key);
				Map<String, String> fieldConfig = (Map<String, String>) mappingConfig.getFields().get(value);
				Object jiraValue = convertToJira(issue,  key, value , userClient);
				fields.put(fieldConfig.get("fieldId"), jiraValue);

			}
		});
		json.put("fields", fields);
		Map<String, String> fieldConfig = (Map<String, String>) mappingConfig.getFields().get("export date");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		fields.put(fieldConfig.get("fieldId"),formatter.format((new Date(System.currentTimeMillis()))));
		try {
			JSONObject newIssue = issueClient.createIssue(json);
			String issueKey = newIssue.getString("key");
			mappingConfig.getQc_jira_mapper().forEach((key, value) -> {
				if (!issue.getField(key).getValue(0).isEmpty() && "assignee".equals(key)) {
					Object jiraValue = convertToJira(issue,  key, value , userClient);
					try {
						issueClient.updateAssignee(issueKey, ((JSONObject)jiraValue).getString("key"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						log.error("error updating assignee " + issueKey, e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error("error updating assignee " + issueKey, e);
					}
				}
			});
		} catch (Exception e) {
			log.error("error creating issue " + issue.getField("id").getValue(0), e);
		}

	}

	private Object convertToJira(Entity issue, String key, String value, UserClient userClient) {
		String qcValue = issue.getField(key).getValue(0);
		if ("description".equals(key)) {
			qcValue = convertHtml(qcValue).toString();
		}
		if (("severity").equals(key)) {
			qcValue = mappingConfig.getSeverityMap().get(qcValue);
		}
		if (("user-09").equals(key)) {
			qcValue = mappingConfig.getPriorityMap().get(qcValue);
		}
		log.info("QC field value   -> " + qcValue);
		log.info("JR field name    -> " + value);
		Map<String, String> fieldConfig = (Map<String, String>) mappingConfig.getFields().get(value);
		Object jiraValue = qcValue;
		log.info("JR field config ->" + fieldConfig);
		if ("option".equals(fieldConfig.get("type")) || "priority".equals(fieldConfig.get("type"))) {
			JSONArray allowed = new JSONArray((new JSONObject(fieldConfig)).getString("allowedValues"));
			for (int i = 0; i < allowed.length(); i++) {
				JSONObject allowedValue = allowed.getJSONObject(i);

				if (("priority".equals(fieldConfig.get("type")) && qcValue.equals(allowedValue.getString("name")))
						|| ("option".equals(fieldConfig.get("type"))
								&& qcValue.equals(allowedValue.getString("value")))) {
					jiraValue = ((new JSONObject()).put("value", qcValue));
				}
			}
			;
			log.info("JR field allowed ->" + jiraValue);
		} else if ("user".equals(fieldConfig.get("type"))) {
			JSONObject user = null;
			try {
				user = new JSONObject(userClient.searchUser(qcValue, 1, 0));
				jiraValue = ((new JSONObject()).put("name", user.getString("key")));
			} catch (JSONException e) {
				log.error("error getting user " + qcValue, e);
			} catch (Exception e) {
				log.error("error getting user " + qcValue, e);
			}
			log.info(jiraValue.toString());
		}

		log.info("JR field fieldId    -> " + fieldConfig.get("fieldId"));
		log.info("JR field mapping ->" + mappingConfig.getFields().get(value));
		log.info("JR field value ->" + jiraValue);
		return  jiraValue;

	}

	private StringBuffer convertHtml(String value) {
		final StringBuffer result = new StringBuffer(100);
		Document doc = Jsoup.parse(value, "");
		doc.body().childNodes().forEach(node -> {

			if ("div".equals(node.nodeName())) {
				Elements els = ((Element) node).select("div > font > span");
				for (Element el : els) {
					result.append(el.text()).append("\n");
				}

			}
			if ("table".equals(node.nodeName())) {
				Elements els = ((Element) node).select("table > tbody > tr  ");
				for (Element tr : els) {

					for (Element td : tr.select(" tr > td > div > font > span ")) {
						result.append("|").append(td.text().isEmpty() ? " " : td.text());

					}
					result.append("\n");

				}
				result.append("\n");

			}
		});

		return result;

	}

	private Calendar getMaxExportDate() {
		String JIRAExportDateName = server.getJiraExportDate();
		String JIRAProject = server.getJiraProject();

		String queryString = "project = \"" + JIRAProject + "\" And  \"" + JIRAExportDateName
				+ "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc";

		SearchClient searchClient = new SearchClient();

		Calendar maxExportDate = GregorianCalendar.getInstance();
		maxExportDate.set(2000, 1, 1, 0, 0, 0);
		log.info("exportDate set to " + maxExportDate.getTime().toString());

		SearchResult result = null;
		try {
			result = searchClient.searchJql(queryString, 1, 0);
			if (result.getTotal() > 0) {

				result.getIssues().forEach(issue -> {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					try {
						Date export_Date = formatter
								.parse(issue.getIssueFields().get("export date").getValue().toString());
						if (maxExportDate.getTime().before(export_Date))
							maxExportDate.setTime(export_Date);
					} catch (ParseException e) {
						log.error("error parsing date getting export date", e);
					}
				});

			}
		} catch (Exception e1) {
			log.error("error connecting to JIRA getting export date", e1);
		}

		log.info("exportDate set to " + maxExportDate.getTime().toString());
		return maxExportDate;
	}

}