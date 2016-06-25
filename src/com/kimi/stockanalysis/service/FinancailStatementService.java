
package com.kimi.stockanalysis.service;

import com.kimi.stockanalysis.dao.FinancailStatementDao;
import com.kimi.stockanalysis.dao.g.BaseDao;
import com.kimi.stockanalysis.entity.FinancailStatement;
/**
 * @author kimi
 * @version 0.99
 * @see 继承基类
 */
public class FinancailStatementService extends BaseService<FinancailStatement,FinancailStatement> {
	private FinancailStatementDao financailStatementDao;

	protected BaseDao<FinancailStatement,FinancailStatement> getDao(){
		return this.financailStatementDao;
	};
	
	public void setFinancailStatementDao(FinancailStatementDao dao){
		this.financailStatementDao=dao;
	}
}