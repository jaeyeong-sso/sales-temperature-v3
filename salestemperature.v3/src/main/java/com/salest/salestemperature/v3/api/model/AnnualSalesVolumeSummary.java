package com.salest.salestemperature.v3.api.model;

public class AnnualSalesVolumeSummary {

	private int totalSalesCount;
	private long totalSalesAmount;
	private int avrgSalesCount;
	private long avrgSalesAmount;
	
	public AnnualSalesVolumeSummary(){}
	
	public AnnualSalesVolumeSummary(
			int totalSalesCount, long totalSalesAmount, int avrgSalesCount, long avrgSalesAmount){
		this.totalSalesCount = totalSalesCount;
		this.totalSalesAmount = totalSalesAmount;
		this.avrgSalesCount = avrgSalesCount;
		this.avrgSalesAmount = avrgSalesAmount;
	}
	
	public int getTotalSalesCount(){
		return this.totalSalesCount;
	}
	public long getTotalSalesAmount(){
		return this.totalSalesAmount;
	}
	public int getAvrgSalesCount(){
		return this.avrgSalesCount;
	}
	public long getAvrgSalesAmount(){
		return this.avrgSalesAmount;
	}
	
	public void setTotalSalesCount(int totalSalesCount){
		this.totalSalesCount = totalSalesCount;
	}
	public void setTotalSalesAmount(long totalSalesAmount){
		this.totalSalesAmount = totalSalesAmount;
	}
	public void setAvrgSalesCount(int avrgSalesCount){
		this.avrgSalesCount = avrgSalesCount;
	}
	public void setAvrgSalesAmount(long avrgSalesAmount){
		this.avrgSalesAmount = avrgSalesAmount;
	}
	
	@Override
	public String toString() {
		
		return new StringBuffer(" totalSalesCount : ").append(this.totalSalesCount)
				.append(" totalSalesAmount : ").append(this.totalSalesAmount)
				.append(" avrgSalesAmount : ").append(this.avrgSalesAmount)
				.append( "avrgSalesAmount : ").append(this.avrgSalesAmount).toString();

	}
}
