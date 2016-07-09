/**
 * 
 */
package com.kimi.stockanalysis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.catcher.StockInfoCatcher;
import com.kimi.stockanalysis.dao.StockInfoDao;
import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.TaskTypeEnum;

/**
 * @author kimi
 */
public class TaskGenerateService {
	@Autowired
	private StockInfoDao stockInfoDao;

	public void execute() {
		// 上市公司信息
		commitStockInfoTask();

		List<StockInfo> taskInfoList = stockInfoDao.selectList(new StockInfo());
		for (StockInfo info : taskInfoList) {
			commitDetailInfoTask(info.getCode(), info.getType());
			commitFinancailStatementTask(info.getCode(), info.getType());
			commitPriceInfoTask(info.getCode(), info.getType());
		}
	}

	/**
	 * 生成抓取公司详细信息的任务
	 * 
	 * @param code 股票代码
	 * @param type 股票类型
	 */
	private void commitStockInfoTask() {
		CatchTask task = new CatchTask();
		task.setUrl("http://www.cninfo.com.cn/cninfo-new/information/companylist");
		task.setType(TaskTypeEnum.JUCAONET_COMPANY_LIST);
		TaskQueueService.commitTask(task);
	}

	/**
	 * 生成抓取公司详细信息的任务
	 * 
	 * @param code 股票代码
	 * @param type 股票类型
	 */
	private void commitDetailInfoTask(String code, String type) {
		CatchTask task_detail = new CatchTask();
		task_detail.addInfo("code", code);
		task_detail.setType(TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL);
		task_detail.setUrl("http://www.cninfo.com.cn/information/lastest/" + type + code + ".html");
		TaskQueueService.commitTask(task_detail);
	}

	/**
	 * 生成抓取股票当前价格的任务
	 * 
	 * @param code 股票代码
	 * @param type 股票类型
	 */
	private void commitPriceInfoTask(String code, String type) {
		CatchTask task_price = new CatchTask();
		task_price.addInfo("code", code);
		task_price.setType(TaskTypeEnum.SINAJS_PRICE);
		task_price.setUrl("http://hq.sinajs.cn/list=" + type.substring(0, 2) + code);
		TaskQueueService.commitTask(task_price);
	}

	/**
	 * 生成抓取公司财务报表的任务
	 * 
	 * @param code 股票代码
	 * @param type 股票类型
	 */
	private void commitFinancailStatementTask(String code, String type) {
		CatchTask task = new CatchTask();
		task.setType(TaskTypeEnum.EASTMONEYNET_STATEMENT);
		task.setUrl("http://soft-f9.eastmoney.com/soft/gp13.php?code=" + code + StockInfoCatcher.typeMap.get(type));
		task.addInfo("code", code);
		TaskQueueService.commitTask(task);
	}

}
