package com.salest.salestemperature.v3.web.request.model;

public class SalesAmountProportionResponse {

	private String itemName;
	private long totalAmount;
	private float percentage;
	
	public SalesAmountProportionResponse(){}
	
	public SalesAmountProportionResponse(String itemName, long totalAmount){
		this.itemName = itemName;
		this.totalAmount = totalAmount;
		this.percentage = 0.0f;
	}
	
	public String getItemName(){
		return this.itemName;
	}
	public long getTotalAmount(){
		return this.totalAmount;
	}
	public float getPercentage(){
		return this.percentage;
	}
	
	public void setItemName(String itemName){
		this.itemName = itemName;
	}
	public void setTotalAmount(long totalAmount){
		this.totalAmount = totalAmount;
	}
	public void setPercentage(float percentage){
		this.percentage = percentage;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(" itemName : ").append(this.itemName)
				.append(" totalAmount : ").append(this.totalAmount)
				.append(" percentage : ").append(this.percentage).toString();
	}
}
