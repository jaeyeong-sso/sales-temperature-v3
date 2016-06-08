package com.salest.salestemperature.v3.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.api.model.ProductDetailResponse;
import com.salest.salestemperature.v3.api.model.SalesVolumeResponse;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.model.Category;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.SalesVolume;
import com.salest.salestemperature.v3.service.AnalyzeSalesVolumeService;
import com.salest.salestemperature.v3.service.ProductsInfoService;

@Component
@Path("/salesvolume")
public class SalesvolumeResource {

	@Autowired
	AnalyzeSalesVolumeService analyzeSalesVolumeService;
	
	@GET
	@Path("/annual_sales_vol_sum/{queryYear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAnnualSalesVolumeSummary(@PathParam("queryYear") String queryYear) {
		
		AnnualSalesVolumeSummary annualSalesVolume = analyzeSalesVolumeService.getAnnualSalesVolume(queryYear);
		
		if(annualSalesVolume!=null){
			return Response.status(200).entity(annualSalesVolume).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/monthly_sales_vol/{queryYear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMontlySalesVolume(@PathParam("queryYear") String queryYear) {
		
		List<SalesVolume> fullYearSalesVolumes = new ArrayList<SalesVolume>();
		List<SalesVolume> monthlySalesVolumes = analyzeSalesVolumeService.listingMonthlySalesVolume(queryYear);
		
		if(monthlySalesVolumes!=null){
			
			for(int i=1; i<=12; i++){
				
				final String targetMonthDate = String.format("%s-%02d", queryYear, i);
				
				SalesVolume targetCateItem = (SalesVolume) CollectionUtils.find(monthlySalesVolumes, new org.apache.commons.collections.Predicate() {
			        public boolean evaluate(Object monthSalesVolume) {
			            return ((SalesVolume)monthSalesVolume).getDate().equals(targetMonthDate) ;
			        }
			    });
				
				if(targetCateItem!=null){
					fullYearSalesVolumes.add(targetCateItem);
				} else {
					fullYearSalesVolumes.add(new SalesVolume.SalesVolumeBuilder()
							.withDate(targetMonthDate).withTotalSalesAmount(0L).withTotalSalesCount(0).build());
				}
			}
			return Response.status(200).entity(fullYearSalesVolumes).build();
			
		} else {
			return Response.status(500).build();
		}
	}

	@GET
	@Path("/products_sales_vol/{queryYear}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoriesSalesVolume(@PathParam("queryYear") String queryYear) {
		
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getMonthlySalesVolumeByCategories(queryYear);

		List<SalesVolumeResponse> responseItemList = new ArrayList<SalesVolumeResponse>();

		for(int monthIdx=1; monthIdx<=12; monthIdx++){
			String dateKey = String.format("%s-%02d", queryYear,monthIdx);
			
			SalesVolumeResponse mothlySalesVolume = new SalesVolumeResponse(dateKey);
			
			for(SalesVolume itemSalseVolume : itemSalesVolumes){
				if(itemSalseVolume.getDate().equals(dateKey)){
					mothlySalesVolume.addItemList(
							new SalesVolumeResponse.ItemDetail(itemSalseVolume.getOptItemName(), itemSalseVolume.getTotalSalesCount(), itemSalseVolume.getTotalSalesAmount()));
				}
			}
			
			responseItemList.add(mothlySalesVolume);
		}
		
		if(itemSalesVolumes!=null){
			return Response.status(200).entity(responseItemList).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/products_sales_vol/{queryYear}/{queryCategory}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsSalesVolume(@PathParam("queryYear") String queryYear, @PathParam("queryCategory") String queryCategory) {
		
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getMonthlySalesVolumeByProducts(queryYear, queryCategory);
		
		List<SalesVolumeResponse> responseItemList = new ArrayList<SalesVolumeResponse>();

		for(int monthIdx=1; monthIdx<=12; monthIdx++){
			String dateKey = String.format("%s-%02d", queryYear,monthIdx);
			
			SalesVolumeResponse mothlySalesVolume = new SalesVolumeResponse(dateKey);
			
			for(SalesVolume itemSalseVolume : itemSalesVolumes){
				if(itemSalseVolume.getDate().equals(dateKey)){
					mothlySalesVolume.addItemList(
							new SalesVolumeResponse.ItemDetail(itemSalseVolume.getOptItemName(), itemSalseVolume.getTotalSalesCount(), itemSalseVolume.getTotalSalesAmount()));
				}
			}
			
			responseItemList.add(mothlySalesVolume);
		}
		
		if(itemSalesVolumes!=null){
			return Response.status(200).entity(responseItemList).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	
	@GET
	@Path("/timebase_sales_vol/{queryYear}/{queryMonth}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimeBaseSalesVolume(@PathParam("queryYear") String queryYear, @PathParam("queryMonth") String queryMonth) {
		
		String dateYearMonthKey = String.format("%s-%s", queryYear,queryMonth);
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getTimebaseSalesVolumeOfMonth(dateYearMonthKey);
		
		SalesVolumeResponse responseItems = new SalesVolumeResponse(dateYearMonthKey);
		
		for(SalesVolume salesVolume : itemSalesVolumes){
			responseItems.addItemList(
					new SalesVolumeResponse.ItemDetail(salesVolume.getOptItemName(), salesVolume.getTotalSalesCount(), salesVolume.getTotalSalesAmount()));
		}
		
		if(itemSalesVolumes!=null){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/dayofweek_sales_vol/{queryYear}/{queryMonth}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDayOfWeekSalesVolume(@PathParam("queryYear") String queryYear, @PathParam("queryMonth") String queryMonth) {
		
		String dateYearMonthKey = String.format("%s-%s", queryYear,queryMonth);
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getDayOfWeekSalesVolumeOfMonth(dateYearMonthKey);
		
		SalesVolumeResponse responseItems = new SalesVolumeResponse(dateYearMonthKey);
		
		for(SalesVolume salesVolume : itemSalesVolumes){
			responseItems.addItemList(
					new SalesVolumeResponse.ItemDetail(salesVolume.getOptItemName(), salesVolume.getTotalSalesCount(), salesVolume.getTotalSalesAmount()));
		}

		if(itemSalesVolumes!=null){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	/*
	@GET
	@Path("/get_products_category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductCategoriesNames() {
		
		List<String> responseItems = productsInfoService.getProductCategoriesInfo();

		if(responseItems.size()>0){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/get_products_items/{categoryName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPopularProductItemsNames(@PathParam("categoryName") String categoryName) {
		
		List<Product> products = productsInfoService.getProductItemsDetails(categoryName);
		List<ProductDetailResponse> responseItems = new ArrayList<ProductDetailResponse>();
		
		for(Product product : products){
			responseItems.add(new ProductDetailResponse(product.getId(), product.getName(),product.getPrice()));
		}
		
		if(responseItems.size()>0){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}
	}
	*/
}
