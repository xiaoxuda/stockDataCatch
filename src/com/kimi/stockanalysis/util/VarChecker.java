/**
 * 
 */
package com.kimi.stockanalysis.util;

import org.slf4j.Logger;

/**
 * 数据校验工具类,配合日志及异常策略
 * @author kimi
 * @version 1.0
 */
public class VarChecker {
	public static boolean isNotBlank(String var, Logger logger, String info){
		if(var == null || var.length() == 0){
			logger.error(info);
		}else{
			return true;
		}
		return false;
	}
}
