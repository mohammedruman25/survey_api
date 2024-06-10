package com.survey.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.survey.api.surveymgmt.service.SurveyService;

@EnableScheduling
@Configuration

public class ScheduledCronJob {
	
	@Autowired
	SurveyService surServ;
	
	@Scheduled(cron = "0 0 1 * * ?")  // Job run everyday 1 AM 
	public void scheduleTaskUsingCronExpression() {
	 
	    long now = System.currentTimeMillis() / 1000;
	    System.out.println(
	      "schedule tasks using cron jobs - " + now);
	    surServ.setSurveyExpired();
	}

}
