/**
 * 
 */
package com.kimi.stockanalysis.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.catcher.StockInfoCatcher;
import com.kimi.stockanalysis.dao.StockInfoDao;
import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.CycleEnum;
import com.kimi.stockanalysis.enums.TaskTypeEnum;

/**
 * 任务生成器，按照不同任务的时间策略定时生成数据抓取任务
 * @author kimi
 */
public class TaskGenerateService {
	Logger LOGGER = LoggerFactory.getLogger(TaskGenerateService.class);
	
	@Autowired
	private StockInfoDao stockInfoDao;

	/** 生成任务执行开关 **/
	private boolean isContinue = true;
	/** 调度周期 ，毫秒**/
	private long time_gap = 5 * 60 * 1000L;
	/** 上次调度时间 **/
	private Map<String,Date> scheduleMap = new HashMap<String,Date>();
	
	/**启动生成器**/
	public void startGenerator() {
		
		Thread thread = new Thread("taskGenerate"){
			@Override
			public void run(){
				while(true){
					if(isContinue){
						commitStockInfoTask();
						commitDetailInfoTask();
						commitFinancailStatementTask();
						commitPriceInfoTask();
					}
					
					try {
						Thread.sleep(time_gap);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						LOGGER.error("taskGenerate,异常信息{}", e.getMessage());
					}
					
				}
			}
		};
		thread.start();
	}

	public void reStartGenerator(){
		this.isContinue = true;
	}
	
	public void closeGenerator(){
		this.isContinue = false;
	}
	
	/**
	 * 生成抓取公司详细信息的任务
	 */
	private void commitStockInfoTask() {
		String typeCode = TaskTypeEnum.JUCAONET_COMPANY_LIST.getCode();
		String cycleCode = TaskTypeEnum.JUCAONET_COMPANY_LIST.getCycle();
		if(!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))){
			return;
		}
		//更新调度时间
		scheduleMap.put(typeCode,new Date());
		CatchTask task = new CatchTask();
		task.setUrl("http://www.cninfo.com.cn/cninfo-new/information/companylist");
		task.setType(typeCode);
		TaskQueueService.commitTask(task);
	}

	/**
	 * 生成抓取公司详细信息的任务
	 */
	private void commitDetailInfoTask() {
		String typeCode = TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL.getCode();
		String cycleCode = TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL.getCycle();
		if(!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))){
			return;
		}
		//更新调度时间
		scheduleMap.put(typeCode,new Date());
		
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if(taskInfoList == null || taskInfoList.size() == 0){
			scheduleMap.put(typeCode,null);
			return;
		}
		
		for (StockInfo info : taskInfoList) {
			CatchTask task_detail = new CatchTask();
			task_detail.addInfo("code", info.getCode());
			task_detail.addInfo("type", info.getType());
			task_detail.setType(typeCode);
			task_detail.setUrl("http://www.cninfo.com.cn/information/lastest/" 
					+ info.getType() + info.getCode() + ".html");
			TaskQueueService.commitTask(task_detail);
		}
	}

	/**
	 * 生成抓取股票当前价格的任务
	 * 
	 */
	private void commitPriceInfoTask() {
		String typeCode = TaskTypeEnum.SINAJS_PRICE.getCode();
		String cycleCode = TaskTypeEnum.SINAJS_PRICE.getCycle();
		if(!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))){
			return;
		}
		//更新调度时间
		scheduleMap.put(typeCode,new Date());
		
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if(taskInfoList == null || taskInfoList.size() == 0){
			scheduleMap.put(typeCode,null);
			return;
		}
		
		for (StockInfo info : taskInfoList) {
			CatchTask task_price = new CatchTask();
			task_price.addInfo("code", info.getCode());
			task_price.addInfo("type", info.getType());
			task_price.setType(typeCode);
			task_price.setUrl("http://hq.sinajs.cn/list=" 
					+ info.getType().substring(0, 2) + info.getCode());
			TaskQueueService.commitTask(task_price);
		}
	}

	/**
	 * 生成抓取公司财务报表的任务
	 * 
	 */
	private void commitFinancailStatementTask() {
		String typeCode = TaskTypeEnum.EASTMONEYNET_STATEMENT.getCode();
		String cycleCode = TaskTypeEnum.EASTMONEYNET_STATEMENT.getCycle();
		if(!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))){
			return;
		}
		//更新调度时间
		scheduleMap.put(typeCode,new Date());
		
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if(taskInfoList == null || taskInfoList.size() == 0){
			scheduleMap.put(typeCode,null);
			return;
		}
		for (StockInfo info : taskInfoList) {
			CatchTask task = new CatchTask();
			task.setType(typeCode);
			task.setUrl("http://soft-f9.eastmoney.com/soft/gp13.php?code=" 
				+ info.getCode() + StockInfoCatcher.typeMap.get(info.getType()));
			task.addInfo("code", info.getCode());
			task.addInfo("type", info.getType());
			TaskQueueService.commitTask(task);
		}
	}

}
