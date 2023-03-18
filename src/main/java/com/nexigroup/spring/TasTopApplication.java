package com.nexigroup.spring;

<<<<<<<HEAD=======

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;>>>>>>>refs/remotes/origin/main
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;<<<<<<<HEAD=======
import org.springframework.context.annotation.Bean;>>>>>>>refs/remotes/origin/main

import com.nexigroup.spring.alm.AuthenticateLoginLogout;
import com.nexigroup.spring.alm.Defects;
import com.nexigroup.spring.alm.Entities;
import com.nexigroup.spring.alm.infrastructure.RestConnectorALM;
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

	public static void main(String[] args) throws Exception {

		SpringApplication.run(TasTopApplication.class, args);
	}

	private static void onStartup() {
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
	}

	@Bean
	public CommandLineRunner init() {
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

			Map<String, String> severityMap = new HashMap<>();
			severityMap.put("1-Critical", "1-Critical");
			severityMap.put("2-Major", "4-Very High");
			severityMap.put("3-Average", "3-High");
			severityMap.put("4-Minor", "2-Medium");
			severityMap.put("5-Enhancement", "1-Low");
			Map<String, String> priorityMap = new HashMap<>();

			priorityMap.put("1-Resolve Immediately", "2"); // "Critical"
			priorityMap.put("2-Give High Attention", "10001"); // "High"); //
			priorityMap.put("3-Normal Queue", "10002"); // "Medium");
			priorityMap.put("4-Low Priority", "10003"); // "Low");

//			Resource qcResouce = ctx.getResource(proxy.getQc_jira_mapper());
//			InputStream is = qcResouce.getInputStream();
//			ByteArrayOutputStream container = new ByteArrayOutputStream();
//
//			byte[] buf = new byte[1024];
//			int read;
//			while ((read = is.read(buf, 0, 1024)) > 0) {
//				container.write(buf, 0, read);
//			}
//
//			JSONArray json = (new JSONObject(new String(container.toByteArray()))).getJSONArray("updated");
//
//			Map<String, Mapping> map = new HashMap<>();
//			for (int i = 0; i < json.length(); i++) {
//				JSONObject jobji = json.getJSONObject(i).getJSONObject("original")
//						.getJSONObject("element").getJSONObject("mappings");
//				for (int k = 0; k < jobji.getJSONArray("field-mappings").length(); k++) {
//					JSONObject jobjk = jobji.getJSONArray("field-mappings").getJSONObject(k);
//					Mapping m = new Mapping();
//					if ( jobjk.getJSONArray("sources").length() == jobjk.getJSONArray("targets").length() && jobjk.getJSONArray("sources").length() == 1) {
//						for (int l = 0; l < jobjk.getJSONArray("sources").length(); l++) {
//							JSONObject jobjl = jobjk.getJSONArray("sources").getJSONObject(l);
//							m.setOrigin(new MappingField(jobjl.getString("id"),jobjl.getString("label")));
//						}
//						for (int l = 0; l < jobjk.getJSONArray("targets").length(); l++) {
//							JSONObject jobjl = jobjk.getJSONArray("targets").getJSONObject(l);
//							m.setTarget(new MappingField(jobjl.getString("id"),jobjl.getString("label")));  
//						}
//					} else {
//						System.out.println("source and target different or more than 1 ");
//						System.out.println(jobjk.getJSONArray("sources").toString(3));
//						System.out.println(jobjk.getJSONArray("targets").toString(3));
//					}
//					map.put(m.getOrigin().getLabel(), m);
//					
//					
//
//
//					if (jobjk.has("value-transformation")) {
//						
//						m.setTransformationId(jobjk.getJSONObject("value-transformation").getString("id"));
//
//						if (jobjk.getJSONObject("value-transformation").has("configuration")) {
//							JSONObject jobjtmpl = jobjk.getJSONObject("value-transformation")
//									.getJSONObject("configuration");
//							if (jobjtmpl.has("mappings")) {
//								for (int l = 0; l < jobjtmpl.getJSONArray("mappings").length(); l++) {
//									JSONObject jobjl = jobjtmpl.getJSONArray("mappings").getJSONObject(l);
//									if (jobjl.has("source") && jobjl.has("target")) {
//										m.getConfiguration().put(jobjl.getString("source"), jobjl.getString("target"));
//									}
//
//								}
//							}
//						}
//
//					}
//				}
//
//			}
			log.info("Init della connesione a ALM");
			RestConnectorALM con = RestConnectorALM.getInstance().init(new HashMap<String, String>(),
					server.getAlmHost(), server.getAlmDomain(), server.getAlmProject());
			log.info("Login a ALM");
			AuthenticateLoginLogout authenticate = new AuthenticateLoginLogout();
			String authenticationPoint = authenticate.isAuthenticated();
			log.info("We should authenticate at: " + authenticationPoint);
			if (authenticationPoint != null) {
				authenticate.login(authenticationPoint, server.getAlmUser(), server.getAlmPassword());
			}
			authenticate.session();
			log.info("Logged in ALM");
			RestConnectorJIRA jira = RestConnectorJIRA.getInstance().init(server.getJiraHost(), server.getJiraUser(),
					server.getJiraPassword());
			SearchClient searchClient = new SearchClient();
			//
			String JIRAProject = server.getJiraProject();
//			String JIRAProject = "XTRM";
			String JIRAExportDateName = server.getJiraExportDate();

			String queryString = "project = \"" + JIRAProject + "\" And  \"" + JIRAExportDateName
					+ "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc";
//			String queryString = "project = \"" + JIRAProject + "\" AND issuetype = Bug And  \"ID QC\" ~ \"DEF2460\"";
			SearchResult result = searchClient.searchJql(queryString, 1, 0);
			Calendar maxExportDate = GregorianCalendar.getInstance();
			maxExportDate.set(2000, 1, 1, 0, 0, 0);

			if (result.getTotal() > 0) {

				result.getIssues().forEach(issue -> {
					String val = issue.getIssueFields().get("Export Date").getValue().toString();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					try {
						System.out.println(val);
						Date export_Date = formatter
								.parse(issue.getIssueFields().get("Export Date").getValue().toString());
						if (maxExportDate.getTime().before(export_Date))
							maxExportDate.setTime(export_Date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

			}
//			System.out.println(maxExportDate);
			Defects defect = new Defects();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Entities defects = defect.getDefects(
					"{last-modified [ >= '" + formatter.format(maxExportDate.getTime()) + "' ]}",
					"last-modified[DESC]");
			IssueClient issueClient = new IssueClient();
			UserClient userClient = new UserClient();
			defects.getEntities().forEach((key, value) -> {
				String qrString = "project = \"" + JIRAProject + "\" And  \"ID QC\" ~ \"DEF"
						+ value.getField("id").getValue(0) + "\" ";
				value.getFields().forEach((keyval, val) -> {
					System.out.println(val);

				});
				try {
					SearchResult res = searchClient.searchJql(qrString, 1, 0);
					if (res.getTotal() > 0) {
						System.out.println("update " + res.getTotal());
					} else {

						JSONObject json = new JSONObject();
						JSONObject fields = new JSONObject();
						fields.put("project", (new JSONObject()).put("key", JIRAProject));
						fields.put("issuetype", (new JSONObject()).put("name", "Bug"));
						fields.put("summary", value.getField("name").getValue(0));
						final StringBuffer description = new StringBuffer("");
						Document doc = Jsoup.parse(value.getField("description").getValue(0), "");
						doc.body().childNodes().forEach(node -> {

							if ("div".equals(node.nodeName())) {
								Elements els = ((Element) node).select("div > font > span");
								for (Element el : els) {
									description.append(el.text()).append("\n");
								}

							}
							if ("table".equals(node.nodeName())) {
								Elements els = ((Element) node).select("table > tbody > tr  ");
								for (Element tr : els) {

									for (Element td : tr.select(" tr > td > div > font > span ")) {
										description.append("|").append(td.text().isEmpty() ? " " : td.text());
										System.out.println(td);
									}
									description.append("\n");

								}
								description.append("\n");

							}
						});

						System.out.println(description.toString());

						// YYYY-MM-DDThh:mm:ss.sTZD

//JIRA	ALM
//Creato	Detected on date
//    //Priorità	Priority
//    //PT - Severity	Severity
//Stato	Status
//    //Riepilogo	Summary
//    //Assegnatario	Assigned To
//    //Reporter	Detected By
//    //Descrizione	Description

						fields.put("description", description.toString());
						fields.put("customfield_11000", "DEF" + value.getField("id").getValue(0));
						System.out.println(value.getField("owner").getValue(0));
						System.out.println(value.getField("detected-by").getValue(0));
						System.out.println(value.getField("status").getValue(0));
						JSONObject reporter = new JSONObject(
								userClient.searchUser(value.getField("owner").getValue(0), 1, 0));
						JSONObject assignee = new JSONObject(
								userClient.searchUser(value.getField("detected-by").getValue(0), 1, 0));
						fields.put("reporter", (new JSONObject()).put("name", reporter.getString("key")));
						fields.put("priority",
								(new JSONObject()).put("id", priorityMap.get(value.getField("user-09").getValue(0))));
						fields.put("customfield_11430", (new JSONObject()).put("value",
								severityMap.get(value.getField("severity").getValue(0))));
//						fields.put("createdtime", "2017-08-16T14:21:00.000-0500"); //value.getField("creation-time").getValue(0)) ;
//						fields.put("customfield_12603", "2017-08-16T14:21:00.000-0500"); //value.getField("creation-time").getValue(0)) ;

						// fields.put("assegnee", (new
						// JSONObject()).put("name",assegnee.getString("key")));
//						fields.put("status", value.getField("status").getValue(0) );
						json.put("fields", fields);
						JSONObject newIssue = issueClient.createIssue(json);
						String issueKey = newIssue.getString("key");
//						JSONObject updateIssue = new JSONObject();
//						fields = new JSONObject();
//						fields.put("assegnee", (new JSONObject()).put("name",assegnee.getString("key")));
//						updateIssue.put("update", new JSONObject().put("fields", fields));
						issueClient.updateAssignee(issueKey, assignee.getString("key"));
						System.out.println(json.toString(3));
						System.out.println("create 'ID QC' = " + value.getField("id").getValue(0));
					}
					// System.out.println(res);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		};
	}

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
