package com.nexigroup.spring.quartz;

import java.text.MessageFormat;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.Setter;

@Setter
public class SampleJob {

	// @Autowired
	// private SampleJobService jobService;
//	private String name;

	public void execute(JobExecutionContext context) throws JobExecutionException {

		System.out.println(MessageFormat.format("Job: {0}", getClass()));
	}
//	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//
//		jobService.executeSampleJob();
//	}

}