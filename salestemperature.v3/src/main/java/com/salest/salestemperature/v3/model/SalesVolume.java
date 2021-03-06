package com.salest.salestemperature.v3.model;

import java.util.Comparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	
	public void setOptItemName(String optItemName){
		this.optItemName = optItemName;
	}
	
	@Override
	public String toString() {
		
		return new StringBuffer("date: ").append(this.date)
				.append(",  optItemName: ").append(this.optItemName)
				.append(",  totalSalesAmount: ").append(this.totalSalesAmount)
				.append(",  totalSalesCount: ").append(this.totalSalesCount).toString();

	}
	
	public String toJsonString() {
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("date", this.date);
		jsonObj.put("optItemName", this.optItemName);
		jsonObj.put("totalSalesCount", this.totalSalesCount);
		jsonObj.put("totalSalesAmount", this.totalSalesAmount);
		
		return jsonObj.toJSONString();
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
