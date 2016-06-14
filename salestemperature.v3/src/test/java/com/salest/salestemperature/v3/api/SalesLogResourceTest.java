package com.salest.salestemperature.v3.api;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class SalesLogResourceTest  extends JerseyTest {
	
    public SalesLogResourceTest() throws Exception {
		super();
    }
    
    protected Application configure() {
        return new ResourceConfig(SalesLogResource.class).property("contextConfigLocation", "classpath:/config/applicationContext.xml");  
    }
    
   //@Test
    public void write_saleslog(){
    	
    	Map<String, String> reqParam = new HashMap<String, String>();
    	reqParam.put("product_name", "PRODUCT-NAME");
    	reqParam.put("tr_date", "2016-06-08");
    	reqParam.put("tr_time", "18:37:00");
        
    	Response response = target("saleslog").request().post(Entity.json(reqParam), Response.class);
    	assertTrue(response.getStatus() == 200);
    }
}
