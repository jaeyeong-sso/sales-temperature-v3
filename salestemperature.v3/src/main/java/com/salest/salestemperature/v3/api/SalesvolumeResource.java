package com.salest.salestemperature.v3.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/salesvolume")
public class SalesvolumeResource {

	@GET
	@Path("/monthly_sales_vol")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMontlySalesVolume() {
		return "getMontlySalesVolume";
	}
}
