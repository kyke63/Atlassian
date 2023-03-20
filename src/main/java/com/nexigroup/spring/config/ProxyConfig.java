package com.nexigroup.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ProxyConfig {
	@Value("${http.proxyHost}")
	private String proxyHost;
	@Value("${http.proxyPort}")
	private String proxyPort;
	@Value("${http.proxyUser}")
	private String proxyUser;
	@Value("${http.proxyPassword}")
	private String proxyPassword;
	@Value("${http.nonProxyHosts}")
	private String nonProxyHosts;

}
