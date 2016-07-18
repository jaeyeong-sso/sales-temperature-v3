package com.salest.salestemperature.v3.web.request.model;

public class ProductDetailResponse {
	
	private String id;
	private String name;
	private int price;
	
	public ProductDetailResponse(String id,String name,int price){
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
}
