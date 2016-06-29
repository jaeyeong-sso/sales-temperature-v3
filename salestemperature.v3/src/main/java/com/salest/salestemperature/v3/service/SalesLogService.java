package com.salest.salestemperature.v3.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.model.Product;

public class SalesLogService {

	@Autowired
	Provider<ProductInfoDtoService> productInfoDtoServiceProvider;
	private ProductInfoDtoService productInfoDtoService;
	
	private RedisCacheService redisCacheService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
	public void setRedisCacheService(RedisCacheService redisCacheService){
		this.redisCacheService = redisCacheService;
	}
	
	private Logger logger = Logger.getLogger(SalesLogService.class);
	
	public boolean writeSalesLog(final String product_name, String transaction_date, String transaction_time){
		
		this.productInfoDtoService = productInfoDtoServiceProvider.get();
		List<Product> products = productInfoDtoService.getProducts();
		
		Product targetProduct = (Product)CollectionUtils.find(products, new org.apache.commons.collections.Predicate() {
	        public boolean evaluate(Object product) {
	            return ((Product)product).getName().equals(product_name);
	        }
	    });
		
		if(targetProduct!=null){
			
			String strDateYMD = simpleDateFormat.format(new Date());
			
			long salesCounter = redisCacheService.createOrIncrCounterValue(
					RedisCacheService.TOTAL_SALES_COUNTER_OF_DAY + strDateYMD, 1L);
			
			//2014-12-01-09,18:57:35,106,1,4500
			String message = String.format("%s-%02d,%s,%s,%d,%d", 
					transaction_date, salesCounter, transaction_time, targetProduct.getId(), 1, targetProduct.getPrice());
			
			System.out.println("[writeSalesLog] : " + message);
			
			logger.info(message);
			
			return true;
		} else {
			return false;
		}
		
	}
	
}
