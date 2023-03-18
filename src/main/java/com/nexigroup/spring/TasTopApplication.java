package com.nexigroup.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;

import com.nexigroup.spring.config.ProxyConfig;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@ConfigurationPropertiesScan("com.nexigroup.spring.config")
public class TasTopApplication {

	@Autowired
	private ProxyConfig proxy;
	@Autowired
	private ApplicationContext ctx;

	public static void main(String[] args) throws Exception {

		SpringApplication.run(TasTopApplication.class, args);
	}

	/*
	 * @Bean public CommandLineRunner init() { return (args) -> { Properties
	 * proxyProp = new Properties(System.getProperties());
	 * proxyProp.setProperty("http.proxyHost", proxy.getProxyHost());
	 * proxyProp.setProperty("http.proxyPort", proxy.getProxyPort());
	 * proxyProp.setProperty("http.proxyUser", proxy.getProxyUser());
	 * proxyProp.setProperty("http.proxyPassword", proxy.getProxyPassword());
	 * proxyProp.setProperty("http.nonProxyHosts", proxy.getNonProxyHosts());
	 * 
	 * proxyProp.setProperty("https.proxyHost", proxy.getProxyHost());
	 * proxyProp.setProperty("https.proxyPort", proxy.getProxyPort());
	 * proxyProp.setProperty("https.proxyUser", proxy.getProxyUser());
	 * proxyProp.setProperty("https.proxyPassword", proxy.getProxyPassword());
	 * proxyProp.setProperty("https.nonProxyHosts", proxy.getNonProxyHosts());
	 * System.setProperties(proxyProp);
	 * 
	 * Resource qcResouce = ctx.getResource(proxy.getQc_jira_mapper()); InputStream
	 * is = qcResouce.getInputStream(); ByteArrayOutputStream container = new
	 * ByteArrayOutputStream();
	 * 
	 * byte[] buf = new byte[1024]; int read; while ((read = is.read(buf, 0, 1024))
	 * > 0) { container.write(buf, 0, read); }
	 * 
	 * JSONObject json = new JSONObject(new String(container.toByteArray()));
	 * 
	 * System.out.println(json.getJSONArray("updated").getJSONObject(0).
	 * getJSONObject("original")
	 * .getJSONObject("element").getJSONObject("mappings").getJSONArray(
	 * "field-mappings").getJSONObject(0)
	 * .getJSONArray("sources").getJSONObject(0).getString("id"));
	 * 
	 * for (int i = 0; i < json.getJSONArray("updated").length(); i++) { JSONObject
	 * jobj =
	 * json.getJSONArray("updated").getJSONObject(i).getJSONObject("original")
	 * .getJSONObject("element").getJSONObject("mappings"); for (int k = 0; k <
	 * jobj.getJSONArray("field-mappings").length(); k++) { JSONObject jobjk =
	 * jobj.getJSONArray("field-mappings").getJSONObject(k); for (int l = 0; l <
	 * jobjk.getJSONArray("sources").length(); l++) { JSONObject jobjl =
	 * jobjk.getJSONArray("sources").getJSONObject(l);
	 * 
	 * System.out.println("source   -->" + jobjl.getString("id"));
	 * System.out.println("source   -->" + jobjl.getString("label"));
	 * 
	 * } for (int l = 0; l < jobjk.getJSONArray("targets").length(); l++) {
	 * JSONObject jobjl = jobjk.getJSONArray("targets").getJSONObject(l);
	 * 
	 * System.out.println("target   -->" + jobjl.getString("id"));
	 * System.out.println("target   -->" + jobjl.getString("label"));
	 * 
	 * }
	 * 
	 * if (jobjk.has("value-transformation")) {
	 * 
	 * System.out .println("trasf id -->" +
	 * jobjk.getJSONObject("value-transformation").getString("id"));
	 * 
	 * if (jobjk.getJSONObject("value-transformation").has("configuration")) {
	 * JSONObject jobjtmpl = jobjk.getJSONObject("value-transformation")
	 * .getJSONObject("configuration"); if (jobjtmpl.has("mappings")) { for (int l =
	 * 0; l < jobjtmpl.getJSONArray("mappings").length(); l++) { JSONObject jobjl =
	 * jobjtmpl.getJSONArray("mappings").getJSONObject(l); if (jobjl.has("source")
	 * && jobjl.has("target")) { System.out.println("trasf    -->" +
	 * jobjl.getString("source")); System.out.println("trasf    -->" +
	 * jobjl.getString("target")); }
	 * 
	 * } } }
	 * 
	 * }
	 * 
	 * System.out.println();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * // log.info("Init della connesione a ALM"); // RestConnectorALM con =
	 * RestConnectorALM.getInstance().init(new HashMap<String, String>(), //
	 * "http://qualitycenter.sia.local:8080", "POST_TRADING", "T2S"); //
	 * log.info("Login a ALM"); // AuthenticateLoginLogout authenticate = new
	 * AuthenticateLoginLogout(); // String authenticationPoint =
	 * authenticate.isAuthenticated(); // log.info("We should authenticate at: " +
	 * authenticationPoint); // if (authenticationPoint != null) { //
	 * authenticate.login(authenticationPoint, "TaskTop", "cNzZTMR4"); // } //
	 * authenticate.session(); // log.info("Logged in ALM" + authenticationPoint);
	 * // Defects defect = new Defects(); // // "query=" // Entities defects =
	 * defect.getDefects("{id[ = 2460 ]}"); //// Map<String, Map<String, String[]>>
	 * defects = defect.getDefects(""); // String defectID = "2460"; //
	 * System.out.println(defects); //// for (String key :
	 * defects.get(defectID).keySet()) { //// if
	 * (defects.get(defectID).get(key).length > 0) ////
	 * System.out.println("'"+key+"' <--> '"+defects.get(defectID).get(key)[0]+"'");
	 * //// else //// System.out.println("'"+key+"' <--> 'null'"); //// } // //
	 * log.info("Ask for Defects in ALM "); // RestConnectorJIRA jira =
	 * RestConnectorJIRA.getInstance().init("https://corporate.sia.eu/jira", //
	 * "NicotraLeopoldo", "Renato87!?"); // ProjectClient projectClient = new
	 * ProjectClient(); ////// Map<String, Map<String,String>> projects
	 * =projectClient.getAllProjects(); ////// System.out.println(projects); //
	 * String JIRAProject = "XTRM"; // String JIRAExportDateName = "Export Date";
	 * //// Map<String, String[]> project =projectClient.getProject(JIRAProject);
	 * //// System.out.println(project.get("issueTypes")[0]); // SearchClient
	 * searchClient = new SearchClient(); // // String queryString = "project = \""
	 * + JIRAProject + "\" And  \"" + JIRAExportDateName // +
	 * "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc"; //// String
	 * queryString = "project = \"" + JIRAProject +
	 * "\" AND issuetype = Bug And  \"ID QC\" ~ \"DEF2460\""; //
	 * searchClient.searchJql(queryString, 1, 0); ////// Map<String, Map<String,
	 * String>> customFields = searchClient.getCustomFields(); //////
	 * System.out.println(customFields.size()); //// }; }
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
