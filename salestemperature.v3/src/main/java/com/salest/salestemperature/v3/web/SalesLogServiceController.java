package com.salest.salestemperature.v3.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.service.SalesLogService;
import com.salest.salestemperature.v3.web.request.model.SalesLogRequest;

@Controller
@RequestMapping(value="/saleslog")
public class SalesLogServiceController {

   	@Autowired
   	SalesLogService salesLogService;
   	
	@RequestMapping(value="/write", method=RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public ResponseEntity<?> writeSalesLog(@RequestBody SalesLogRequest reqParamObj) {
		
		if(salesLogService.writeSalesLog(reqParamObj.getProductName(), reqParamObj.getTrDate(), reqParamObj.getTrTime())){
			return new ResponseEntity<String>("{\"result\":\"ok\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
