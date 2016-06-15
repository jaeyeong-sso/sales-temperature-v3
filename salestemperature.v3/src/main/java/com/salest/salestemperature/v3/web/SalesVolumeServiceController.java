package com.salest.salestemperature.v3.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;
import com.salest.salestemperature.v3.api.model.SalesVolumeResponse;
import com.salest.salestemperature.v3.model.SalesVolume;
import com.salest.salestemperature.v3.service.AnalyzeSalesVolumeService;

@Controller
@RequestMapping(value="/salesvolume")
public class SalesVolumeServiceController {

	@Autowired
	AnalyzeSalesVolumeService analyzeSalesVolumeService;
	
	@RequestMapping(value="/annual_sales_vol_sum/{queryYear}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAnnualSalesVolumeSummary(@PathVariable String queryYear){
		AnnualSalesVolumeSummary annualSalesVolume = analyzeSalesVolumeService.getAnnualSalesVolume(queryYear);
		if( annualSalesVolume!=null ){
			return new ResponseEntity<AnnualSalesVolumeSummary>(annualSalesVolume, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/monthly_sales_vol/{queryYear}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMontlySalesVolume(@PathVariable String queryYear){
		
		List<SalesVolume> fullYearSalesVolumes = new ArrayList<SalesVolume>();
		List<SalesVolume> monthlySalesVolumes = analyzeSalesVolumeService.listingMonthlySalesVolume(queryYear);
		
		if(monthlySalesVolumes!=null){
			
			for(int i=1; i<=12; i++){
				
				final String targetMonthDate = String.format("%s-%02d", queryYear, i);
				
				SalesVolume targetCateItem = (SalesVolume) CollectionUtils.find(monthlySalesVolumes, new org.apache.commons.collections.Predicate() {
			        public boolean evaluate(Object monthSalesVolume) {
			            return ((SalesVolume)monthSalesVolume).getDate().equals(targetMonthDate);
			        }
			    });
				
				if(targetCateItem!=null){
					fullYearSalesVolumes.add(targetCateItem);
				} else {
					fullYearSalesVolumes.add(new SalesVolume.SalesVolumeBuilder()
							.withDate(targetMonthDate).withTotalSalesAmount(0L).withTotalSalesCount(0).build());
				}
			}
			
			return new ResponseEntity<List<SalesVolume>>(fullYearSalesVolumes, HttpStatus.OK);
			
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value="/products_sales_vol/{queryYear}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCategoriesSalesVolume(@PathVariable String queryYear){
		
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
		
		if(itemSalesVolumes.size()>0 && responseItemList.size()>0){
			return new ResponseEntity<List<SalesVolumeResponse>>(responseItemList, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(value="/products_sales_vol/{queryYear}/{queryCategory}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProductsSalesVolume(@PathVariable String queryYear, @PathVariable String queryCategory){
		
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
		
		if(itemSalesVolumes.size()>0 && responseItemList.size()>0){
			return new ResponseEntity<List<SalesVolumeResponse>>(responseItemList, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/timebase_sales_vol/{queryYear}/{queryMonth}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTimeBaseSalesVolume(@PathVariable String queryYear, @PathVariable String queryMonth){
		
		String dateYearMonthKey = String.format("%s-%s", queryYear,queryMonth);
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getTimebaseSalesVolumeOfMonth(dateYearMonthKey);
		
		SalesVolumeResponse responseItems = new SalesVolumeResponse(dateYearMonthKey);
		
		for(SalesVolume salesVolume : itemSalesVolumes){
			responseItems.addItemList(
					new SalesVolumeResponse.ItemDetail(salesVolume.getOptItemName(), salesVolume.getTotalSalesCount(), salesVolume.getTotalSalesAmount()));
		}
		
		if(itemSalesVolumes.size()>0){
			return new ResponseEntity<SalesVolumeResponse>(responseItems, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value="/dayofweek_sales_vol/{queryYear}/{queryMonth}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDayOfWeekSalesVolume(@PathVariable String queryYear, @PathVariable String queryMonth){
		
		String dateYearMonthKey = String.format("%s-%s", queryYear,queryMonth);
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getDayOfWeekSalesVolumeOfMonth(dateYearMonthKey);
		
		SalesVolumeResponse responseItems = new SalesVolumeResponse(dateYearMonthKey);
		
		for(SalesVolume salesVolume : itemSalesVolumes){
			responseItems.addItemList(
					new SalesVolumeResponse.ItemDetail(salesVolume.getOptItemName(), salesVolume.getTotalSalesCount(), salesVolume.getTotalSalesAmount()));
		}

		if(itemSalesVolumes.size()>0){
			return new ResponseEntity<SalesVolumeResponse>(responseItems, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
