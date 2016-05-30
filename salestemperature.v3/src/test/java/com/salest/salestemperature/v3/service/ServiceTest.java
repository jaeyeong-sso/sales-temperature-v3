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
public class ServiceTest {
	
	@Autowired
	private ApplicationContext context;

	private PopularProductsService popularProductsService;
	
	@Before
	public void setUp(){
		this.popularProductsService = context.getBean("popularProductsService", PopularProductsService.class);

	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(popularProductsService);
	}
	
	@Test
	public void getPopularProductsTest(){
		assertTrue("getMostPopularProducts <= 0 ", this.popularProductsService.getMostPopularProducts().size() > 0);
	}
	
}
