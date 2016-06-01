package com.salest.salestemperature.v3.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.model.SalesVolume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/applicationContext.xml")
public class ServiceTest {
	
	@Autowired
	private ApplicationContext context;

	private AnalyzeProductSalesVolumeService analyzeProductSalesVolumeService;
	
	@Before
	public void setUp(){
		this.analyzeProductSalesVolumeService = context.getBean("popularProductsService", AnalyzeProductSalesVolumeService.class);

	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(analyzeProductSalesVolumeService);
	}

	@Test
	public void annualSalesVolumeTest(){
		AnnualSalesVolumeSummary annualSalesVolumeSummary = analyzeProductSalesVolumeService.getAnnualSalesVolume("2014");
	}
	
	
	//@Test
	public void salesContributionPerMenuTest(){
		
		List<SalesVolume> listResult;
		
		listResult = analyzeProductSalesVolumeService.getMonthlySalesVolumeByCategories("2014");
		
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}
		/*
		listResult = analyzeProductSalesVolumeService.getMonthlySalesVolumeByProducts("2014","¶ó¶¼");
		
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}
		*/
		assertTrue(listResult.size() > 0);
	}
	
}
