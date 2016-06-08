package com.salest.salestemperature.v3.api;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyApplication extends ResourceConfig {
	
	public JerseyApplication(){
		register(RequestContextFilter.class);
		register(MultiPartFeature.class);
		register(SalesvolumeResource.class);
		register(ProductInfoResource.class);
	}
}
