
package com.kimi.stockanalysis.service;

import com.kimi.stockanalysis.dao.StockInfoDao;
import com.kimi.stockanalysis.dao.g.BaseDao;
import com.kimi.stockanalysis.entity.StockInfo;
/**
 * @author kimi
 * @version 0.99
 * @see 继承基类
 */
public class StockInfoService extends BaseService<StockInfo,java.lang.String> {
	private StockInfoDao stockInfoDao;

	protected BaseDao<StockInfo,java.lang.String> getDao(){
		return this.stockInfoDao;
	};
	
	public void setStockInfoDao(StockInfoDao dao){
		this.stockInfoDao=dao;
	}
}