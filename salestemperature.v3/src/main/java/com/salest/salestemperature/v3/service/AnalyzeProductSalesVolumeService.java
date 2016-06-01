package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.ProductSalesVolume;
import com.salest.salestemperature.v3.model.SalesVolume;


public class AnalyzeProductSalesVolumeService {
	
	private CategoryDao categoryDao;
	private SalesVolumeDao salesVolumeDao;
	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}
	
	public void setSalesVolumeDao(SalesVolumeDao salesVolumeDao){
		this.salesVolumeDao = salesVolumeDao;
	}

	
	public AnnualSalesVolumeSummary getAnnualSalesVolume(String queryYear){
		
		Map<String,Object> mapAnnualSalesVolume = salesVolumeDao.getAnnualSalesVolume(queryYear);
		
		long totalNumOfDate = (Long) mapAnnualSalesVolume.get("total_num_of_date");
		long totalNumOfProduct = (Long) mapAnnualSalesVolume.get("total_num_of_product");
		long totalAmount = (Long) mapAnnualSalesVolume.get("total_amount");
		
		AnnualSalesVolumeSummary annualSalesVolumeSummary = new AnnualSalesVolumeSummary();
		
		annualSalesVolumeSummary.setTotalSalesCount((int) totalNumOfProduct);
		annualSalesVolumeSummary.setTotalSalesAmount(totalAmount);
		annualSalesVolumeSummary.setAvrgSalesCount((int) (totalNumOfProduct/totalNumOfDate));
		annualSalesVolumeSummary.setAvrgSalesAmount(totalAmount/totalNumOfDate);
		
		return annualSalesVolumeSummary;
	}
	
	public List<SalesVolume> listingMonthlySalesVolume(String queryYear){
		return salesVolumeDao.listingMonthlySalesVolume(queryYear);
	}

	public List<SalesVolume> getMonthlySalesVolumeByCategories(String queryYear){
		
		List<Category> productCategories = categoryDao.listingAllProductCategory();
		List<SalesVolume> categorySalesVolumes = salesVolumeDao.listingCategoriesMonthlySalesVolume(queryYear);
		
		List<String> categoryNames = new ArrayList<String>();
		for(Category category : productCategories){
			categoryNames.add(category.getName());
		}

		for(int monthIdx=1; monthIdx<=12; monthIdx++){
			
			final String dateKey = String.format("%s-%02d", queryYear,monthIdx);
			
			SalesVolume findObject = (SalesVolume)CollectionUtils.find(categorySalesVolumes, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object categorySalesVolume) {
		            return ((SalesVolume)categorySalesVolume).getDate().equals(dateKey);
		        }
		    });
			
			if(findObject==null){
				for(String categoryName : categoryNames){
					categorySalesVolumes.add(
							new SalesVolume.SalesVolumeBuilder()
							.withDate(dateKey)
							.withOptItemName(categoryName)
							.withTotalSalesCount(0)
							.withTotalSalesAmount(0L).build());
				}
			}
		}
		
		Collections.sort(categorySalesVolumes, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getDate().compareTo(second.getDate());
			}
		});
		
		return categorySalesVolumes;
	}
	
	public List<SalesVolume> getMonthlySalesVolumeByProducts(String queryYear, final String categoryName){
	
		List<SalesVolume> listResult = salesVolumeDao.listingProductsMonthlySalesVolume(queryYear, categoryName);
		Category category = categoryDao.listingMajorProductsOfCategory(categoryName);

		Set<String> productNameKeySet = category.getProductsMap().keySet();
				
		List<SalesVolume> finalResultList = new ArrayList<SalesVolume>(); 
			
		Map<String,Integer> etcItemsToatalCountMap = new HashMap<String,Integer>();
		Map<String,Long> etcItemsToatalAmountMap = new HashMap<String,Long>();
			
		for(SalesVolume itemSalesVolume : listResult){
				if( productNameKeySet.contains(itemSalesVolume.getOptItemName())){
					finalResultList.add(itemSalesVolume);
				} else {
					String dateKey = itemSalesVolume.getDate();
					if(etcItemsToatalCountMap.containsKey(dateKey)){
						etcItemsToatalCountMap.put(dateKey, etcItemsToatalCountMap.get(dateKey) + itemSalesVolume.getTotalSalesCount());
						etcItemsToatalAmountMap.put(dateKey, etcItemsToatalAmountMap.get(dateKey) + itemSalesVolume.getTotalSalesAmount());
					} else {
						etcItemsToatalCountMap.put(dateKey, itemSalesVolume.getTotalSalesCount());
						etcItemsToatalAmountMap.put(dateKey, itemSalesVolume.getTotalSalesAmount());
					}
				}
		}
			
		for(String key : etcItemsToatalCountMap.keySet()){
				finalResultList.add(new SalesVolume.SalesVolumeBuilder()
						.withDate(key)
						.withOptItemName("ETC.")
						.withTotalSalesCount(etcItemsToatalCountMap.get(key))
						.withTotalSalesAmount(etcItemsToatalAmountMap.get(key))
						.build());
		}
			

		Collections.sort(finalResultList, new Comparator<SalesVolume>() {
			public int compare(SalesVolume first, SalesVolume second) {
				return first.getDate().compareTo(second.getDate());
			}
		});

		/*
		for(SalesVolume salesVolumItem : finalResultList){
			System.out.println(salesVolumItem.toString());
		}
		*/
		
		return finalResultList;
	}

}
