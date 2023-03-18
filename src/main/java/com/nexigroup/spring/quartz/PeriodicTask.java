package com.nexigroup.spring.quartz;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PeriodicTask {

	@Scheduled(cron = "${cron-string}")
	public void everyFiveSeconds() {
		log.info("Periodic task: " + new Date());
	}
}