package com.nexigroup.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableScheduling
@ConfigurationPropertiesScan("com.nexigroup.spring.config")
public class QuartzApplication {

//	@Autowired
//	static Job simpleJob;

	public static void main(String[] args) throws Exception {

		SpringApplication.run(QuartzApplication.class, args);
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
