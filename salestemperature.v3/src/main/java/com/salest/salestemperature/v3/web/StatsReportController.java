package com.salest.salestemperature.v3.web;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class StatsReportController {
	
	@RequestMapping("/stats_report")
	public ModelAndView statsReport(){
		ModelAndView mv = new ModelAndView("stats_report");
		return mv;
	}
}