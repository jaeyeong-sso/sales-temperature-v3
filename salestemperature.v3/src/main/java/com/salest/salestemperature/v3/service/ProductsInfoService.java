package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;

public class ProductsInfoService {

	private Logger logger = Logger.getLogger(ProductsInfoService.class);
	
	private CategoryDao categoryDao;
	
	@Autowired
	Provider<ProductInfoDtoService> productInfoDtoServiceProvider;
	
	private ProductInfoDtoService productInfoDtoService;
   	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}

	public List<String> getProductCategoriesInfo(){
		
		this.productInfoDtoService = productInfoDtoServiceProvider.get();
		
		List<Category> productsCategories = categoryDao.listingAllProductCategory();
		
		// Store Products list to DTO Session Bean
		productInfoDtoService.extractProductsFromCategories(productsCategories);
		//System.out.println("getProductCategoriesInfo : " + productInfoDtoService.getTestTimeStamp());

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
}
