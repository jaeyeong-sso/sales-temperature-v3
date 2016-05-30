package com.salest.salestemperature.v3.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.dao.CategoryDao;
import com.salest.salestemperature.v3.dao.ProductSalesVolumeDao;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.ProductSalesVolume;
import com.salest.salestemperature.v3.model.SalesVolume;


public class AnalyzeProductSalesVolumeService {
	
	private CategoryDao categoryDao;
	private ProductSalesVolumeDao productSalesVolumeDao;
	private SalesVolumeDao salesVolumeDao;
	
	public void setCategoryDao(CategoryDao categoryDao){
		this.categoryDao = categoryDao;
	}
	
	public void setProductSalesVolumeDao(ProductSalesVolumeDao productSalesVolumeDao){
		this.productSalesVolumeDao = productSalesVolumeDao;
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
	
	private void addNewProductToExistCategories(List<Category> categories, final Product product){
		
		Category targetCateItem = (Category) CollectionUtils.find(categories, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object cateItem) {
	            return ((Category)cateItem).getName().equals(product.getCategory().getName()) ;
	        }
	    });
		
		if(targetCateItem == null){
			categories.add(new Category.CategoryBuilder().withName(product.getCategory().getName()).withAddProduct(product).build());
			
		} else {
			if(targetCateItem.getProducts().size()<10){
				targetCateItem.getProducts().add(product);
			}
		}
	}
	
	public List<Category> getMostPopularProducts(){

		List<Category> popularProducts = new ArrayList<Category>();
		
		List<Category> categories = categoryDao.listingProductCategory();
		List<ProductSalesVolume> productsSalesVolume = productSalesVolumeDao.listingProductsBySalesVolume();
		
		for(final ProductSalesVolume productSalesVolume : productsSalesVolume){
			
			Category targetCategory = (Category) CollectionUtils.find(categories, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object category) {
		            return ((Category)category).getName().equals(productSalesVolume.getCateName());
		        }
		    });
			
			Product targetProduct = (Product) CollectionUtils.find(targetCategory.getProducts(), new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object productItem) {
		            return ((Product)productItem).getId().equals(productSalesVolume.getId());
		        }
		    });
			
			this.addNewProductToExistCategories(popularProducts, targetProduct);
		
			for(Category category : popularProducts){
				System.out.println("[category] : " + category.getName());
				
				for(Product product : category.getProducts()){
					System.out.println("	[Product] : " + product.getName() );
				}
			}
		}
		
		return popularProducts;
	}
	
	public List<SalesVolume> getMonthlySalesVolumeByCategories(String queryYear){
		return salesVolumeDao.listingCategoriesMonthlySalesVolume(queryYear);
	}
	
	public List<SalesVolume> getMonthlySalesVolumeByProducts(String queryYear, String categoryName){
		return salesVolumeDao.listingProductsMonthlySalesVolume(queryYear, categoryName);
	}

}
