package com.kimi.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kimi.calculator.IncreaseRateCalculator;
import com.kimi.catcher.BaseCatcher;
import com.kimi.catcher.FinancailStatementCatcher;
import com.kimi.catcher.StockInfoCatcher;
import com.kimi.catcher.StockInfoDetailCatcher;
import com.kimi.service.CatchTask;
import com.kimi.service.TaskQueueService;
import com.kimi.service.TaskType;
/*
 * @author kimi
 */
public class MyController {
	public static void main(String[] args) {
		taskGenerator();
	}
	/**
	 * 任务加载器，所有任务都需要在这里加载到任务队列，爬虫需要在这里启动
	 */
	public static void taskGenerator(){
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"classpath:com/kimi/config/spring-config.xml"});
		//上市公司信息
		CatchTask task=new CatchTask();
		task.setUrl("http://www.cninfo.com.cn/cninfo-new/information/companylist");
		task.setType(TaskType.JUCAONET_COMPANY_LIST);
		TaskQueueService.commitTask(TaskType.JUCAONET_COMPANY_LIST,task);
		BaseCatcher pcicatcher = context.getBean("stockInfoCatcher",StockInfoCatcher.class);
		pcicatcher.start();
		//上市公司详细信息
		BaseCatcher sidcatcher = context.getBean("stockInfoDetailCatcher",StockInfoDetailCatcher.class);
		sidcatcher.start();
		//上市公司财务报表
		BaseCatcher estcatcher = context.getBean("financailStatementCatcher",FinancailStatementCatcher.class);
		estcatcher.start();
		
		//计算财务数据变化率
		IncreaseRateCalculator increaseRateCalculator = context.getBean("increaseRateCalculator",IncreaseRateCalculator.class);
		increaseRateCalculator.submitCalculate();
		
	}
}
