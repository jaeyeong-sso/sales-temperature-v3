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

public class SalesVolumeResourceTest extends JerseyTest {
	
	    public SalesVolumeResourceTest() throws Exception {
			super();
	    }

	    protected Application configure() {
	        return new ResourceConfig(SalesvolumeResource.class).property("contextConfigLocation", "classpath:/config/applicationContext.xml");  
	    }
	 
	    //@Test
	    public void monthly_sales_vol_test() {

	    	Response response = target("salesvolume/monthly_sales_vol/2014").request().get(Response.class);
	    	assertTrue(response.getStatus() == 200);
	    }
	    
	    //@Test
	    public void products_sales_vol_test(){
	  
	    	Response response = target("salesvolume/products_sales_vol/2014").request().get(Response.class);
	    	assertTrue(response.getStatus() == 200);
	    }
	    
	    @Test
	    public void products_sales_vol_WITH_category_test(){
	  
	    	Response response = target("salesvolume/products_sales_vol/2014/Ä¿ÇÇ").request().get(Response.class);
	    	assertTrue(response.getStatus() == 200);
	    }
	    
}
