package com.kimi.stockanalysis.controller;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kimi.stockanalysis.catcher.BaseCatcher;
import com.kimi.stockanalysis.catcher.FinancailStatementCatcher;
import com.kimi.stockanalysis.catcher.StockInfoCatcher;
import com.kimi.stockanalysis.catcher.StockInfoDetailCatcher;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.TaskQueueService;

/*
 * @author kimi
 */
public class MyController {
	private static Logger logger = LoggerFactory.getLogger(MyController.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("src/com/kimi/stockanalysis/config/log4j.properties");

		logger.info("app is start!");
		
		taskGenerator();
	}

	/**
	 * 任务加载器，所有任务都需要在这里加载到任务队列，爬虫需要在这里启动
	 */
	public static void taskGenerator() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:com/kimi/stockanalysis/config/spring-config.xml" });
		// 上市公司信息
		CatchTask task = new CatchTask();
		task.setUrl("http://www.cninfo.com.cn/cninfo-new/information/companylist");
		task.setType(TaskTypeEnum.JUCAONET_COMPANY_LIST);
		TaskQueueService.commitTask(TaskTypeEnum.JUCAONET_COMPANY_LIST, task);
		BaseCatcher pcicatcher = context.getBean("stockInfoCatcher", StockInfoCatcher.class);
		pcicatcher.start();
		// 上市公司详细信息
		BaseCatcher sidcatcher = context.getBean("stockInfoDetailCatcher", StockInfoDetailCatcher.class);
		sidcatcher.start();
		// 上市公司财务报表
		BaseCatcher estcatcher = context.getBean("financailStatementCatcher", FinancailStatementCatcher.class);
		estcatcher.start();
	}
}
