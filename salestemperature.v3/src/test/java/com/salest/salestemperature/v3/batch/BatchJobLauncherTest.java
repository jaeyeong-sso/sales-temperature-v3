package com.salest.salestemperature.v3.batch;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/spring-batch-service.xml")
public class BatchJobLauncherTest {
	
	@Autowired
	private ApplicationContext context;

	private JobLauncher jobLauncher;
	
	@Before
	public void setUp(){
		this.jobLauncher = context.getBean("jobLauncher", org.springframework.batch.core.launch.support.SimpleJobLauncher.class);

	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(jobLauncher);
	}
}
