package com.salest.salestemperature.v3.api;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ProductInfoResourceTest extends JerseyTest {
	
    public ProductInfoResourceTest() throws Exception {
		super();
    }
    
    protected Application configure() {
        return new ResourceConfig(ProductInfoResource.class).property("contextConfigLocation", "classpath:/config/applicationContext.xml");  
    }
    
    //@Test
    public void get_products_category_test(){
    	Response response = target("productinfo/get_products_category").request().get(Response.class);
    	assertTrue(response.getStatus() == 200);
    }
    
    //@Test
    public void get_products_items_test(){
    	Response response = target("productinfo/get_products_items/Ä¿ÇÇ").request().get(Response.class);
    	assertTrue(response.getStatus() == 200);
    }
}
