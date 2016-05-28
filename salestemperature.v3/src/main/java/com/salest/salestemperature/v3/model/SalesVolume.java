package com.salest.salestemperature.v3.model;

public class SalesVolume {

	public static final String DATE_TOTAL = "DATE_TOTAL";
	
	private String date; 
	private int totalSalesCount;
	private long totalSalesAmount;
	
	private SalesVolume(SalesVolumeBuilder builder){
		this.date = builder.date;
		this.totalSalesAmount = builder.totalSalesAmount;
		this.totalSalesCount = builder.totalSalesCount;
	}
	
	public String getDate(){
		return this.date;
	}
	public int getTotalSalesCount(){
		return this.totalSalesCount;
	}
	public long getTotalSalesAmount(){
		return this.totalSalesAmount;
	}
	
	public static class SalesVolumeBuilder {
		
		private String date; 
		private int totalSalesCount;
		private long totalSalesAmount;
		
		public SalesVolumeBuilder(){
			this.date = null;
			this.totalSalesCount = 0;
			this.totalSalesAmount = 0;
		}
		
		public SalesVolume build() {
            return new SalesVolume(this);
        }
		
		public SalesVolumeBuilder withDate(String date){
			this.date = date;
			return this;
		}
		
		public SalesVolumeBuilder withTotalSalesCount(int totalSalesCount){
			this.totalSalesCount = totalSalesCount;
			return this;
		}
		
		public SalesVolumeBuilder withTotalSalesAmount(long totalSalesAmount){
			this.totalSalesAmount = totalSalesAmount;
			return this;
		}
	}
}
