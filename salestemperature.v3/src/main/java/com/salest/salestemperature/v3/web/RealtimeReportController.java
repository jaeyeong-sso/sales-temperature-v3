package com.salest.salestemperature.v3.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RealtimeReportController {

	@RequestMapping("/realtime_report")
	public ModelAndView realtimeReport(){
		ModelAndView mv = new ModelAndView("realtime_report");
		return mv;
	}
}
