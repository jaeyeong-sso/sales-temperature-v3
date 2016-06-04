package com.salest.salestemperature.v3.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.SalesVolume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/applicationContext.xml")
public class DaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	private SalesVolumeDao salesVolumeDao;
	private CategoryDao categoryDao;
	
	@Before
	public void setUp(){
		this.salesVolumeDao = context.getBean("salesVolumeDao", SalesVolumeDao.class);
		this.categoryDao = context.getBean("categoryDao", CategoryDao.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(salesVolumeDao);
		assertNotNull(categoryDao);
	}
	
	//@Test
	public void getMonthlySalesVolumesTest(){
		
		List<SalesVolume> salesVolumes = salesVolumeDao.listingMonthlySalesVolume("2015");
		
		for(int i=0; i<salesVolumes.size(); i++){
			System.out.println(salesVolumes.get(i).getDate() + " " +
					salesVolumes.get(i).getTotalSalesCount() + " " +
					salesVolumes.get(i).getTotalSalesAmount());
		}
		
		assertTrue("Get MonthlySalesVolumes", salesVolumes.size() > 0 );
	}
	
	//@Test
	public void listingProductCategoryTest(){
		
		List<Category> productCategories = categoryDao.listingAllProductCategory();
		

		for(int i=0; i<productCategories.size(); i++){
			Category cateItem = productCategories.get(i);
			System.out.println("[CateName] : " + cateItem.getName());
			
			Map<String,Product> productsMap = cateItem.getProductsMap();
			for(String key : productsMap.keySet()){
				System.out.println("[Product]: " + key + "		[Value]: " + productsMap.get(key).toString());
			}
		}
		
		Category category = productCategories.get(0);
		
		for(String key : category.getProductsMap().keySet()){
			Product product = category.getProductsMap().get(key);
	
			assertEquals(category.getName(), category.getName(), product.getCategory().getName());
		}
	}
	
	
	//@Test
	public void getAnnualSalesVolumeTest(){
		Map<String, Object> mapAnnualSalseVolume = salesVolumeDao.getAnnualSalesVolume("2015");
		
		for(String key : mapAnnualSalseVolume.keySet()){
			System.out.println("[key]:" + key + "		[value]:" + mapAnnualSalseVolume.get(key));
		}
		
		assertTrue(mapAnnualSalseVolume.size() > 1);
	}
	
	//@Test
	public void listingMajorProductsOfCategoryTest(){
		
		Category category = categoryDao.listingMajorProductsOfCategory("커피");
		
		Map<String,Product> productsMap = category.getProductsMap();

		for(String key : productsMap.keySet()){
			System.out.println("[key]:" + key + "		[value]:" + productsMap.get(key).toString());
		}
		
		assertEquals(category.getName(), category.getName(), "커피");
	}
	
	//@Test
	public void listingCategoriesMonthlySalesVolumeTest(){
		List<SalesVolume> categorySalesVolumes = salesVolumeDao.listingCategoriesMonthlySalesVolume("2014");
		for(SalesVolume salesVolume : categorySalesVolumes){
			System.out.println(salesVolume.toString());
		}
	}
	
	//@Test
	public void listingTimebaseSalesVolumeOfMonth(){
		List<SalesVolume> salesVolumes = salesVolumeDao.listingTimebaseSalesVolumeOfMonth("2014-09");
		for(SalesVolume salesVolume : salesVolumes){
			System.out.println(salesVolume.toString());
		}
	}
	
	//@Test
	public void listingDayOfWeekSalesVolumeOfMonth(){
		List<SalesVolume> salesVolumes = salesVolumeDao.listingDayOfWeekSalesVolumeOfMonth("2014-09");
		for(SalesVolume salesVolume : salesVolumes){
			System.out.println(salesVolume.toString());
		}
	}
}