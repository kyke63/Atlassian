package com.nexigroup.spring.quartz;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nexigroup.spring.config.MappingConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PeriodicTask {

	@Autowired
	private MappingConfig mappingConfig;

	@Scheduled(cron = "${cron-string}")
	public void everyFiveSeconds() {
		log.info("Periodic task: " + new Date());

		log.info("--->" + mappingConfig.getSeverityMap());
		log.info("--->" + mappingConfig.getPriorityMap());
		log.info("--->" + mappingConfig.getFields());
	}
}