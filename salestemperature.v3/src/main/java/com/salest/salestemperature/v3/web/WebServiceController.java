package com.salest.salestemperature.v3.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.service.ProductsInfoService;

@Controller
public class WebServiceController {

	@Autowired
	ProductsInfoService productsInfoService;

	@RequestMapping(value="/productinfo/get_products_category", method=RequestMethod.GET)
	@ResponseBody
	public List<String> getProductsCategory(){
		List<String> responseItems = productsInfoService.getProductCategoriesInfo();
		return responseItems;
	}

}
