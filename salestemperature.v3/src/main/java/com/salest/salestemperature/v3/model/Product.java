package com.salest.salestemperature.v3.model;

public class Product {
	
	private String id;
	private String name;
	private int price;
	
	private Category category;
	
	private Product(ProductBuilder builder){
		this.id = builder.id;
		this.name = builder.name;
		this.price = builder.price;
	}
	
	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}

	public int getPrice(){
		return this.price;
	}
	public Category getCategory(){
		return this.category;
	}
	
	public void setCategory(Category category){
		this.category = category;
	}

	@Override
	public String toString() {
		
		return new StringBuffer("id:").append(this.id)
				.append(" name:").append(this.name)
				.append(" price:").append(this.price).toString();

	}
	
	
	public static class ProductBuilder {
		private String id;
		private String name;
		private int price;
		
		public ProductBuilder(){
			this.id = null;
			this.name = null;
			this.price = 0;
		}
		public Product build() {
	        return new Product(this);
	    }
		public ProductBuilder withId(String id){
			this.id = id;
			return this;
		}
		public ProductBuilder withName(String name){
			this.name = name;
			return this;
		}
		public ProductBuilder withPrice(int price){
			this.price = price;
			return this;
		}

	}
}
