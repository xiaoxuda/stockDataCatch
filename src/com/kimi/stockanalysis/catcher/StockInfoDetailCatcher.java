package com.kimi.stockanalysis.catcher;

import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.StockDataService;
/*
 * @author kimi
 * @see 抓取上市公司股票数量
 */
public class StockInfoDetailCatcher extends BaseCatcher{
	
	@Autowired
	private StockDataService stockDataService;
		
	@Override
	public String getTaskkey() {
		return TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL.getCode();
	}
	
	@Override
	public boolean extract(String src,CatchTask task){
		if(src==null || src=="" || src.contains("没有查询到数据！")){
			LOGGER.error("公司详细信息抓取失败,TaskType:{} param:",task.getType(),task.getInfo());
			return false;
		}
		//提取数据
		int start=src.indexOf("zx_data2");
		if(start==-1){
			LOGGER.error("公司详细信息抓取失败,TaskType:{} param:",task.getType(),task.getInfo());
			return false;
		}
		src=src.substring(start);
		start=src.indexOf(">");
		src=src.substring(start+1);
		int end=src.indexOf("<");
		src=src.substring(0, end-1);
		src=src.replaceAll("[^0-9]", "");
		Long l = Long.valueOf(src.isEmpty()?"0":src);
		if(l.equals(0)){
			LOGGER.error("公司详细信息抓取失败,TaskType:{} param:",task.getType(),task.getInfo());
			return false;
		}
		
		StockInfo stockInfo = new StockInfo();
		stockInfo.setCode(task.getInfo().get("code").toString());
		stockInfo.setSc(l);
		int cnt = stockDataService.siUpdateOrInsert(stockInfo, false);
		return cnt == 1;
	}
	
}
