package com.nexigroup.spring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.nexigroup.spring.alm.AuthenticateLoginLogout;
import com.nexigroup.spring.alm.Defects;
import com.nexigroup.spring.alm.Entities;
import com.nexigroup.spring.alm.infrastructure.RestConnectorALM;
import com.nexigroup.spring.config.MappingConfig;
import com.nexigroup.spring.config.ProxyConfig;
import com.nexigroup.spring.config.ServerConfig;
import com.nexigroup.spring.jira.IssueClient;
import com.nexigroup.spring.jira.SearchClient;
import com.nexigroup.spring.jira.SearchResult;
import com.nexigroup.spring.jira.UserClient;
import com.nexigroup.spring.jira.infrastructure.RestConnectorJIRA;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@ConfigurationPropertiesScan("com.nexigroup.spring.config")
public class TasTopApplication {

	@Autowired
	private ProxyConfig proxy;
	@Autowired
	private ServerConfig server;
	@Autowired
	private ApplicationContext ctx;
	@Autowired
	private MappingConfig mapping;

	public static void main(String[] args) throws Exception {

		SpringApplication.run(TasTopApplication.class, args);
	}

	@Bean
	@Order(1)
	public CommandLineRunner setupProxy() {
		return (args) -> {
			Properties proxyProp = new Properties(System.getProperties());
			proxyProp.setProperty("http.proxyHost", proxy.getProxyHost());
			proxyProp.setProperty("http.proxyPort", proxy.getProxyPort());
			proxyProp.setProperty("http.proxyUser", proxy.getProxyUser());
			proxyProp.setProperty("http.proxyPassword", proxy.getProxyPassword());
			proxyProp.setProperty("http.nonProxyHosts", proxy.getNonProxyHosts());

			proxyProp.setProperty("https.proxyHost", proxy.getProxyHost());
			proxyProp.setProperty("https.proxyPort", proxy.getProxyPort());
			proxyProp.setProperty("https.proxyUser", proxy.getProxyUser());
			proxyProp.setProperty("https.proxyPassword", proxy.getProxyPassword());
			proxyProp.setProperty("https.nonProxyHosts", proxy.getNonProxyHosts());
			System.setProperties(proxyProp);

			RestConnectorJIRA jira = RestConnectorJIRA.getInstance().init(server.getJiraHost(), server.getJiraUser(),
					server.getJiraPassword());
			IssueClient issueClient = new IssueClient();
			String JIRAProject = server.getJiraProject();
			JSONObject metadata = issueClient.createIssueMeta(JIRAProject);
			JSONArray prjs = metadata.getJSONArray("projects");
			JSONArray types = prjs.getJSONObject(0).getJSONArray("issuetypes");
			JSONObject fields = types.getJSONObject(0).getJSONObject("fields");
			Map<String, Object> fieldsMap = new HashMap<>();
			fields.toMap().forEach((key, value) -> {
				if (!("issuetype".equals(key) || "project".equals(key))) {
					Map<String, String> config = new HashMap<>();
					config.put("type", fields.getJSONObject(key).getJSONObject("schema").getString("type"));
					config.put("fieldId", fields.getJSONObject(key).getString("fieldId"));
					config.put("required", "" + fields.getJSONObject(key).getBoolean("required"));
					if (fields.getJSONObject(key).opt("allowedValues") != null)
						config.put("allowedValues", fields.getJSONObject(key).getJSONArray("allowedValues").toString());
					log.info(key + "->" + fields.getJSONObject(key).toString(3));
					log.info("type ->" + fields.getJSONObject(key).getJSONObject("schema").getString("type"));
					log.info("fieldId ->" + fields.getJSONObject(key).getString("fieldId"));
					log.info("required ->" + fields.getJSONObject(key).getBoolean("required"));
					fieldsMap.put(fields.getJSONObject(key).getString("name"), config);
				}
			});
			mapping.setFields(fieldsMap);
			log.info("\n" + fieldsMap.toString());
			log.info("fine");
			//
//			String JIRAExportDateName = server.getJiraExportDate();
//
//			String queryString = "project = \"" + JIRAProject + "\" And  \"" + JIRAExportDateName
//					+ "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc";
//
//			SearchClient searchClient = new SearchClient();
//
//			Calendar maxExportDate = GregorianCalendar.getInstance();
//			maxExportDate.set(2000, 1, 1, 0, 0, 0);
//			log.info("exportDate set to " + maxExportDate.getTime().toString());
//
//			SearchResult result = null;
//			try {
//				result = searchClient.searchJql(queryString, 1, 0);
//				if (result.getTotal() > 0) {
//
//					result.getIssues().forEach(issue -> {
//						String val = issue.getIssueFields().get("Export Date").getValue().toString();
//						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//						try {
//							System.out.println(val);
//							Date export_Date = formatter
//									.parse(issue.getIssueFields().get("Export Date").getValue().toString());
//							if (maxExportDate.getTime().before(export_Date))
//								maxExportDate.setTime(export_Date);
//						} catch (ParseException e) {
//							log.error("error getting export date", e);
//						}
//					});
//
//				}
//			} catch (Exception e1) {
//				log.error("error getting export date", e1);
//			}
//
//			log.info("exportDate set to " + maxExportDate.getTime().toString());
		};

	}

