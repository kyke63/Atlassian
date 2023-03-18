package com.nexigroup.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.nexigroup.spring.config.MappingConfig;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableScheduling
@ConfigurationPropertiesScan("com.nexigroup.spring.config")
public class QuartzApplication {

//	@Autowired
//	static Job simpleJob;

	@Autowired
	private MappingConfig mapping;

	public static void main(String[] args) throws Exception {

		SpringApplication.run(QuartzApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return (args) -> {

			log.info("--->" + mapping.getSeverity());
			log.info("--->" + mapping.getSeverityMap());

		};

	}

//	private static void onStartup() throws SchedulerException {
//
//		JobDetail job = JobBuilder
//
//				.newJob(simpleJob.getClass()).usingJobData("param", "value") // add
//				// a
//				// parameter
//				.build();
//
//		Date afterFiveSeconds = Date
//				.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant());
//		Trigger trigger = TriggerBuilder.newTrigger().startAt(afterFiveSeconds).build();
//
//		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//		Scheduler scheduler = schedulerFactory.getScheduler();
//		scheduler.start();
//		scheduler.scheduleJob(job, trigger);
//	}

}
