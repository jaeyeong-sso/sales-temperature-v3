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
public class AnalyzeSalesVolumeServiceTest {
	
	@Autowired
	private ApplicationContext context;

	private AnalyzeSalesVolumeService analyzeSalesVolumeService;
	
	@Before
	public void setUp(){
		this.analyzeSalesVolumeService = context.getBean("popularProductsService", AnalyzeSalesVolumeService.class);

	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(analyzeSalesVolumeService);
	}

	//@Test
	public void annualSalesVolumeTest(){
		AnnualSalesVolumeSummary annualSalesVolumeSummary = analyzeSalesVolumeService.getAnnualSalesVolume("2014");
	}
	
	
	//@Test
	public void getMonthlySalesVolumeByCategories(){
		
		List<SalesVolume> listResult;
		
		listResult = analyzeSalesVolumeService.getMonthlySalesVolumeByCategories("2014");
		
		System.out.println("\r\n[START] - getMonthlySalesVolumeByCategories - [END]\r\n");
		
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}

		assertTrue(listResult.size() > 0);
	}
	
	//@Test
	public void getMonthlySalesVolumeByProductsTest(){
		
		List<SalesVolume> listResult = analyzeSalesVolumeService.getMonthlySalesVolumeByProducts("2014","¶ó¶¼");
		
		System.out.println("\r\n[START] - getMonthlySalesVolumeByProductsTest - [END]\r\n");
		
		/*
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}
		*/
		
		assertTrue(listResult.size() > 0);
	}
	
	//@Test
	public void getTimebaseSalesVolumeOfMonthTest(){
		List<SalesVolume> listResult = analyzeSalesVolumeService.getTimebaseSalesVolumeOfMonth("2014-09");
		
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}

		assertTrue(listResult.size() > 0);
	}
	
	//@Test
	public void getDayOfWeekSalesVolumeOfMonthTest(){
		List<SalesVolume> listResult = analyzeSalesVolumeService.getDayOfWeekSalesVolumeOfMonth("2014-09");
		
		for(SalesVolume salesVolumeItem : listResult){
			System.out.println(salesVolumeItem.toString());
		}
	
		assertTrue(listResult.size() > 0);
	}
}
