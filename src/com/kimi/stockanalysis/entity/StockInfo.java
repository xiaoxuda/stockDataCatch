﻿
package com.kimi.stockanalysis.entity;

import java.text.ParseException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author kimi
 */
public class StockInfo implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "StockInfo";
	public static final String ALIAS_CODE = "股票交易代码";
	public static final String ALIAS_TYPE = "股票类型：01沪市主板，02深市主板";
	public static final String ALIAS_NAME = "股票名称";
	public static final String ALIAS_SC = "股本数量";
	public static final String ALIAS_PRICE = "当前价格";
	public static final String ALIAS_MODIFY_TIME = "最后修改时间";

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	//date formats
	public static final String FORMAT_MODIFY_TIME = DATE_TIME_FORMAT;
	
	//columns START
	private java.lang.String code;
	private java.lang.String type;
	private java.lang.String name;
	private java.lang.Long sc;
	private java.lang.Float price;
	private java.util.Date modifyTime;
	//columns END

	public StockInfo(){
	}

	public StockInfo(
		java.lang.String code
	){
		this.code = code;
	}

	public void setCode(java.lang.String value) {
		this.code = value;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	public void setType(java.lang.String value) {
		this.type = value;
	}
	
	public java.lang.String getType() {
		return this.type;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	
	public java.lang.String getName() {
		return this.name;
	}
	public void setSc(java.lang.Long value) {
		this.sc = value;
	}
	
	public java.lang.Long getSc() {
		return this.sc;
	}
	public void setPrice(java.lang.Float value) {
		this.price = value;
	}
	
	public java.lang.Float getPrice() {
		return this.price;
	}
	public String getModifyTimeString() {
		return getModifyTime()==null?null:DateFormatUtils.format(getModifyTime(), FORMAT_MODIFY_TIME);
	}
	public void setModifyTimeString(String value) {
		try {
			setModifyTime(DateUtils.parseDate(value, new String[]{FORMAT_MODIFY_TIME,DATE_TIME_FORMAT}));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setModifyTime(java.util.Date value) {
		this.modifyTime = value;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

	//region toString & equals & clone
    @Override
    public String toString() {
        return "StockInfo{" +
				 "code=" + code +  "," +
				 "type=" + type +  "," +
				 "name=" + name +  "," +
				 "sc=" + sc +  "," +
				 "price=" + price +  "," +
				 "modifyTime=" + modifyTime + 
                '}';
    }	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if(obj instanceof StockInfo == false) return false;
		StockInfo other = (StockInfo)obj;
		if (!code.equals(other.code)) return false;
        return true;			
	}
	@Override
    public int hashCode() {
        int result = super.hashCode();
		result = 31 * result + code.hashCode();
        return result;
    }
	public StockInfo clone(){
		StockInfo newobj=new StockInfo();
			 newobj.code=this.code;
			 newobj.type=this.type;
			 newobj.name=this.name;
			 newobj.sc=this.sc;
			 newobj.price=this.price;
			 newobj.modifyTime=this.modifyTime;
		return newobj;
	}
	//endregion
}

