package com.salest.salestemperature.v3.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledBatchTask {
	@Scheduled(cron="*/10 * * * * *") 
    public void updateUrbanServiceArea() {
    	java.util.Calendar calendar = java.util.Calendar.getInstance();
    	java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println("현재 시각: " +  dateFormat.format(calendar.getTime()));
    }
}
