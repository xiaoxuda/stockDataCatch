package com.kimi.stockanalysis.catcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.StockInfoService;
/*
 * @author kimi
 * @see 抓取股票代码与名称、股票类型
 */
public class StockInfoCatcher extends BaseCatcher{
	private StockInfoService stockInfoService;
	
	private Logger logger = LoggerFactory.getLogger(StockInfoCatcher.class);
	
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

	@Override
	public String getTaskkey() {
		return TaskTypeEnum.JUCAONET_COMPANY_LIST;
	}
	
	@Override
	protected boolean extract(String src,CatchTask task){
		if(src==null || src==""){
			logger.error("TaskType:"+task.getType()+" param:"+task.getInfo()+" info:抓取公司列表失败！");
			return false;
		}
		int start=src.indexOf("<div class=\"list-ct\">");
		int end=StringUtils.indexOf(src, "</div></div><div class=\"clear\">", start);
		if(start==-1 || end==-1){
			logger.error("TaskType:"+task.getType()+" param:"+task.getInfo()+" info:抓取公司列表失败！");
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
	/**
	 * 
	 * @param map
	 * @param task
	 * @return
	 */
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
				  
			  }
		  }
		  return true;
	}

}
