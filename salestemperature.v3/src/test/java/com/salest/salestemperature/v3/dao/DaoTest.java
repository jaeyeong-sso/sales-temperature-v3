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
		
		Category category = categoryDao.listingMajorProductsOfCategory("目乔");
		
		Map<String,Product> productsMap = category.getProductsMap();

		for(String key : productsMap.keySet()){
			System.out.println("[key]:" + key + "		[value]:" + productsMap.get(key).toString());
		}
		
		assertEquals(category.getName(), category.getName(), "目乔");
	}
	
	@Test
	public void listingProductsMonthlySalesVolume(){
		
		List<SalesVolume> listResult = salesVolumeDao.listingProductsMonthlySalesVolume("2014", "目乔");
		Category category = categoryDao.listingMajorProductsOfCategory("目乔");

		Set<String> productNameKeySet = category.getProductsMap().keySet();
			
		List<SalesVolume> finalResultList = new ArrayList<SalesVolume>(); 
		
		Map<String,Integer> etcItemsToatalCountMap = new HashMap<String,Integer>();
		Map<String,Long> etcItemsToatalAmountMap = new HashMap<String,Long>();
		
		for(SalesVolume itemSalesVolume : listResult){
			if( productNameKeySet.contains(itemSalesVolume.getOptItemName())){
				finalResultList.add(itemSalesVolume);
			} else {
				String dateKey = itemSalesVolume.getDate();
				if(etcItemsToatalCountMap.containsKey(dateKey)){
					etcItemsToatalCountMap.put(dateKey, etcItemsToatalCountMap.get(dateKey) + itemSalesVolume.getTotalSalesCount());
					etcItemsToatalAmountMap.put(dateKey, etcItemsToatalAmountMap.get(dateKey) + itemSalesVolume.getTotalSalesAmount());
				} else {
					etcItemsToatalCountMap.put(dateKey, itemSalesVolume.getTotalSalesCount());
					etcItemsToatalAmountMap.put(dateKey, itemSalesVolume.getTotalSalesAmount());
				}
			}
		}
		
		for(String key : etcItemsToatalCountMap.keySet()){
			finalResultList.add(new SalesVolume.SalesVolumeBuilder()
					.withDate(key)
					.withOptItemName("ETC.")
					.withTotalSalesCount(etcItemsToatalCountMap.get(key))
					.withTotalSalesAmount(etcItemsToatalAmountMap.get(key))
					.build());
		}
		
		for(SalesVolume salesVolumItem : finalResultList){
			System.out.println(salesVolumItem.toString());
		}
		
		Collections.sort(finalResultList, new Comparator<SalesVolume>() {
		    public int compare(SalesVolume o1, SalesVolume o2) {
		        return o1.getDate().compareTo(o2.getDate());
		    }
		});

		System.out.println("\r\n");
		
		for(SalesVolume salesVolumItem : finalResultList){
			System.out.println(salesVolumItem.toString());
		}
		
	}
}