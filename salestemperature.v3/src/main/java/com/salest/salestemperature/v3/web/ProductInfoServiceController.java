package com.salest.salestemperature.v3.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import com.salest.salestemperature.v3.api.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.model.Product;
import com.salest.salestemperature.v3.service.ProductsInfoService;

@Controller
@RequestMapping(value="/productinfo")
public class ProductInfoServiceController {

	@Autowired
	ProductsInfoService productsInfoService;

	@RequestMapping(value="/get_products_category", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getProductsCategory(){
		List<String> responseItems = productsInfoService.getProductCategoriesInfo();
		return responseItems;
	}
	
	@RequestMapping(value="/get_products_items/{categoryName}", method=RequestMethod.GET)
	@ResponseBody
	public List<ProductDetailResponse> getPopularProductItemsNames(@PathVariable String categoryName){
		
		List<ProductDetailResponse> responseItems = new ArrayList<ProductDetailResponse>();
		List<Product> products = productsInfoService.getProductItemsDetails(categoryName);

		for(Product product : products){
			responseItems.add(new ProductDetailResponse(product.getId(), product.getName(),product.getPrice()));
		}
			
		return responseItems;
	}

}
