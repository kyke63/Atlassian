package com.nexigroup.spring.quartz.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.nexigroup.spring.quartz.SampleJob;

@Configuration
public class QuartzConfig {

//	@Bean
//	@QuartzDataSource
//	public DataSource quartzDataSource() {
//		return DataSourceBuilder.create().build();
//	}

//	@Bean
//	public JobDetail jobDetail() {
//		return JobBuilder.newJob().ofType(SampleJob.class).storeDurably().withIdentity("Qrtz_Job_Detail")
//				.withDescription("Invoke Sample Job service...").build();
//	}

	@Bean
	public JobDetailFactoryBean jobDetail() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(SampleJob.class);
		jobDetailFactory.setDescription("Invoke Sample Job service...");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}

//	@Bean
//	public Trigger trigger(JobDetail job) {
//		return TriggerBuilder.newTrigger().forJob(job).withIdentity("Qrtz_Trigger").withDescription("Sample trigger")
//				.withSchedule(simpleSchedule().repeatForever().withIntervalInHours(1)).build();
//	}
	@Bean
	public SimpleTriggerFactoryBean trigger(JobDetail job) {
		SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setJobDetail(job);
		trigger.setRepeatInterval(1000);
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		return trigger;
	}
}
