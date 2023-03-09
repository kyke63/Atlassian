package com.nexigroup.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nexigroup.spring.alm.AuthenticateLoginLogout;
import com.nexigroup.spring.alm.Defects;
import com.nexigroup.spring.alm.Entities;
import com.nexigroup.spring.alm.infrastructure.RestConnectorALM;
import com.nexigroup.spring.jira.ProjectClient;
import com.nexigroup.spring.jira.SearchClient;
import com.nexigroup.spring.jira.infrastructure.RestConnectorJIRA;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TasTopApplication {

	private Properties origProp;
	private Properties proxyProp;

	public static void main(String[] args) throws Exception {
		Properties origProp = System.getProperties();
		Properties proxyProp = new Properties(origProp);
		proxyProp.setProperty("http.proxyHost", "10.47.5.41");
		proxyProp.setProperty("http.proxyPort", "8080");
		proxyProp.setProperty("http.proxyUser", "NicotraLeopoldo");
		proxyProp.setProperty("http.proxyPassword", "Renato87!?");
		proxyProp.setProperty("http.nonProxyHosts", "localhost|127.0.0.1|qualitycenter-te.sia.local|qualitycenter.sia.local");
		proxyProp.setProperty("https.proxyHost", "10.47.5.41");
		proxyProp.setProperty("https.proxyPort", "8080");
		proxyProp.setProperty("https.nonProxyHosts", "localhost|127.0.0.1|qualitycenter-te.sia.local|qualitycenter.sia.local");
		proxyProp.setProperty("https.proxyUser", "NicotraLeopoldo");
		proxyProp.setProperty("https.proxyPassword", "Renato87!?");
		System.setProperties(proxyProp);

		SpringApplication.run(TasTopApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return (args) -> {
			log.info("Init della connesione a ALM");
			RestConnectorALM con = RestConnectorALM.getInstance().init(new HashMap<String, String>(),
					"http://qualitycenter.sia.local:8080", "POST_TRADING", "T2S");
			log.info("Login a ALM");
			AuthenticateLoginLogout authenticate = new AuthenticateLoginLogout();
			String authenticationPoint = authenticate.isAuthenticated();
			log.info("We should authenticate at: " + authenticationPoint);
			if (authenticationPoint != null) {
				authenticate.login(authenticationPoint, "TaskTop", "cNzZTMR4");
			}
			authenticate.session();
			log.info("Logged in ALM" + authenticationPoint);
			Defects defect = new Defects();
			//"query="
			Entities defects = defect.getDefects("{id[ = 2460 ]}");
//			Map<String, Map<String, String[]>> defects = defect.getDefects("");
			String defectID="2460" ;
			System.out.println(defects);
//			for (String key : defects.get(defectID).keySet()) {
//				if (defects.get(defectID).get(key).length > 0)  
//					System.out.println("'"+key+"' <--> '"+defects.get(defectID).get(key)[0]+"'");
//				else 
//					System.out.println("'"+key+"' <--> 'null'");
//			}
			
			log.info("Ask for Defects in ALM ");
			RestConnectorJIRA jira = RestConnectorJIRA.getInstance().init("https://corporate.sia.eu/jira",
					"NicotraLeopoldo", "Renato87!?");
			ProjectClient projectClient = new ProjectClient();
////			Map<String, Map<String,String>> projects =projectClient.getAllProjects(); 
////			System.out.println(projects);
			String JIRAProject = "XTRM";
			String JIRAExportDateName = "Export Date";
//			Map<String, String[]> project =projectClient.getProject(JIRAProject);
//			System.out.println(project.get("issueTypes")[0]);
			SearchClient searchClient = new SearchClient();
			
			
			String queryString = "project = \"" + JIRAProject + "\" And  \"" + JIRAExportDateName + "\" is not EMPTY  Order By \"" + JIRAExportDateName + "\" Desc";
//			String queryString = "project = \"" + JIRAProject + "\" AND issuetype = Bug And  \"ID QC\" ~ \"DEF2460\"";
			searchClient.searchJql(queryString, 1, 0 );
////			Map<String, Map<String, String>> customFields = searchClient.getCustomFields();
////			System.out.println(customFields.size());
//
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
