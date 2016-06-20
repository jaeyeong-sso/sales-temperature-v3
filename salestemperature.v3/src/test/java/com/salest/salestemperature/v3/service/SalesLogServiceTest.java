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
public class SalesLogServiceTest {
	
	@Autowired
	private ApplicationContext context;

	private SalesLogService salesLogService;
	
	@Before
	public void setUp(){
		this.salesLogService = context.getBean("salesLogService", SalesLogService.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(salesLogService);
	}
	
	//@Test
	public void writeSalesLogTest(){
		salesLogService.writeSalesLog("커피", "transaction_date", "transaction_time");
	}
}