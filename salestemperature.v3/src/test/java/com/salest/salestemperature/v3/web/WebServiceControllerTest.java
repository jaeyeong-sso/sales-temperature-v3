package com.salest.salestemperature.v3.web;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
		assertNotNull(this.wac.getBean(WebServiceController.class));
	}

	@Test
	public void testSessionScope() throws Exception {
		 
		MockHttpSession mocksession_1 = new MockHttpSession();
		
		this.mockMvc.perform(get("/productinfo/get_products_category")
			.session(mocksession_1))
			//.andDo(print())
			.andExpect(status().isOk());
		
		Thread.sleep(3000);
		
		MockHttpSession mocksession_2 = new MockHttpSession();
		
		this.mockMvc.perform(get("/productinfo/get_products_category")
				.session(mocksession_2))
				//.andDo(print())
				.andExpect(status().isOk());
		
		this.mockMvc.perform(get("/productinfo/get_products_category")
				.session(mocksession_1))
				//.andDo(print())
				.andExpect(status().isOk());
		
		assertNotEquals(mocksession_1, mocksession_2);
	}
}
