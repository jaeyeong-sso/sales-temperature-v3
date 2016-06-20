package com.salest.salestemperature.v3.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/applicationContext.xml")
public class KafkaSparkProcessServiceTest {

	@Autowired
	private ApplicationContext context;
	
	private KafkaSparkProcessService kafkaSparkProcessService;
	
	@Before
	public void setUp(){
		this.kafkaSparkProcessService = context.getBean("kafkaSparkProcessService", KafkaSparkProcessService.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(kafkaSparkProcessService);
	}
	
	//@Test
	public void processMessageTest(){
		kafkaSparkProcessService.startProcessMessageStreams();
	}
	
}
