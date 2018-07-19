package com.jtd.web.service.task;

import org.apache.log4j.Logger;

import com.jtd.utils.SpringContextHolder;

public class Task {
	
	private Logger log = Logger.getLogger(Task.class);

	public void taskOfCheckPartnerAccBalance() {
		
		TaskService service = (TaskService) SpringContextHolder
				.getBean("taskService");
		service.taskOfCheckPartnerAccBalance();
		
	}

	public void taskOfCheckAdvertiserAuditStatus() {
		
		TaskService service = (TaskService) SpringContextHolder
				.getBean("taskService");
		service.taskOfCheckAdvertiserAuditStatus();
		
	}
	
	public void taskOfCheckCreativeAuditStatus() {
		
		TaskService service = (TaskService) SpringContextHolder
				.getBean("taskService");
		service.taskOfCheckCreativeAuditStatus();
		
	}
	
	public void taskOfBesSettingRefresh() {
		
		TaskService service = (TaskService) SpringContextHolder
				.getBean("taskService");
		
		service.taskOfBesSettingRefresh();
		
	}
	
	public void taskOfGenerateReportDemoData(){
		
		TaskService service = (TaskService) SpringContextHolder
				.getBean("taskService");
		service.taskOfGenerateReportDemoData();
	}
	
}
