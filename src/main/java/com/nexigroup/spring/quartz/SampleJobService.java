package com.nexigroup.spring.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SampleJobService {

	public static final long EXECUTION_TIME = 5000L;

	private AtomicInteger count = new AtomicInteger();

	public void executeSampleJob() {

		log.info("The sample job has begun...");
		try {
			Thread.sleep(EXECUTION_TIME);
		} catch (InterruptedException e) {
			log.error("Error while executing sample job", e);
		} finally {
			count.incrementAndGet();
			log.info("Sample job has finished...");
		}
	}

	public int getNumberOfInvocations() {
		return count.get();
	}
}