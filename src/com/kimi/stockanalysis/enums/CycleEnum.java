/**
 * 
 */
package com.kimi.stockanalysis.enums;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 周期枚举
 * @author kimi
 *
 */
public enum CycleEnum {
	HOUR("hour","小时",60*60*1000L),
	DAY("day","天",24*60*60*1000L),
	WEEK("week","周",7*24*60*60*1000L);
	
	private String code;
	private String desc;
	private long micromillions;
	
	CycleEnum(String code, String desc, long micromillions){
		this.code=code;
		this.desc=desc;
		this.micromillions=micromillions;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the micromillions
	 */
	public long getMicromillions() {
		return micromillions;
	}
	
	/**
	 * 根据编编码取得周期枚举
	 * @param code
	 * @return
	 */
	public static CycleEnum getByCode(String code){
		if(StringUtils.isBlank(code)){
			return null;
		}
		for(CycleEnum em : CycleEnum.values()){
			if(em.getCode().equals(code)){
				return em;
			}
		}
		return null;
	}
	
	/**
	 * 根据上次执行的时间点判断是否开启下一个周期
	 * @param lastTimePoint
	 * @return
	 */
	public boolean isInNextCycle(Date lastTimePoint){
		//无上次执行时间点则默认执行
		if(null == lastTimePoint){
			return true;
		}
		Date now = new Date();
		
		return (now.getTime()-lastTimePoint.getTime()) >= this.getMicromillions()
				? true : false;
	}
}
