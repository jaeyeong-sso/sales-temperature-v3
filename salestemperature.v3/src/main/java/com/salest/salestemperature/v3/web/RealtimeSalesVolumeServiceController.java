package com.salest.salestemperature.v3.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.api.model.SalesAmountProportionResponse;
import com.salest.salestemperature.v3.api.model.SalesVolumeResponse;
import com.salest.salestemperature.v3.dao.SalesVolumeDao;
import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.model.SalesVolume;
import com.salest.salestemperature.v3.service.AnalyzeSalesVolumeService;
import com.salest.salestemperature.v3.service.RedisCacheService;
import com.salest.salestemperature.v3.service.TimeSeriesService;

@Controller
@RequestMapping(value="/realtimesalesvolume")
public class RealtimeSalesVolumeServiceController {

   	@Autowired
   	RedisCacheService redisCacheService;
   	
	@Autowired
	Provider<ProductInfoDtoService> productInfoDtoServiceProvider;
	private ProductInfoDtoService productInfoDtoService;
	
	@Autowired
	private AnalyzeSalesVolumeService analyzeSalesVolumeService;
	
	@Autowired
	private TimeSeriesService timeSeriesService;
	
	@RequestMapping(value="/{keyPrefix}/product_of/{keyTargetDate}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTotalAmountProductOf(
			@PathVariable String keyPrefix, @PathVariable String keyTargetDate){
		
		this.productInfoDtoService = productInfoDtoServiceProvider.get();
		List<Product> products = productInfoDtoService.getProducts();
		
		List<SalesAmountProportionResponse> responses = new ArrayList<SalesAmountProportionResponse>();
		
		Set<String> targetKeys = redisCacheService.readKeys(keyPrefix+":product_of:"+keyTargetDate+":*");
		
		long totalAmountOfAll = 0;
		
		for(String key : targetKeys){
			String[] keyFields = key.split(":");
			
			final String productCodeOfKey = keyFields[keyFields.length-1];
			long salesAmount = Long.parseLong(redisCacheService.readValueByKey(key));
			
			Product targetProduct = (Product)CollectionUtils.find(products, new org.apache.commons.collections.Predicate() {
		        public boolean evaluate(Object product) {
		            return ((Product)product).getId().equals(productCodeOfKey);
		        }
		    });

			totalAmountOfAll += salesAmount;
			responses.add(new SalesAmountProportionResponse(targetProduct.getName(), salesAmount));
		}
		
		for(SalesAmountProportionResponse response : responses){
			response.setPercentage( ((float)response.getTotalAmount()/totalAmountOfAll) * 100.0f);
			response.setPercentage( Math.round(response.getPercentage()*100)/100.0f);
		}
	
		return new ResponseEntity<List<SalesAmountProportionResponse>>(responses, HttpStatus.OK);
	}

	
	@RequestMapping(value="/{keyPrefix}/category_of/{keyTargetDate}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTotalAmountCategoryOf(
			@PathVariable String keyPrefix, @PathVariable String keyTargetDate){
		
		List<SalesAmountProportionResponse> responses = new ArrayList<SalesAmountProportionResponse>();
	
		Set<String> targetKeys = redisCacheService.readKeys(keyPrefix+":category_of:"+keyTargetDate+":*");
		long totalAmountOfAll = 0;
		
		for(String key : targetKeys){
			String[] keyFields = key.split(":");
			
			final String categoryNameOfKey = keyFields[keyFields.length-1];
			long salesAmount = Long.parseLong(redisCacheService.readValueByKey(key));
			totalAmountOfAll += salesAmount;
			
			responses.add(new SalesAmountProportionResponse(categoryNameOfKey, salesAmount));
		}
		
		for(SalesAmountProportionResponse response : responses){
			response.setPercentage( ((float)response.getTotalAmount()/totalAmountOfAll) * 100.0f);
			response.setPercentage( Math.round(response.getPercentage()*100)/100.0f);
		}
		
		return new ResponseEntity<List<SalesAmountProportionResponse>>(responses, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/timebaseSalesDiff/{queryDate}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTimebaseSalesDiff(@PathVariable String queryDate){

		String targetPastDate = timeSeriesService.getPastWeekMonthIdxDate(queryDate);
		List<SalesVolume> itemSalesVolumes = analyzeSalesVolumeService.getTimebaseSalesVolumeOfDate(targetPastDate);
		
		SalesVolumeResponse responseItems = new SalesVolumeResponse(targetPastDate);
		
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
