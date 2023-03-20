package com.nexigroup.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
public class ServerConfig {
		
	@Value("${jira.server.baseurl}")
	private String jiraHost;
	@Value("${jira.server.user}")
	private String jiraUser;
	@Value("${jira.server.password}")
	private String jiraPassword;
	@Value("${jira.server.project}")
	private String jiraProject;
	@Value("${jira.server.project.exportDate}")
	private String jiraExportDate;

	@Value("${alm.server.baseurl}")
	private String almHost;
	@Value("${alm.server.user}")
	private String almUser;
	@Value("${alm.server.password}")
	private String almPassword;
	@Value("${alm.server.domain}")
	private String almDomain;
	@Value("${alm.server.project}")
	private String almProject;
	
	@Setter
	private Boolean almLogin = false;

	
}
