package com.salest.salestemperature.v3.web.request.model;

import java.util.List;

public class TodaysStreamDataResponse {
	
	private List<SalesAmountProportionResponse> categories;
	private List<SalesAmountProportionResponse> products;
	
	private SalesVolumeDiffResponse salesVolumeDiff;
	
	public TodaysStreamDataResponse(){};
	
	public void setCategories(List<SalesAmountProportionResponse> categories){
		this.categories = categories;
	}
	
	public void setProducts(List<SalesAmountProportionResponse> products){
		this.products = products;
	}
	
	public void setSalesVolumeDiff(SalesVolumeDiffResponse salesVolumeDiff){
		this.salesVolumeDiff = salesVolumeDiff;
	}
	
	public List<SalesAmountProportionResponse> getCategories(){
		return this.categories;
	}
	
	public List<SalesAmountProportionResponse> getProducts(){
		return this.products;
	}
	
	public SalesVolumeDiffResponse getSalesVolumeDiff(){
		return this.salesVolumeDiff;
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	
	/*
	public static class SalesAmountProportionResponse {

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

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class SalesVolumeDiffResponse {
		
		private String date;
		private List<ItemDetail> itemList;
		
		public SalesVolumeDiffResponse(String date){
			this.date = date;
			this.itemList = new ArrayList<ItemDetail>();
		}
		public String getDate(){
			return this.date;
		}
		public List<ItemDetail> getItemList(){
			return this.itemList;
		}
		public void setDate(String date){
			this.date = date;
		}
		public void setItemList(List<ItemDetail> itemList){
			this.itemList = itemList;
		}
		public void addItemList(ItemDetail item){
			this.itemList.add(item);
		}

		public static class ItemDetail {
			
			private String itemName;
			private long pastTotalSalesAmount;
			private long nowTotalSalesAmount;
			
			public ItemDetail(String itemName, long pastTotalSalesAmount, long nowTotalSalesAmount){
				this.itemName = itemName;
				this.pastTotalSalesAmount = pastTotalSalesAmount;
				this.nowTotalSalesAmount = nowTotalSalesAmount;
			}
			
			public String getItemName(){
				return this.itemName;
			}
			public long getPastTotalSalesAmount(){
				return this.pastTotalSalesAmount;
			}
			public long getNowTotalSalesAmount(){
				return this.nowTotalSalesAmount;
			}
			public void setItemName(String itemName){
				this.itemName = itemName;
			}
			public void setPastTotalSalesAmount(long pastTotalSalesAmount){
				this.pastTotalSalesAmount = pastTotalSalesAmount;
			}
			public void setNowTotalSalesAmount(long nowTotalSalesAmount){
				this.nowTotalSalesAmount = nowTotalSalesAmount;
			}
			
			@Override
			public String toString() {
				
				return new StringBuffer(" itemName : ").append(this.itemName)
						.append(", pastTotalSalesAmount : ").append(this.pastTotalSalesAmount)
						.append(", nowTotalSalesAmount : ").append(this.nowTotalSalesAmount).toString();

			}
			
		}
	}
	*/
}
