
package com.kimi.dao;

import com.kimi.dao.g.BaseDao;
import com.kimi.entity.FinancailStatement;
/**
 * @author kimi
 * @version 0.99
 * @see 继承基类，定义基本操作以外的数据库操作
 */
public class FinancailStatementDao extends BaseDao<FinancailStatement,FinancailStatement> {
	public FinancailStatementDao(){
		this.namespace="com.kimi.dao.FinancailStatementDao";
	}
}