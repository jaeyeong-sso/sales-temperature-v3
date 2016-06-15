package com.salest.salestemperature.v3.service;

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
			//2014-12-01-09,18:57:35,106,1,4500
			String message = String.format("%s,%s,%s,1,%s", 
					transaction_date, transaction_time,targetProduct.getId(), targetProduct.getPrice());
			
			System.out.println("[writeSalesLog] : " + message);
			
			//logger.info(message);
			return true;
		} else {
			return false;
		}
		
	}
	
}