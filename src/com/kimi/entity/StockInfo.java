
package com.kimi.entity;

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
	
	//date formats
	
	//columns START
	private java.lang.String code;
	private java.lang.String type;
	private java.lang.String name;
	private java.lang.Long sc;
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

	//region toString & equals & clone
    @Override
    public String toString() {
        return "StockInfo{" +
				 "code=" + code + 
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
		return newobj;
	}
	//endregion
}

