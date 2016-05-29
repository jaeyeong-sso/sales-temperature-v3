package com.salest.salestemperature.v3.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;

@Path("/salesvolume")
public class SalesvolumeResource {

	@GET
	@Path("/monthly_sales_vol")
	@Produces(MediaType.APPLICATION_JSON)
	public AnnualSalesVolumeSummary getMontlySalesVolume() {
		
		AnnualSalesVolumeSummary response = new AnnualSalesVolumeSummary();
		
		response.setTotalSalesCount(1);
		response.setTotalSalesAmount(1000L);
		response.setAvrgSalesCount(2);
		response.setAvrgSalesAmount(2000L);
		
		String jsonResponse = response.toString();
		
		return response;
				//Response.status(200).entity(jsonResponse).build();
	}
}
