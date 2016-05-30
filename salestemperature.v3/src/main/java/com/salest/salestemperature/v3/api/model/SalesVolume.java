package com.salest.salestemperature.v3.api.model;

public class SalesVolume {

	public static final String DEFAULT_DATE_FIELD_VALUE = "DATE_TOTAL";
	public static final String DEFAULT_OPT_ITEM_NAME_VALUE = "ALL";
	
	private String date; 
	private String optItemName;
	private int totalSalesCount;
	private long totalSalesAmount;
	
	public SalesVolume(){
		this.date = DEFAULT_DATE_FIELD_VALUE;
		this.optItemName = DEFAULT_OPT_ITEM_NAME_VALUE;
		this.totalSalesCount = 0;
		this.totalSalesAmount = 0L;
	}
	
	public SalesVolume(String date, String optItemName, int totalSalesCount, long totalSalesAmount){
		this.date = date;
		this.optItemName = optItemName;
		this.totalSalesCount = totalSalesCount;
		this.totalSalesAmount = totalSalesAmount;
	}
	
	public String getDate(){
		return this.date;
	}
	public String getOptItemName(){
		return this.optItemName;
	}
	public int getTotalSalesCount(){
		return this.totalSalesCount;
	}
	public long getTotalSalesAmount(){
		return this.totalSalesAmount;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	public void setOptItemName(String optItemName){
		this.optItemName = optItemName;
	}
	public void setTotalSalesCount(int totalSalesCount){
		this.totalSalesCount = totalSalesCount;
	}
	public void setTotalSalesAmount(long totalSalesAmount){
		this.totalSalesAmount = totalSalesAmount;
	}

	
	@Override
	public String toString() {
		
		return new StringBuffer("date : ").append(this.date)
				.append(" optItemName : ").append(this.optItemName)
				.append(" totalSalesCount : ").append(this.totalSalesCount)
				.append(" totalSalesAmount : ").append(this.totalSalesAmount).toString();

	}
}
