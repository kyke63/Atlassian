package com.nexigroup.spring.quartz;

import java.text.MessageFormat;


import lombok.Setter;

@Setter
public class SampleJob {

	// @Autowired
	// private SampleJobService jobService;
//	private String name;

	public void execute()  {

		System.out.println(MessageFormat.format("Job: {0}", getClass()));
	}
//	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//
//		jobService.executeSampleJob();
//	}

}