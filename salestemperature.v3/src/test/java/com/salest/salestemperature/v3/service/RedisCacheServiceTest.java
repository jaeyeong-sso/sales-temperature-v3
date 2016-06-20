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
public class RedisCacheServiceTest {
	
	@Autowired
	private ApplicationContext context;
	private RedisCacheService redisCacheService;
	
	@Before
	public void setUp(){
		this.redisCacheService = context.getBean("redisCacheService", RedisCacheService.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(redisCacheService);
	}
	
	//@Test
	public void readWriteTest(){
		
		String testKey = "test_key";
		String testValue = String.valueOf(Integer.valueOf(100));
		
		redisCacheService.createOrUpdateValueByKey(testKey, testValue);
		assertEquals(redisCacheService.readValueByKey(testKey), testValue);
	}
	
	//@Test
	public void incrCounterUpdateTest(){
		
		String testKey = "total_sales_count_of_day:2016-06-20";

		redisCacheService.deleteKey(testKey);
		
		assertEquals(redisCacheService.createOrIncrCounterValue(testKey, 1), 1L);
		assertEquals(redisCacheService.createOrIncrCounterValue(testKey, 1), 2L);
		assertEquals(redisCacheService.createOrIncrCounterValue(testKey, 1), 3L);
	}
	
}
