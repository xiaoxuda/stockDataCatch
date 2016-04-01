
package com.kimi.dao;

import com.kimi.dao.g.BaseDao;
import com.kimi.entity.StockInfo;
/**
 * @author kimi
 * @version 0.99
 * @see 继承基类，定义基本操作以外的数据库操作
 */
public class StockInfoDao extends BaseDao<StockInfo, java.lang.String> {
	public StockInfoDao(){
		this.namespace="com.kimi.dao.StockInfoDao";
	}
}