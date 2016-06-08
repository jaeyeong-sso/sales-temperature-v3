package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;

public class ProductsInfoService {

	//private Logger logger = Logger.getLogger(ProductsInfoService.class);
	
	private CategoryDao categoryDao;
	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}
	
	public List<String> getProductCategoriesInfo(){
		
		List<Category> productsCategories = categoryDao.listingAllProductCategory();
		
		List<String> categoriesList = new ArrayList<String>();
		
		for(Category category : productsCategories){
			categoriesList.add(category.getName());
		}
		
		return categoriesList;
	}
	
	public List<Product> getProductItemsDetails(final String categoryName){
		
		List<Product> productsList = new ArrayList<Product>();
		Category category = categoryDao.listingMajorProductsOfCategory(categoryName);
		
		for(String key : category.getProductsMap().keySet()){
			productsList.add(category.getProductsMap().get(key));
		}
		
		return productsList;
	}
	
	public void writeLogToFlume(String message){
		//logger.info(message);
	}
	
}
