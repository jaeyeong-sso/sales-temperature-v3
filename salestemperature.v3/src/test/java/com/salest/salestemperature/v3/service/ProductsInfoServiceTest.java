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
public class ProductsInfoServiceTest {
	
	@Autowired
	private ApplicationContext context;

	private ProductsInfoService productsInfoService;
	
	@Before
	public void setUp(){
		this.productsInfoService = context.getBean("productsInfoService", ProductsInfoService.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(productsInfoService);
	}
	
	//@Test
	public void getProductCategoriesInfoTest(){
		assertTrue(productsInfoService.getProductCategoriesInfo().size() > 0);
	}
	
	//@Test
	public void getProductItemsDetailsTest(){
		assertTrue(productsInfoService.getProductItemsDetails("Ŀ��").size() > 0);
	}
}
