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
import com.salest.salestemperature.v3.model.ProductSalesVolume;
import com.salest.salestemperature.v3.model.SalesVolume;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/config/applicationContext.xml")
public class DaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	private SalesVolumeDao salesVolumeDao;
	private CategoryDao categoryDao;
	private ProductSalesVolumeDao productSalesVolumeDao;
	
	@Before
	public void setUp(){
		this.salesVolumeDao = context.getBean("salesVolumeDao", SalesVolumeDao.class);
		this.categoryDao = context.getBean("categoryDao", CategoryDao.class);
		this.productSalesVolumeDao = context.getBean("productSalesVolumeDao", ProductSalesVolumeDao.class);
	}
	
	@Test
	public void beanLoadingTest(){
		assertNotNull(salesVolumeDao);
		assertNotNull(categoryDao);
		assertNotNull(productSalesVolumeDao);
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
	
	// @Test
	public void listingProductCategoryTest(){
		
		List<Category> productCategories = categoryDao.listingProductCategory();
		
		/*
		for(int i=0; i<productCategories.size(); i++){
			Category cateItem = productCategories.get(i);
			System.out.println("[CateName] : " + cateItem.getName());
			for(int j=0; j<cateItem.getProducts().size(); j++){
				Product productItem = cateItem.getProducts().get(j);
				System.out.println("   >> " + productItem.getName() + " , " + productItem.getId() + " , " + productItem.getPrice());
			}
		}
		*/
		
		Category category = productCategories.get(0);
		Product product = category.getProducts().get(0);
		
		Category category_fo_product = product.getCategory();
		
		assertEquals(category.getName(), category.getName(), category_fo_product.getName());
	}
	
	// @Test
	public void listingProductsBySalesVolumeTest(){
		
		List<ProductSalesVolume> productsBySalesVolume = productSalesVolumeDao.listingProductsBySalesVolume();
		
		for(ProductSalesVolume productSalesVolume : productsBySalesVolume){
			System.out.println(productSalesVolume.getCateName() + " , " + productSalesVolume.getId()  + " , " +  productSalesVolume.getTotalSalesAmount());
		}

		assertNotNull(productsBySalesVolume);
	}
	
	//@Test
	public void getAnnualSalesVolumeTest(){
		Map<String, Object> mapAnnualSalseVolume = salesVolumeDao.getAnnualSalesVolume("2015");
		
		for(String key : mapAnnualSalseVolume.keySet()){
			System.out.println("[key]:" + key + "		[value]:" + mapAnnualSalseVolume.get(key));
		}
		
		assertTrue(mapAnnualSalseVolume.size() > 1);
	}
}