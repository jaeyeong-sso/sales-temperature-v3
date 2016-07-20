package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ProductsInfoService {

	private Logger logger = Logger.getLogger(ProductsInfoService.class);
	
	private CategoryDao categoryDao;
	private RedisCacheService redisCacheService;
	
	@Autowired
	Provider<ProductInfoDtoService> productInfoDtoServiceProvider;
	
	private ProductInfoDtoService productInfoDtoService;
   	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}
	public void setRedisCacheService(RedisCacheService redisCacheService){
		this.redisCacheService = redisCacheService;
	}

	public List<String> getProductCategoriesInfo(){
		
		this.productInfoDtoService = productInfoDtoServiceProvider.get();
		
		List<Category> productsCategories = null;
		
		//Read From Redis Cache.
		List<String> categoriesStrCached = redisCacheService.getFromList(RedisCacheService.CATEGORIES_INFO);
		
		if(categoriesStrCached!=null && categoriesStrCached.size()>0){
			//System.out.println("Read From Redis Cached");
			productsCategories = new ArrayList<Category>();
			
			for(String categoryStr : categoriesStrCached){
				Category categoryRef = null;
				try {
					categoryRef = parseCategoryFromJsonObjectString(categoryStr);
					productsCategories.add(categoryRef);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			productsCategories = categoryDao.listingAllProductCategory();
			
			//Store To Redis Cache.
			for(Category category : productsCategories){
				redisCacheService.addToList(RedisCacheService.CATEGORIES_INFO, category.toJsonString());
			}
		}

		productInfoDtoService.extractProductsFromCategories(productsCategories);
		
		List<String> categoriesList = new ArrayList<String>();
		
		for(Category category : productsCategories){
			categoriesList.add(category.getName());
		}
		
		return categoriesList;
	}
	
	public List<Product> getProductItemsDetails(final String categoryName){
		
		List<Product> productsList = new ArrayList<Product>();
		Category category = null;
		
		String REDIS_KEY = RedisCacheService.MAJOR_PRODUCTS_INFO_OF  + categoryName;
		
		//Read From Redis Cache.
		String categoryStrCached = redisCacheService.readValueByKey(REDIS_KEY);
		
		if(categoryStrCached!=null){
			//System.out.println("Read From Redis Cached");
			try {
				category = parseCategoryFromJsonObjectString(categoryStrCached);
	
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				category = null;
			}
			
		} else {
			category = categoryDao.listingMajorProductsOfCategory(categoryName);
			
			//Store To Redis Cache.
			redisCacheService.createOrUpdateValueByKey(REDIS_KEY, category.toJsonString());
		}
		
		
		for(String key : category.getProductsMap().keySet()){
			productsList.add(category.getProductsMap().get(key));
		}
		
		return productsList;
	}
	
	private Category parseCategoryFromJsonObjectString(String categoryStr) throws ParseException{

		JSONParser jsonParser = new JSONParser();
		JSONObject categoryJsonObj = (JSONObject)jsonParser.parse(categoryStr);
			
		String categoryName = (String) categoryJsonObj.get("name");
		JSONArray productsJsonArray = (JSONArray) categoryJsonObj.get("products");
			
		Map<String,Product> productsMap = new HashMap<String,Product>();
			
		for(int i=0; i<productsJsonArray.size(); i++){
			JSONObject productJsonObj = (JSONObject) productsJsonArray.get(i);
						
			productsMap.put(
					((String) productJsonObj.get("name")), 
						new Product.ProductBuilder()
							.withId((String) productJsonObj.get("id"))
							.withName((String)productJsonObj.get("name"))
							.withPrice(((Long)productJsonObj.get("price")).intValue())
						.build()
					);
			}
		
		return new Category.CategoryBuilder().withName(categoryName).withProductsMap(productsMap).build();
	}
}
