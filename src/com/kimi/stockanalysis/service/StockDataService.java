/**
 * 
 */
package com.kimi.stockanalysis.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.dao.FinancailStatementDao;
import com.kimi.stockanalysis.dao.StockInfoDao;
import com.kimi.stockanalysis.entity.FinancailStatement;
import com.kimi.stockanalysis.entity.StockInfo;

/**
 * @author kimi
 * @version 1.0
 */
public class StockDataService {
	private final Logger LOGGER = LoggerFactory.getLogger(StockDataService.class);

	@Autowired
	private StockInfoDao stockInfoDao;
	@Autowired
	private FinancailStatementDao financailStatementDao;

	/**
	 * 更新或者插入股票基本信息
	 * 
	 * @param entity
	 * @param isNullToSave 是否保存空值
	 * @return 受影响的行数
	 */
	public int siUpdateOrInsert(StockInfo entity, boolean isNullToSave) {
		if (null == entity || StringUtils.isBlank(entity.getCode())) {
			LOGGER.error("参数错误{}", entity);
			return 0;
		}

		int cnt = 0;
		if (isNullToSave) {
			cnt = stockInfoDao.update(entity);
		} else {
			cnt = stockInfoDao.updateSelective(entity);
		}

		if (cnt == 1) {
			return cnt;
		}
		try {
			cnt = stockInfoDao.insert(entity);
		} catch (Exception e) {
			LOGGER.error("插入股票财务报表信息失败，参数{}\n{}", entity, e.getMessage());
		}
		return cnt;
	}
	/**
	 * 更新或者插入股票基本信息
	 * 
	 * @param entity
	 * @return
	 */
	public int siUpdateOrInsert(StockInfo entity){
		return siUpdateOrInsert(entity, true);
	}
	
	/**
	 * 更新或者插入股票财务报表
	 * 
	 * @param entity
	 * @param isNullToSave是否保存空值
	 * @return 受影响的行数
	 */
	public int fsUpdateOrInsert(FinancailStatement entity, boolean isNullToSave) {
		if (null == entity 
				|| StringUtils.isBlank(entity.getCode()) 
				|| StringUtils.isBlank(entity.getDate())) {
			LOGGER.error("参数错误{}", entity);
			return 0;
		}

		int cnt = 0;
		if (isNullToSave) {
			cnt = financailStatementDao.update(entity);
		} else {
			cnt = financailStatementDao.updateSelective(entity);
		}
		if(cnt == 1){
			return cnt;
		}
		
		try {
			cnt = financailStatementDao.insert(entity);
		} catch (Exception e) {
			LOGGER.error("插入股票财务报表信息失败，参数{}\n{}", entity, e.getMessage());
		}
		return cnt;
	}
	/**
	 * 更新或者插入股票财务报表
	 * 
	 * @param entity
	 * @return
	 */
	public int fsUpdateOrInsert(FinancailStatement entity){
		return fsUpdateOrInsert(entity, true);
	}

}
