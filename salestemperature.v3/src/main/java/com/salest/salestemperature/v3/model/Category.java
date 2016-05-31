package com.salest.salestemperature.v3.model;

import java.util.HashMap;
import java.util.Map;

public class Category {
	
	private String name;
	private Map<String,Product> productsMap;
	
	private Category(CategoryBuilder builder){
		this.name = builder.name;
		this.productsMap = builder.productsMap;
		
		for(String key : builder.productsMap.keySet()){
			productsMap.get(key).setCategory(this);
		}
	}
	public String getName(){
		return this.name;
	}	
	public Map<String,Product> getProductsMap(){
		return this.productsMap;
	}
	
	
	public static class CategoryBuilder {
		private String name;
		private Map<String,Product> productsMap;
		
		public CategoryBuilder(){
			this.name = "";
			this.productsMap = new HashMap<String,Product>();
		}
		public Category build() {
	        return new Category(this);
	    }
		public CategoryBuilder withName(String categoryName){
			this.name = categoryName;
			return this;
		}
		public CategoryBuilder withProductsMap(Map<String,Product> productsMap){
			this.productsMap = productsMap;
			return this;
		}
		public CategoryBuilder withAddToProductsMap(String productKey,Product productItem){
			this.productsMap.put(productKey, productItem);
			return this;
		}
	}
	
	
	/*
	private String name;
	private List<Product> products;
	
	private Category(CategoryBuilder builder){
		this.name = builder.name;
		this.products = builder.products;
		
		for(Product product : products){
			product.setCategory(this);
		}
	}
	public String getName(){
		return this.name;
	}
	public List<Product> getProducts(){
		return this.products;
	}
	
	
	public static class CategoryBuilder {
		
		private String name;
		private List<Product> products;

		public CategoryBuilder(){
			this.name = null;
			this.products = new ArrayList<Product>();
		}
		
		public Category build() {
	        return new Category(this);
	    }
		public CategoryBuilder withName(String name){
			this.name = name;
			return this;
		}
		public CategoryBuilder withProducts(List<Product> items){
			this.products = items;
			return this;
		}
		public CategoryBuilder withAddProduct(Product item){
			this.products.add(item);
			return this;
		}
	}
	*/
}
