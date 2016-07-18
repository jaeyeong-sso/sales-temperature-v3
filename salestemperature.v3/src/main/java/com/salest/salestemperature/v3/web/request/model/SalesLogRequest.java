package com.salest.salestemperature.v3.web.request.model;

public class SalesLogRequest {

	private String productName;
	private String trDate;
	private String trTime;
	
	public SalesLogRequest(){}
	
	public String getProductName(){
		return this.productName;
	}
	public String getTrDate(){
		return this.trDate;
	}
	public String getTrTime(){
		return this.trTime;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	public void setTrDate(String trDate){
		this.trDate = trDate;
	}
	public void setTrTime(String trTime){
		this.trTime = trTime;
	}
	
	public String toString(){
		return String.format("productName:%s, trDate:%s, trTime:%s", this.productName, this.trDate, this.trTime);
	}

}