	/*
	 * @Bean public CommandLineRunner init_old() { return (args) -> {
	 * 
	 * Map<String, String> priorityMap = mapping.getPriorityMap(); Map<String,
	 * String> securityMap = mapping.getSeverityMap();
	 * 
	 * log.info("Init della connesione a ALM"); // RestConnectorALM con =
	 * RestConnectorALM.getInstance().init(new HashMap<String, String>(), //
	 * server.getAlmHost(), server.getAlmDomain(), server.getAlmProject()); //
	 * log.info("Login a ALM"); // AuthenticateLoginLogout authenticate = new
	 * AuthenticateLoginLogout(); // String authenticationPoint =
	 * authenticate.isAuthenticated(); // log.info("We should authenticate at: " +
	 * authenticationPoint); // if (authenticationPoint != null) { //
	 * authenticate.login(authenticationPoint, server.getAlmUser(),
	 * server.getAlmPassword()); // } // authenticate.session();
	 * log.info("Logged in ALM"); RestConnectorJIRA jira =
	 * RestConnectorJIRA.getInstance().init(server.getJiraHost(),
	 * server.getJiraUser(), server.getJiraPassword()); SearchClient searchClient =
	 * new SearchClient(); // String JIRAProject = server.getJiraProject(); ////
	 * String JIRAProject = "XTRM"; String JIRAExportDateName =
	 * server.getJiraExportDate(); // String queryString = "project = \"" +
	 * JIRAProject + "\" And  \"" + JIRAExportDateName +
	 * "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc"; //// String
	 * queryString = "project = \"" + JIRAProject +
	 * "\" AND issuetype = Bug And  \"ID QC\" ~ \"DEF2460\""; SearchResult result =
	 * searchClient.searchJql(queryString, 1, 0); Calendar maxExportDate =
	 * GregorianCalendar.getInstance(); maxExportDate.set(2000, 1, 1, 0, 0, 0);
	 * 
	 * if (result.getTotal() > 0) {
	 * 
	 * result.getIssues().forEach(issue -> { String val =
	 * issue.getIssueFields().get("Export Date").getValue().toString();
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	 * try { System.out.println(val); Date export_Date = formatter
	 * .parse(issue.getIssueFields().get("Export Date").getValue().toString()); if
	 * (maxExportDate.getTime().before(export_Date))
	 * maxExportDate.setTime(export_Date); } catch (ParseException e) {
	 * log.error("error getting export date", e); } });
	 * 
	 * } log.info(maxExportDate.toString()); // Defects defect = new Defects(); //
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //
	 * Entities defects = defect.getDefects( // "{last-modified [ >= '" +
	 * formatter.format(maxExportDate.getTime()) + "' ]}", //
	 * "last-modified[DESC]"); // IssueClient issueClient = new IssueClient(); //
	 * UserClient userClient = new UserClient(); //
	 * defects.getEntities().forEach((key, value) -> { // String qrString =
	 * "project = \"" + JIRAProject + "\" And  \"ID QC\" ~ \"DEF" // +
	 * value.getField("id").getValue(0) + "\" "; //
	 * value.getFields().forEach((keyval, val) -> { // System.out.println(val); //
	 * // }); // try { // SearchResult res = searchClient.searchJql(qrString, 1, 0);
	 * // if (res.getTotal() > 0) { // System.out.println("update " +
	 * res.getTotal()); // } else { // // JSONObject json = new JSONObject(); //
	 * JSONObject fields = new JSONObject(); // fields.put("project", (new
	 * JSONObject()).put("key", JIRAProject)); // fields.put("issuetype", (new
	 * JSONObject()).put("name", "Bug")); // fields.put("summary",
	 * value.getField("name").getValue(0)); // final StringBuffer description = new
	 * StringBuffer(""); // Document doc =
	 * Jsoup.parse(value.getField("description").getValue(0), ""); //
	 * doc.body().childNodes().forEach(node -> { // // if
	 * ("div".equals(node.nodeName())) { // Elements els = ((Element)
	 * node).select("div > font > span"); // for (Element el : els) { //
	 * description.append(el.text()).append("\n"); // } // // } // if
	 * ("table".equals(node.nodeName())) { // Elements els = ((Element)
	 * node).select("table > tbody > tr  "); // for (Element tr : els) { // // for
	 * (Element td : tr.select(" tr > td > div > font > span ")) { //
	 * description.append("|").append(td.text().isEmpty() ? " " : td.text()); //
	 * System.out.println(td); // } // description.append("\n"); // // } //
	 * description.append("\n"); // // } // }); // //
	 * System.out.println(description.toString()); // // // YYYY-MM-DDThh:mm:ss.sTZD
	 * // ////JIRA ALM ////Creato Detected on date //// //Priorità Priority ////
	 * //PT - Severity Severity ////Stato Status //// //Riepilogo Summary ////
	 * //Assegnatario Assigned To //// //Reporter Detected By //// //Descrizione
	 * Description // // fields.put("description", description.toString()); //
	 * fields.put("customfield_11000", "DEF" + value.getField("id").getValue(0)); //
	 * System.out.println(value.getField("owner").getValue(0)); //
	 * System.out.println(value.getField("detected-by").getValue(0)); //
	 * System.out.println(value.getField("status").getValue(0)); // JSONObject
	 * reporter = new JSONObject( //
	 * userClient.searchUser(value.getField("owner").getValue(0), 1, 0)); //
	 * JSONObject assignee = new JSONObject( //
	 * userClient.searchUser(value.getField("detected-by").getValue(0), 1, 0)); //
	 * fields.put("reporter", (new JSONObject()).put("name",
	 * reporter.getString("key"))); // fields.put("priority", // (new
	 * JSONObject()).put("id",
	 * priorityMap.get(value.getField("user-09").getValue(0)))); //
	 * fields.put("customfield_11430", (new JSONObject()).put("value", //
	 * severityMap.get(value.getField("severity").getValue(0)))); ////
	 * fields.put("createdtime", "2017-08-16T14:21:00.000-0500");
	 * //value.getField("creation-time").getValue(0)) ; ////
	 * fields.put("customfield_12603", "2017-08-16T14:21:00.000-0500");
	 * //value.getField("creation-time").getValue(0)) ; // // //
	 * fields.put("assegnee", (new // //
	 * JSONObject()).put("name",assegnee.getString("key"))); ////
	 * fields.put("status", value.getField("status").getValue(0) ); //
	 * json.put("fields", fields); // JSONObject newIssue =
	 * issueClient.createIssue(json); // String issueKey =
	 * newIssue.getString("key"); //// JSONObject updateIssue = new JSONObject();
	 * //// fields = new JSONObject(); //// fields.put("assegnee", (new
	 * JSONObject()).put("name",assegnee.getString("key"))); ////
	 * updateIssue.put("update", new JSONObject().put("fields", fields)); //
	 * issueClient.updateAssignee(issueKey, assignee.getString("key")); //
	 * System.out.println(json.toString(3)); //
	 * System.out.println("create 'ID QC' = " + value.getField("id").getValue(0));
	 * // } // // System.out.println(res); // } catch (Exception e) { //
	 * e.printStackTrace(); // } // }); // }; }
	 */

//	@Bean
//	public CommandLineRunner jiratest() {
//		return (args) -> {
//
//			RestConnectorJIRA con = RestConnectorJIRA.getInstance().init("https://corporate.sia.eu/jira/",
//					"NicotraLeopoldo", "Renato87!?");
//
//			ProjectClient project = new ProjectClient();
//			System.out.println(project.getAllProjects());
//			for (BasicProject p : project.getAllProjects().get()) {
//
//				System.out.println(p);
//
//			}
//		};

//	}
}
