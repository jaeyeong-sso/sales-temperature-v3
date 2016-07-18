package com.salest.salestemperature.v3.web.request.model;

import java.util.ArrayList;
import java.util.List;

public class SalesVolumeDiffResponse {
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
