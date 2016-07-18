package com.salest.salestemperature.v3.web.request.model;

import java.util.ArrayList;
import java.util.List;

public class SalesVolumeResponse {

	private String date;
	private List<ItemDetail> itemList;
	
	public SalesVolumeResponse(String date){
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
		private int totalSalesCount;
		private long totalSalesAmount;
		
		public ItemDetail(String itemName, int totalSalesCount, long totalSalesAmount){
			this.itemName = itemName;
			this.totalSalesCount = totalSalesCount;
			this.totalSalesAmount = totalSalesAmount;
		}
		
		public String getItemName(){
			return this.itemName;
		}
		public int getTotalSalesCount(){
			return this.totalSalesCount;
		}
		public long getTotalSalesAmount(){
			return this.totalSalesAmount;
		}
		public void setItemName(String itemName){
			this.itemName = itemName;
		}
		public void setTotalSalesCount(int totalSalesCount){
			this.totalSalesCount = totalSalesCount;
		}
		public void setTotalSalesAmount(long totalSalesAmount){
			this.totalSalesAmount = totalSalesAmount;
		}
		
		@Override
		public String toString() {
			
			return new StringBuffer(" itemName : ").append(this.itemName)
					.append(", totalSalesCount : ").append(this.totalSalesCount)
					.append(", totalSalesAmount : ").append(this.totalSalesAmount).toString();

		}
		
	}
}
