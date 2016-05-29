package com.salest.salestemperature.v3.api;

import static org.junit.Assert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.salest.salestemperature.v3.api.model.AnnualSalesVolumeSummary;


public class SalesvolumeServiceTest extends JerseyTest {
	
	    public SalesvolumeServiceTest() throws Exception {
			super();
	    }

	    protected Application configure() {
	        return new ResourceConfig(SalesvolumeResource.class).property("contextConfigLocation", "classpath:/config/applicationContext.xml");  
	    }
	 
	    @Test
	    public void salesvolumeResourceTest() {
	    	//assertEquals("","");
	        final Response response = target("salesvolume/monthly_sales_vol").request().get(Response.class);
	        
	        assertNotNull(response);
	    }
}
