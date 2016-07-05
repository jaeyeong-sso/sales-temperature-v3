package com.salest.salestemperature.v3.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeSeriesService {

	public String getPastWeekMonthIdxDate(String fromDate){
		
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		try {
			cal.setTime(sdf.parse(fromDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}

		int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
		int dayOfWeek   = cal.get(Calendar.DAY_OF_WEEK);
		
		cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR)-1));
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		cal.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
				
		return String.format("%04d-%02d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DATE));
	}
}
