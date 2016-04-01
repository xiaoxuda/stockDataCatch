package com.kimi.catcher;

import com.kimi.entity.StockInfo;
import com.kimi.service.CatchTask;
import com.kimi.service.StockInfoService;
import com.kimi.service.TaskType;
/*
 * @author kimi
 * @see 抓取上市公司股票数量
 */
public class StockInfoDetailCatcher extends BaseCatcher{
	private StockInfoService stockInfoService;
	
	public void setStockInfoService(StockInfoService stockInfoService){
		this.stockInfoService=stockInfoService;
	};
	public StockInfoDetailCatcher(){
		this.key=TaskType.JUCAONET_COMPANY_SHARECAPITAL;
	}
	@Override
	public boolean extract(String src,CatchTask task){
		if(src==null || src==""){
			return false;
		}
		if(src.contains("没有查询到数据！")){
			return false;
		}
		//提取数据
		int start=src.indexOf("zx_data2");
		if(start==-1){
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
			return false;
		}
		createOrUpdate(l,task);
		return true;
	}
	public boolean createOrUpdate(Long l,CatchTask task){
		  StockInfo old = stockInfoService.selectOne((String)task.getInfo("code"));
		  if(old!=null){
			  StockInfo si = new StockInfo();
			  si.setCode(old.getCode());
			  si.setSc(l);
			  stockInfoService.updateSelective(si);
		  }
		  return true;
	}
}
