package com.salest.salestemperature.v3.web;

import java.util.ArrayList;
import java.util.List;

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
import com.salest.salestemperature.v3.web.request.model.ProductDetailResponse;

@Controller
@RequestMapping(value="/productinfo")
public class ProductInfoServiceController {

	@Autowired
	ProductsInfoService productsInfoService;

	@RequestMapping(value="/get_products_category", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProductsCategory(){
		
		List<String> responseItems = productsInfoService.getProductCategoriesInfo();

		if(responseItems!=null && responseItems.size()>0){
			return new ResponseEntity<List<String>>(responseItems, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/get_products_items/{categoryName}", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPopularProductItemsNames(@PathVariable String categoryName){
		
		List<ProductDetailResponse> responseItems = new ArrayList<ProductDetailResponse>();
		List<Product> products = productsInfoService.getProductItemsDetails(categoryName);

		for(Product product : products){
			responseItems.add(new ProductDetailResponse(product.getId(), product.getName(),product.getPrice()));
		}
			
		if(products!=null && responseItems.size()>0){
			return new ResponseEntity<List<ProductDetailResponse>>(responseItems, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
