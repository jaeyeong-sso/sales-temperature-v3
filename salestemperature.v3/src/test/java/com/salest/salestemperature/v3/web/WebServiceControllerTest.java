package com.salest.salestemperature.v3.web;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.server.RequestBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import com.salest.salestemperature.v3.dto.ProductInfoDtoService;
import com.salest.salestemperature.v3.service.ProductsInfoService;
import com.salest.salestemperature.v3.web.RealtimeReportController;

import static org.hamcrest.CoreMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/applicationContext.xml","/dispatcher-servlet.xml" })
@WebAppConfiguration
public class WebServiceControllerTest {

	@Autowired
	WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	MockHttpSession session;   
	
	
	@Before
	public void setUp(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void loadingControllerTest(){
		assertNotNull(this.wac.getBean(SalesVolumeServiceController.class));
		assertNotNull(this.wac.getBean(ProductInfoServiceController.class));
		assertNotNull(this.wac.getBean(SalesLogServiceController.class));
	}
	
	//@Test
	public void integrationSalesLogServiceControllerTest() throws Exception{
		
		MockHttpSession mocksession_1 = new MockHttpSession();
		MockHttpSession mocksession_2 = new MockHttpSession();
		
	   	String PARAM_KEY_PRODUCT_NAME = "product_name";
	   	String PARAM_KEY_TR_DATE = "tr_date";
	   	String PARAM_TR_TIME = "tr_time";

	   	// Depend on "get_products_category" call.
		this.mockMvc.perform(get("/productinfo/get_products_category").session(mocksession_1))
			//.andDo(print())
			.andExpect(status().isOk());
		
		// [Case #1] Working in the same Session Bean Scope
		this.mockMvc.perform(post("/saleslog/write").session(mocksession_1)
				.param(PARAM_KEY_PRODUCT_NAME, "아메리카노")
				.param(PARAM_KEY_TR_DATE, "2016-06-15")
				.param(PARAM_TR_TIME, "15:38:00"))
			//.andDo(print())
			.andExpect(status().isOk());
		
		/*
		// [Case #2] Working in the different Session Bean Scope
		this.mockMvc.perform(post("/saleslog/write").session(mocksession_2)
				.param(PARAM_KEY_PRODUCT_NAME, "아메리카노")
				.param(PARAM_KEY_TR_DATE, "2016-06-15")
				.param(PARAM_TR_TIME, "15:39:00"))
			//.andDo(print())
			.andExpect(status().isInternalServerError());
		*/
	}
	
	
	//@Test
	public void SalesVolumeServiceControllerTest() throws Exception{
		
		MockHttpSession mocksession = new MockHttpSession();
		
		this.mockMvc.perform(get("/salesvolume/annual_sales_vol_sum/" + "2014").session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/salesvolume/monthly_sales_vol/" + "2014").session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/salesvolume/products_sales_vol/" + "2014").session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(get(new URI("/salesvolume/products_sales_vol/" + "2014" + "/커피")).session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/salesvolume/timebase_sales_vol/" + "2014/12").session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/salesvolume/dayofweek_sales_vol/" + "2014/12").session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	
	
	//@Test
	public void ProductInfoServiceControllerTest() throws Exception{
		MockHttpSession mocksession = new MockHttpSession();

		this.mockMvc.perform(get(new URI("/productinfo/get_products_items/" + "커피")).session(mocksession))
				.andDo(print())
				.andExpect(status().isOk());
		
		
		// "get_products_category" need to do test with Session Bean Scope
		
		MockHttpSession mocksession_1 = new MockHttpSession();
		MockHttpSession mocksession_2 = new MockHttpSession();
		
		assertNotEquals(mocksession_1, mocksession_2);
		
		this.mockMvc.perform(get("/productinfo/get_products_category").session(mocksession_1))
			.andDo(print())
			.andExpect(status().isOk());
		
		Thread.sleep(3000);
		
		this.mockMvc.perform(get("/productinfo/get_products_category").session(mocksession_2))
				.andDo(print())
				.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/productinfo/get_products_category").session(mocksession_1))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
}
