package com.salest.salestemperature.v3.service;

import org.apache.log4j.Logger;

import com.salest.salestemperature.v3.dao.CategoryDao;

public class SalesLogService {

	private Logger logger = Logger.getLogger(SalesLogService.class);
	
	public void writeSalesLog(String product_name, String transaction_date, String transaction_time){
		
		//2014-12-01-09,18:57:35,106,1,4500
		String message = String.format("%s,%s,100,1,10000", transaction_date, transaction_time);
		logger.info(message);
		
	}
	
}
