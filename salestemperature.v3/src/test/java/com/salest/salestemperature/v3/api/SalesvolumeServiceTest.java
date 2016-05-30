package com.salest.salestemperature.v3.api;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.salest.salestemperature.v3.api.model.*;

public class SalesvolumeServiceTest extends JerseyTest {
	
	    public SalesvolumeServiceTest() throws Exception {
			super();
	    }

	    protected Application configure() {
	        return new ResourceConfig(SalesvolumeResource.class).property("contextConfigLocation", "classpath:/config/applicationContext.xml");  
	    }
	 
	    //@Test
	    public void salesvolumeResourceTest() {
	    	
	    	Response response;
	        
	        //response = target("salesvolume/annual_sales_vol_sum/2014").request().get(Response.class);
	        //assertEquals("[annual_sales_vol_sum] : RESPONSE STATUS is not 200", response.getStatus(), 200);
	        
	        response = target("salesvolume/monthly_sales_vol/2014").request().get(Response.class);
	        assertEquals("[monthly_sales_vol] : RESPONSE STATUS is not 200", response.getStatus(), 200);
	        
	        List<SalesVolume> responseListObj = response.readEntity(new GenericType<List<SalesVolume>>(){});
	  
	        assertEquals("[monthly_sales_vol / 2014] : ", responseListObj.get(responseListObj.size()-1).getDate(), "2014-12");
	        
	    }
}
