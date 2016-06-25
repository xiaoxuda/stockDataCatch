package com.kimi.stockanalysis.catcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.StockInfoService;
/*
 * @author kimi
 * @see 抓取上市公司股票数量
 */
public class StockInfoDetailCatcher extends BaseCatcher{
	private StockInfoService stockInfoService;
	
	private Logger logger = LoggerFactory.getLogger(StockInfoDetailCatcher.class);
	
	public void setStockInfoService(StockInfoService stockInfoService){
		this.stockInfoService=stockInfoService;
	};
	public StockInfoDetailCatcher(){
		this.key=TaskTypeEnum.JUCAONET_COMPANY_SHARECAPITAL;
	}
	@Override
	public boolean extract(String src,CatchTask task){
		if(src==null || src=="" || src.contains("没有查询到数据！")){
			logger.error("TaskType:"+task.getType()+" param:"+task.getInfo()+" info:公司详细信息抓取失败！");
			return false;
		}
		//提取数据
		int start=src.indexOf("zx_data2");
		if(start==-1){
			logger.error("TaskType:"+task.getType()+" param:"+task.getInfo()+" info:公司详细信息提取失败！");
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
			logger.error("TaskType:"+task.getType()+" param:"+task.getInfo()+" info:公司详细信息提取失败！");
			return false;
		}
		createOrUpdate(l,task);
		return true;
	}
	public boolean createOrUpdate(Long l,CatchTask task){
		  StockInfo old = stockInfoService.selectOne((String)task.getInfoValue("code"));
		  if(old!=null){
			  StockInfo si = new StockInfo();
			  si.setCode(old.getCode());
			  si.setSc(l);
			  stockInfoService.updateSelective(si);
		  }
		  return true;
	}
}
