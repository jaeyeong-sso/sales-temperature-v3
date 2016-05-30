package com.salest.salestemperature.v3.api.model;

public class SalesVolume {

	private String date; 
	private int totalSalesCount;
	private long totalSalesAmount;
	
	public SalesVolume(){}
	
	public SalesVolume(
			String date, int totalSalesCount, long totalSalesAmount){
		this.date = date;
		this.totalSalesCount = totalSalesCount;
		this.totalSalesAmount = totalSalesAmount;
	}
	
	public String getDate(){
		return this.date;
	}
	public long getTotalSalesAmount(){
		return this.totalSalesAmount;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	public void setTotalSalesCount(int totalSalesCount){
		this.totalSalesCount = totalSalesCount;
	}
	public void setTotalSalesAmount(long totalSalesAmount){
		this.totalSalesAmount = totalSalesAmount;
	}

	
	@Override
	public String toString() {
		
		return new StringBuffer("date").append(this.date).append(" totalSalesCount : ").append(this.totalSalesCount)
				.append(" totalSalesAmount : ").append(this.totalSalesAmount).toString();

	}
}
