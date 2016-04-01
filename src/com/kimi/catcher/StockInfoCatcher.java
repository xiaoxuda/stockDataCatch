package com.kimi.catcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.kimi.entity.StockInfo;
import com.kimi.service.CatchTask;
import com.kimi.service.StockInfoService;
import com.kimi.service.TaskQueueService;
import com.kimi.service.TaskType;
/*
 * @author kimi
 * @see 抓取股票代码与名称、股票类型
 */
public class StockInfoCatcher extends BaseCatcher{
	private StockInfoService stockInfoService;
	
	public void setStockInfoService(StockInfoService stockInfoService){
		this.stockInfoService=stockInfoService;
	};
	//股票类型
	public class TypeClass{
		public final static String SZMB="szmb";
		public final static String SZSME="szsme";
		public final static String SZCN="szcn";
		public final static String SHMB="shmb";
		public final static String HKMB="hk_mb";
		public final static String HKGEN="hk_gem";
	}
	//匹配www.eastmoney.com网站对股票类型的定义
	public static Map<String,String> typeMap = new HashMap<String,String>();
	static{
		typeMap.put(TypeClass.SZMB,"02");
		typeMap.put(TypeClass.SZSME,"02");
		typeMap.put(TypeClass.SZCN,"02");
		typeMap.put(TypeClass.SHMB,"01");
	}
	
	public StockInfoCatcher(){
		this.key=TaskType.JUCAONET_COMPANY_LIST;
	}
	@Override
	protected boolean extract(String src,CatchTask task){
		if(src==null || src==""){
			return false;
		}
		int start=src.indexOf("<div class=\"list-ct\">");
		int end=StringUtils.indexOf(src, "</div></div><div class=\"clear\">", start);
		if(start==-1 || end==-1){
			return false;
		}
		//需要使用webclient
		String list=src.substring(start+21, end);
		list=list.replaceAll("[\t]|[ ]", "");
		String table[]=list.split("</div><divid=\"con-a-[0-9]{1}\"[^>]*>");
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		//不取香港市场的股票，顺序需要根据网站同步调整
		String types[]={TypeClass.SZMB,TypeClass.SZSME,TypeClass.SZCN,TypeClass.SHMB};
		for(int i=0;i<4;i++){
			String item = table[i];
			item=item.replaceAll("<li[^>]*>|<a[^>]*>|</li>|<ul[^>]*>|</ul>|<div[^>]*>|</div>","");
			item=item.replaceAll("[\t]|[ ]", "");
			List<String> trs=new ArrayList<String>(Arrays.asList(item.split("</a>")));
			result.put(types[i], trs);
		}
		createOrUpdate(result,task);
		return true;
	}
	private boolean createOrUpdate(Map<String,List<String>> map,CatchTask task){
		  for(String type : map.keySet()){
			  List<String> list = map.get(type);
			  for(String s : list){
				  String code=s.substring(0, 6);
				  StockInfo si = new StockInfo();
				  si.setCode(code);
				  StockInfo old = stockInfoService.selectOne(code);
				  if(old!=null){
					  StockInfo fs = new StockInfo();
					  fs.setCode(old.getCode());
					  fs.setName(s.substring(6).replace(" ", ""));
					  fs.setType(type);
					  stockInfoService.updateSelective(fs);
				  }else{
					  StockInfo fs = new StockInfo();
					  fs.setCode(code);
					  fs.setName(s.substring(6).replace(" ", ""));
					  fs.setType(type);
					  stockInfoService.insert(fs);
				  }
				  
				  //提交公司详细信息抓取任务
				  commitDetailInfoTask(code,type);
				  //提交公司财务报表抓取任务
				  commitFinancailStatementTask(code,type);
			  }
		  }
		  return true;
	}
	/**
	 * 生成抓取公司详细信息的任务
	 * @param code	股票代码
	 * @param type	股票类型
	 */
	private void commitDetailInfoTask(String code,String type){
		CatchTask task_detail=new CatchTask();
		task_detail.setInfo("code", code);
		task_detail.setType(TaskType.JUCAONET_COMPANY_SHARECAPITAL);
		task_detail.setUrl("http://www.cninfo.com.cn/information/lastest/"+type+code+".html");
		TaskQueueService.commitTask(TaskType.JUCAONET_COMPANY_SHARECAPITAL,task_detail);
	}
	/**
	 * 生成抓取公司财务报表的任务
	 * @param code	股票代码
	 * @param type	股票类型
	 */
	private void commitFinancailStatementTask(String code,String type){
		CatchTask task=new CatchTask();
		task.setType(TaskType.EASTMONEYNET_STATEMENT);
		task.setUrl("http://soft-f9.eastmoney.com/soft/gp13.php?code="+code+typeMap.get(type));
		task.setInfo("code",code);
		TaskQueueService.commitTask(TaskType.EASTMONEYNET_STATEMENT,task);
	}
}
