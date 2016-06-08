package com.salest.salestemperature.v3.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salest.salestemperature.v3.api.model.ProductDetailResponse;
import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.service.ProductsInfoService;

@Component
@Path("/productinfo")
public class ProductInfoResource {
	
	@Autowired
	ProductsInfoService productsInfoService;

	//private static final Logger logger = Logger.getLogger(ProductInfoResource.class);
	
	@GET
	@Path("/get_products_category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductCategoriesNames() {

		List<String> responseItems = productsInfoService.getProductCategoriesInfo();

		if(responseItems.size()>0){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}
	}
	
	@GET
	@Path("/get_products_items/{categoryName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPopularProductItemsNames(@PathParam("categoryName") String categoryName) {
		
		//productsInfoService.writeLogToFlume("getPopularProductItemsNames: " + categoryName);
		//logger.info("[REQUEST]: '/get_products_items/{categoryName}' >> " + categoryName);
	
		List<Product> products = productsInfoService.getProductItemsDetails(categoryName);
		List<ProductDetailResponse> responseItems = new ArrayList<ProductDetailResponse>();
		
		for(Product product : products){
			responseItems.add(new ProductDetailResponse(product.getId(), product.getName(),product.getPrice()));
		}
		
		if(responseItems.size()>0){
			return Response.status(200).entity(responseItems).build();
		} else {
			return Response.status(500).build();
		}

	}
}
