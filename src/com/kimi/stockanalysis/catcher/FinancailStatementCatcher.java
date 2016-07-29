package com.kimi.stockanalysis.catcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.entity.FinancailStatement;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.StockDataService;
/*
 * @author kimi
 * @see 抓取上市公司财务报表
 */
public class FinancailStatementCatcher extends BaseCatcher{
	
	@Autowired
	private StockDataService stockDataService;
		
	@Override
	public String getTaskkey() {
		return TaskTypeEnum.EASTMONEYNET_STATEMENT.getCode();
	}
	
	@Override
	public boolean extract(String src,CatchTask task){
		if(src==null || src=="" || src.contains("该品种暂无此项记录!")){
			LOGGER.error("TaskType:{} param:{},抓取财务报表失败！",task.getType(), task.getInfo());
			return false;
		}
		int start=src.indexOf("<table id=\"tablefont\"");
		int end=src.indexOf("<table id=\"fixedtableheader\"");
		if(start==-1 || end==-1){
			LOGGER.error("TaskType:{} param:{},抓取财务报表失败！",task.getType(), task.getInfo());
			return false;
		}
		String table=src.substring(start, end);
		start=table.indexOf("<tr>")+4;
		end=table.lastIndexOf("</tr>");
		table=table.substring(start, end);
		table=table.replaceAll("<td[^>]*>|<p[^>]*>|</td>|</p>|&nbsp;", "");
		table=table.replaceAll("--", "0");
		List<String> trs=new ArrayList<String>(Arrays.asList(table.split("</tr><tr>")));
		for(int i=0;i<trs.size();){
			String temp=trs.get(i);
			if(temp.contains("表摘要")||temp.contains("每股指标")){
				trs.remove(i);
				continue;
			}else{
				temp=temp.substring(6,temp.length()-7);
				temp=temp.replaceAll("</span><span>", ",");
				trs.set(i, temp);
				++i;
			}
		}
		createOrUpdate(trs,task);
		return true;
	}
	
	public double extractData(String s){
		double result=0;
		if(s==null || s.isEmpty()){
			return result;
		}
		try{
			int index;
			if((index=s.indexOf("万亿"))>-1){
				s=s.substring(0,index);
				result=Double.valueOf(s.isEmpty()?"0":s)*1000000000000l;
			}
			else if((index=s.indexOf("亿"))>-1){
				s=s.substring(0,index);
				result=Double.valueOf(s.isEmpty()?"0":s)*100000000;
			}
			else if((index=s.indexOf("万"))>-1){
				s=s.substring(0,index);
				result=Double.valueOf(s.isEmpty()?"0":s)*10000;
			}
			else{
				result=Double.valueOf(s.isEmpty()?"0":s);
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("【{}】解析异常{}", s, e.getMessage());
		}
		return result;
	}
	
	public boolean createOrUpdate(List<String> list,CatchTask task){
		  List<String[]> aList = new ArrayList<String[]>();
		  for(String s:list){
			  aList.add(s.split(","));
		  }
		  int size = aList.get(0).length;
		  for(int i=1;i<size;i++){
			  FinancailStatement financailStatement = new FinancailStatement();
			  financailStatement.setCode((String)task.getInfoValue("code"));
			  financailStatement.setDate(aList.get(0)[i]);
			  financailStatement.setPe(extractData(aList.get(1)[i]));
			  financailStatement.setBvps(extractData(aList.get(3)[i]));
			  financailStatement.setCps(extractData(aList.get(4)[i]));
			  financailStatement.setRoe(extractData(aList.get(5)[i]));
			  financailStatement.setJroe(extractData(aList.get(6)[i]));
			  financailStatement.setSgpr(extractData(aList.get(7)[i]));
			  financailStatement.setSmpr(extractData(aList.get(8)[i]));
			  financailStatement.setDtar(extractData(aList.get(9)[i]));
			  financailStatement.setOpgr(extractData(aList.get(10)[i]));
			  financailStatement.setToi(extractData(aList.get(12)[i]));
			  financailStatement.setToc(extractData(aList.get(13)[i]));
			  financailStatement.setOi(extractData(aList.get(14)[i]));
			  financailStatement.setOc(extractData(aList.get(15)[i]));
			  financailStatement.setOp(extractData(aList.get(16)[i]));
			  financailStatement.setTp(extractData(aList.get(17)[i]));
			  financailStatement.setMp(extractData(aList.get(18)[i]));
			  financailStatement.setMpbpc(extractData(aList.get(19)[i]));
			  financailStatement.setTa(extractData(aList.get(20)[i]));
			  financailStatement.setTl(extractData(aList.get(21)[i]));
			  financailStatement.setSe(extractData(aList.get(22)[i]));
			  financailStatement.setTacf(extractData(aList.get(24)[i]));
			  financailStatement.setIacf(extractData(aList.get(25)[i]));
			  financailStatement.setFacf(extractData(aList.get(26)[i]));
			  financailStatement.setCnca(extractData(aList.get(27)[i]));
			  
			  stockDataService.fsUpdateOrInsert(financailStatement);
		  }
		  return true;
	}
}
