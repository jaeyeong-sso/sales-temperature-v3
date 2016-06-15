package com.salest.salestemperature.v3.web;

import java.util.List;
import java.util.Map;

import javax.inject.Provider;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.service.SalesLogService;

@Controller
@RequestMapping(value="/saleslog")
public class SalesLogServiceController {

   	@Autowired
   	SalesLogService salesLogService;
   	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> writeSalesLog(
			@RequestParam(value="product_name", required=true) String reqProductName,
			@RequestParam(value="tr_date", required=true) String reqTrDate,
			@RequestParam(value="tr_time", required=true) String reqTrTime ) {
		
		if(salesLogService.writeSalesLog(reqProductName, reqTrDate, reqTrTime)){
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
