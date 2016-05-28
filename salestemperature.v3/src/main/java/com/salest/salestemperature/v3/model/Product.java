package com.salest.salestemperature.v3.model;

public class Product {
	
	private String id;
	private String name;
	private int price;
	
	private Category category;
	
	public Product(String name, String id, int price){
		this.id = id;
		this.name = name;
		this.price = price;
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
}
