
package com.kimi.stockanalysis.dao;

import com.kimi.stockanalysis.dao.g.BaseDao;
import com.kimi.stockanalysis.entity.FinancailStatement;
/**
 * @author kimi
 * @version 1.0
 * @see 继承基类，定义基本操作以外的数据库操作
 */
public class FinancailStatementDao extends BaseDao<FinancailStatement,FinancailStatement> {
	public FinancailStatementDao(){
		this.namespace="com.kimi.stockanalysis.dao.FinancailStatementDao";
	}
}