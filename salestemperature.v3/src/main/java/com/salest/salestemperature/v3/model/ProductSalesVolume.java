package com.salest.salestemperature.v3.model;

public class ProductSalesVolume {
	
	private String id;
	private String cateName;
	private int totalSalesCount;
	private long totalSalesAmount;
	
	private ProductSalesVolume(ProductSalesVolumeBuilder builder){
		this.id = builder.id;
		this.cateName = builder.cateName;
		this.totalSalesCount = builder.totalSalesCount;
		this.totalSalesAmount = builder.totalSalesAmount;
	}
	
	public String getId(){
		return this.id;
	}
	public String getCateName(){
		return this.cateName;
	}
	public int getTotalSalesCount(){
		return this.totalSalesCount;
	}
	public long getTotalSalesAmount(){
		return this.totalSalesAmount;
	}
	
	public static class ProductSalesVolumeBuilder {
		
		private String id;
		private String cateName;
		private int totalSalesCount;
		private long totalSalesAmount;
		
		public ProductSalesVolumeBuilder(){
			this.id = null;
			this.cateName = null;
			this.totalSalesCount = 0;
			this.totalSalesAmount = 0L;
		}
		public ProductSalesVolume build(){
			return new ProductSalesVolume(this);
		}
		
		public ProductSalesVolumeBuilder withProductId(String id){
			this.id = id;
			return this;
		}
		public ProductSalesVolumeBuilder withCateName(String cateName){
			this.cateName = cateName;
			return this;
		}
		public ProductSalesVolumeBuilder withTotalSalesCount(int totalSalesCount){
			this.totalSalesCount = totalSalesCount;
			return this;
		}
		public ProductSalesVolumeBuilder withTotalSalesAmount(long totalSalesAmount){
			this.totalSalesAmount = totalSalesAmount;
			return this;
		}
	}
}