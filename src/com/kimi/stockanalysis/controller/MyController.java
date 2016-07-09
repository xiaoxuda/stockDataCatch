package com.kimi.stockanalysis.controller;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kimi.stockanalysis.service.CatcherManageService;
import com.kimi.stockanalysis.service.TaskGenerateService;

/*
 * @author kimi
 */
public class MyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyController.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/com/kimi/stockanalysis/config/log4j.properties");

		LOGGER.info("app is start!");
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:com/kimi/stockanalysis/config/spring-config.xml" });
		
		CatcherManageService catcherManageService = context.getBean("catcherManageService",CatcherManageService.class);
		TaskGenerateService taskGenerateService = context.getBean("taskGenerateService",TaskGenerateService.class);
		
		catcherManageService.startCatcher();
		taskGenerateService.execute();
	}
}
