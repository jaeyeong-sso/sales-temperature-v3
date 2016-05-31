package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		return salesVolumeDao.listingCategoriesMonthlySalesVolume(queryYear);
	}
	
	public List<SalesVolume> getMonthlySalesVolumeByProducts(String queryYear, final String categoryName){
	
		
/*
		List<Category> popularProducts = getMostPopularProducts();
		Category targetCategory = (Category) CollectionUtils.find(popularProducts, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object category) {
	            return ((Category)category).getName().equals(categoryName);
	        }
	    });
		
		List<SalesVolume> monthlyProductsSalesVolume = salesVolumeDao.listingProductsMonthlySalesVolume(queryYear, categoryName);
		List<SalesVolume> resultProductsSalesVolume = new ArrayList<SalesVolume>();
*/		
		//List<Product> products = targetCategory.getProducts();
/*
		for(final SalesVolume salesVolumeItem : monthlyProductsSalesVolume){
			
			Product targetProduct = (Product) CollectionUtils.find(products, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object product) {
		        	return ((Product)product).getName().equals(salesVolumeItem.getOptItemName());
		        }
			});
			
			if(targetProduct!=null){
				
			}
		}
*/		
		return null;	//resultProductsSalesVolume;
	}

}
