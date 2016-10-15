package com.kimi.tools;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kimi.stockanalysis.catcher.service.CatcherManageService;
import com.kimi.stockanalysis.catcher.service.TaskGenerateService;

/*
 * 程序入口，app程序
 * @author kimi
 */
public class Starter {
	private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/com/kimi/stockanalysis/config/log4j.properties");

		LOGGER.info("app is start!");
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:com/kimi/stockanalysis/config/spring-config.xml" });
		
		CatcherManageService catcherManageService = context.getBean("catcherManageService",CatcherManageService.class);
		TaskGenerateService taskGenerateService = context.getBean("taskGenerateService",TaskGenerateService.class);

		//开启爬虫监控
		catcherManageService.startCatcherMonitor();
		//启动任务生成器
		taskGenerateService.startGenerator();
	}
}
