package com.salest.salestemperature.v3.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;

@Component
@Scope("session")
public class ProductInfoDtoService {

	//private String testTimeStamp;
	
	private List<Product> products;
	
	public ProductInfoDtoService(){
		products = new ArrayList<Product>();
		//this.testTimeStamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
	}
	
	/*
	public String getTestTimeStamp(){
		return this.testTimeStamp;
	}
	*/
	
	public List<Product> getProducts(){
		return this.products;
	}
	
	public void extractProductsFromCategories(List<Category> categories){
		products.clear();
		for(Category category : categories){
			for(String productKey : category.getProductsMap().keySet()){
				products.add(category.getProductsMap().get(productKey));
			}
		}
	}

}
