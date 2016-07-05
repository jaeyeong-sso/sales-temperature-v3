package com.salest.salestemperature.v3.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/applicationContext.xml")
public class TimeSeriesServiceTest {

	@Autowired
	private ApplicationContext context;

	private TimeSeriesService timeSeriesService;
	
	@Before
	public void setUp(){
		this.timeSeriesService = context.getBean("timeSeriesService", TimeSeriesService.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(timeSeriesService);
	}
	
	//@Test
	public void getPastTargetDateTest(){
		System.out.println( timeSeriesService.getPastWeekMonthIdxDate("2016-07-04") );
		assertEquals( timeSeriesService.getPastWeekMonthIdxDate("2016-07-04"), "2015-07-06");
	}
}
