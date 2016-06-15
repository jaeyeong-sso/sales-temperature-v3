package com.salest.salestemperature.v3.api;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.service.SalesLogService;

@Component
@Path("/saleslog")
public class SalesLogResource {

   	@Autowired
   	SalesLogService salesLogService;
   	
   	final private String PARAM_KEY_PRODUCT_NAME = "product_name";
   	final private String PARAM_KEY_TR_DATE = "tr_date";
   	final private String PARAM_TR_TIME = "tr_time";
   	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response writeSalesLog(Map<String, String> reqParam) {
		
		String product_name = reqParam.get(PARAM_KEY_PRODUCT_NAME);
		String transaction_date = reqParam.get(PARAM_KEY_TR_DATE);
		String transaction_time = reqParam.get(PARAM_TR_TIME);
		
		salesLogService.writeSalesLog(product_name, transaction_date, transaction_time);
		
		return Response.status(200).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductInfoDto(){
		
		return Response.status(200).build();
	}
}
