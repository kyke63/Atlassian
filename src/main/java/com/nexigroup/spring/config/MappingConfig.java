package com.nexigroup.spring.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Getter
public class MappingConfig {

	@Value("#{${severity}}")
	private Map<String, String> severityMap;
	@Value("#{${priority}}")
	private Map<String, String> priorityMap;
	@Value("#{${qc.jira.mapper}}")
	private Map<String, String> qc_jira_mapper;
//	@Value("#{${jira.qc.mapper}}")
//	private Map<String, String> jira_qc_mapper;

	@Setter
	private Map<String, Object> fields;

}
