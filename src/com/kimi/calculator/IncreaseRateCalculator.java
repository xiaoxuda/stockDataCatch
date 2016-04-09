package com.kimi.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

import com.kimi.entity.FinancailStatement;
import com.kimi.entity.StockInfo;
import com.kimi.service.FinancailStatementService;
import com.kimi.service.StockInfoService;
/**
 * @see 增长率计算器，使用线程池技术定期对数据做变动率运算
 * @author zq
 *
 */
public class IncreaseRateCalculator {
	private FinancailStatementService financailStatementService;
	private StockInfoService stockInfoService;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(4);//需要根据部署服务器的性能及服务器常规负载做修改
	
	public void setFinancailStatementService(FinancailStatementService financailStatementService){
		this.financailStatementService=financailStatementService;
	};
	public void setStockInfoService(StockInfoService stockInfoService){
		this.stockInfoService=stockInfoService;
	};
	
	private class IncreaseRateCalculatorThread implements Runnable{
		private String code;
		public IncreaseRateCalculatorThread(String code){
			this.code=code;
		}
		public void run(){
			//读取编码为code的股票对应的所有财务数据
			FinancailStatement fs = new FinancailStatement();
			fs.setCode(this.code);
			List<FinancailStatement> list = financailStatementService.selectList(fs);
			//计算增长率并更新数据库
			calculate(list);
			for(FinancailStatement ifs : list){
				financailStatementService.updateSelective(ifs);
			}
		}
		
		private void calculate(List<FinancailStatement> list){
			//以日期为key保存数据库，方便查找上季度数据
			HashMap<String,FinancailStatement> map = new HashMap<String,FinancailStatement>();
			for(FinancailStatement fs : list){
				map.put(fs.getDate(), fs);
			}
			for(FinancailStatement fs : list){
				//获取当前季度数据的上年同期数据，非空，则计算增长率
				FinancailStatement lfs=map.get(getLastDate(fs.getDate()));
				if(lfs!=null){
					if(fs.getToi()!=null && lfs.getToi()!=null){
						fs.setToiuprate((float)((fs.getToi()-lfs.getToi())/(lfs.getToi()<=0?1:lfs.getToi())));
					}
					if(fs.getTp()!=null && lfs.getTp()!=null){
						fs.setTpuprate((float)((fs.getTp()-lfs.getTp())/(lfs.getTp()<=0?1:lfs.getTp())));
					}
				}
			}
			return; 
		}
		//去年同期的财务报表日期
		private String getLastDate(String date){
			if(date==null)
				return null;
			try{
				String year=date.substring(0, 2);
				Integer y=Integer.valueOf(year);
				y-=1;
				return StringUtils.leftPad(y.toString(), 2, "0")+date.substring(2,date.length());
			}catch(Exception e){
				return null;
			}
		}
	}
	
	public void submitCalculate(){
		//共几千条数据，可以直接取出放到内存中，如果数据量过大需要分批次取出并处理，减少数据库单次的压力及内存的消耗
		List<StockInfo> list = stockInfoService.selectList(new StockInfo());
		for(StockInfo si : list){
			executorService.execute(new IncreaseRateCalculatorThread(si.getCode()));
		}
		executorService.shutdown();
	}
}

