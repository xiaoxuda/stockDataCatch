/**
 * 
 */
package com.kimi.stockanalysis.catcher.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.catcher.enums.CycleEnum;
import com.kimi.stockanalysis.catcher.enums.TaskTypeEnum;
import com.kimi.stockanalysis.dao.StockInfoDao;
import com.kimi.stockanalysis.entity.StockInfo;

/**
 * 任务生成器，按照不同任务的时间策略定时生成数据抓取任务
 * 
 * @author kimi
 */
public class TaskGenerateService {
	private final Logger LOGGER = LoggerFactory.getLogger(TaskGenerateService.class);

	@Autowired
	private StockInfoDao stockInfoDao;
	
	@Autowired
	private TaskQueueService taskQueueService;
	
	/** 生成任务执行开关 **/
	private boolean isContinue = true;
	/** 调度周期 ，毫秒 **/
	private long time_gap = 5 * 60 * 1000L;
	/** 上次调度时间 **/
	private Map<String, Date> scheduleMap = new HashMap<String, Date>();

	@PostConstruct
	public void init(){
		//FIXME 考虑将调度时间落入数据库，启动时从数据库读取
	}
	
	/** 启动生成器 **/
	public void startGenerator() {

		Thread thread = new Thread("taskGenerate") {
			@Override
			public void run() {
				while (true) {
					if (isContinue) {
						//commitStockInfoTask();
						//commitDetailInfoTask();
						//commitFinancailStatementTask();
						commitPriceInfoTask();
						//默认抓取最近五个季度的交易详情
						//commitHistoryTradeDetailInfoTask(1);
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

	public void reStartGenerator() {
		this.isContinue = true;
	}

	public void closeGenerator() {
		this.isContinue = false;
	}

	/**
	 * 生成抓取公司详细信息的任务
	 */
	private void commitStockInfoTask() {
		String typeCode = TaskTypeEnum.JUCAONET_COMPANY_LIST.getCode();
		String cycleCode = TaskTypeEnum.JUCAONET_COMPANY_LIST.getCycle();
		if (!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))) {
			return;
		}
		// 更新调度时间
		scheduleMap.put(typeCode, new Date());
		CatchTask task = new CatchTask();
		task.setUrl("http://www.cninfo.com.cn/cninfo-new/information/companylist");
		task.setType(typeCode);
		taskQueueService.commitTask(task);
	}

	/**
	 * 生成抓取公司详细信息的任务
	 */
	private void commitDetailInfoTask() {
		String typeCode = TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL.getCode();
		String cycleCode = TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL.getCycle();
		if (!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))) {
			return;
		}
		// 更新调度时间
		scheduleMap.put(typeCode, new Date());

		//若没有查询到股票信息则清除调度信息
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if (taskInfoList == null || taskInfoList.size() == 0) {
			scheduleMap.put(typeCode, null);
			return;
		}

		for (StockInfo info : taskInfoList) {
			CatchTask task_detail = new CatchTask();
			task_detail.addInfo("code", info.getCode());
			task_detail.addInfo("type", info.getType());
			task_detail.setType(typeCode);
			task_detail.setUrl(
					"http://www.cninfo.com.cn/information/lastest/" + info.getType() + info.getCode() + ".html");
			taskQueueService.commitTask(task_detail);
		}
	}

	/**
	 * 生成抓取股票当前价格的任务
	 * 
	 */
	private void commitPriceInfoTask() {
		String typeCode = TaskTypeEnum.SINAJS_PRICE.getCode();
		String cycleCode = TaskTypeEnum.SINAJS_PRICE.getCycle();
		if (!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))) {
			return;
		}
		// 更新调度时间
		scheduleMap.put(typeCode, new Date());

		//若没有查询到股票信息则清除调度信息
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if (taskInfoList == null || taskInfoList.size() == 0) {
			scheduleMap.put(typeCode, null);
			return;
		}

		for (StockInfo info : taskInfoList) {
			CatchTask task_price = new CatchTask();
			task_price.addInfo("code", info.getCode());
			task_price.addInfo("type", info.getType());
			task_price.setType(typeCode);
			task_price.setUrl("http://hq.sinajs.cn/list=" + info.getType().substring(0, 2) + info.getCode());
			taskQueueService.commitTask(task_price);
		}
	}

	/**
	 * 生成抓取公司财务报表的任务
	 * 
	 */
	private void commitFinancailStatementTask() {
		String typeCode = TaskTypeEnum.EASTMONEYNET_STATEMENT.getCode();
		String cycleCode = TaskTypeEnum.EASTMONEYNET_STATEMENT.getCycle();
		if (!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))) {
			return;
		}
		// 更新调度时间
		scheduleMap.put(typeCode, new Date());

		//若没有查询到股票信息则清除调度信息
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if (taskInfoList == null || taskInfoList.size() == 0) {
			scheduleMap.put(typeCode, null);
			return;
		}
		for (StockInfo info : taskInfoList) {
			CatchTask task = new CatchTask();
			task.setType(typeCode);
			
			//数据源东方财富网，暂时停用
/*			task.setUrl("http://soft-f9.eastmoney.com/soft/gp13.php?code=" + info.getCode()
					+ StockInfoCatcher.typeMap.get(info.getType()));*/
			//数据源同花顺
			task.setUrl("http://stockpage.10jqka.com.cn/basic/"+ info.getCode() +"/main.txt");
			
			task.addInfo("code", info.getCode());
			task.addInfo("type", info.getType());
			taskQueueService.commitTask(task);
		}
	}

	/**
	 * 生成抓取公司最近quarterCount个季度的历史交易信息的任务
	 * @param quarterCount 需要抓取的以当前季度为起点的季度数量
	 */
	private void commitHistoryTradeDetailInfoTask(int quarterCount) {
		String typeCode = TaskTypeEnum.SINAJS_HISTORY_TRADE_DETAIL.getCode();
		String cycleCode = TaskTypeEnum.SINAJS_HISTORY_TRADE_DETAIL.getCycle();
		if (!CycleEnum.getByCode(cycleCode).isInNextCycle(scheduleMap.get(typeCode))) {
			return;
		}
		// 更新调度时间
		scheduleMap.put(typeCode, new Date());
		
		//若没有查询到股票信息则清除调度信息
		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		if (taskInfoList == null || taskInfoList.size() == 0) {
			scheduleMap.put(typeCode, null);
			return;
		}
		
		for (StockInfo info : taskInfoList) {
			//获取当前年份和季度
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			int quarter = (int) Math.ceil((calendar.get(Calendar.MONTH) + 1)/3.0);
			
			for(int sub_num=0; sub_num<quarterCount ; sub_num++){
				CatchTask task = new CatchTask();
				task.setType(typeCode);
				task.setUrl(
						String.format("http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%s&jidu=%s"
								,info.getCode(), year , quarter));
				task.addInfo("code", info.getCode());
				task.addInfo("type", info.getType());
				taskQueueService.commitTask(task);
				
				--quarter;
				//上年第四季度
				if(quarter == 0){
					year -= 1;
					quarter = 4;
				}
			}
		}
	}
}
