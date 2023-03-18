package com.nexigroup.spring.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class MappingConfig {

	@Value("${severity}")
	private Map<String, String> severity;
	@Value("#{${severity}}")
	private Map<String, String> severityMap;

}
