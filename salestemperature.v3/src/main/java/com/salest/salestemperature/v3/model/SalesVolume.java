package com.salest.salestemperature.v3.model;

public class SalesVolume {

	public static final String DEFAULT_DATE_FIELD_VALUE = "DATE_TOTAL";
	public static final String DEFAULT_OPT_ITEM_NAME_VALUE = "ALL";
	
	private String date;
	private String optItemName;
	private int totalSalesCount;
	private long totalSalesAmount;
	
	private SalesVolume(SalesVolumeBuilder builder){
		this.date = builder.date;
		this.optItemName = builder.optItemName;
		this.totalSalesAmount = builder.totalSalesAmount;
		this.totalSalesCount = builder.totalSalesCount;
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
	
	public static class SalesVolumeBuilder {
		
		private String date;
		private String optItemName;
		private int totalSalesCount;
		private long totalSalesAmount;
		
		public SalesVolumeBuilder(){
			this.date = DEFAULT_DATE_FIELD_VALUE;
			this.optItemName = DEFAULT_OPT_ITEM_NAME_VALUE;
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
		
		public SalesVolumeBuilder withOptItemName(String optItemName){
			this.optItemName = optItemName;
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
