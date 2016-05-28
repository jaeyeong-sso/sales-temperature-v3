package com.salest.salestemperature.v3.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
	
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
}
