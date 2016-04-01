
package com.kimi.entity;

/**
 * @author kimi
 */
public class FinancailStatement implements java.io.Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "FinancailStatement";
	public static final String ALIAS_CODE = "股票代码";
	public static final String ALIAS_DATE = "报告日期，格式yyyy-MM-dd";
	public static final String ALIAS_PE = "每股收益";
	public static final String ALIAS_BVPS = "每股净资产";
	public static final String ALIAS_CPS = "每股现金流";
	public static final String ALIAS_ROE = "净资产收益率";
	public static final String ALIAS_JROE = "总资产净利率";
	public static final String ALIAS_SGPR = "销售毛利率";
	public static final String ALIAS_SMPR = "销售净利率";
	public static final String ALIAS_DTAR = "资产负债率";
	public static final String ALIAS_OPGR = "同比净利润增长率";
	public static final String ALIAS_TOI = "营业总收入";
	public static final String ALIAS_TOC = "营业总成本";
	public static final String ALIAS_OI = "营业收入";
	public static final String ALIAS_OC = "营业成本";
	public static final String ALIAS_OP = "营业利润";
	public static final String ALIAS_TP = "利润总额";
	public static final String ALIAS_MP = "净利润";
	public static final String ALIAS_MPBPC = "归属母公司的净利润";
	public static final String ALIAS_TA = "总资产";
	public static final String ALIAS_TL = "负债总额";
	public static final String ALIAS_SE = "股东权益";
	public static final String ALIAS_TACF = "经营活动产生的现金流";
	public static final String ALIAS_IACF = "投资活动产生的现金流";
	public static final String ALIAS_FACF = "筹资活动产生的现金流";
	public static final String ALIAS_CNCA = "现金及等价物净增加";
	
	//date formats
	
	//columns START
	private java.lang.String code;
	private java.lang.String date;
	private java.lang.Double pe;
	private java.lang.Double bvps;
	private java.lang.Double cps;
	private java.lang.Double roe;
	private java.lang.Double jroe;
	private java.lang.Double sgpr;
	private java.lang.Double smpr;
	private java.lang.Double dtar;
	private java.lang.Double opgr;
	private java.lang.Double toi;
	private java.lang.Double toc;
	private java.lang.Double oi;
	private java.lang.Double oc;
	private java.lang.Double op;
	private java.lang.Double tp;
	private java.lang.Double mp;
	private java.lang.Double mpbpc;
	private java.lang.Double ta;
	private java.lang.Double tl;
	private java.lang.Double se;
	private java.lang.Double tacf;
	private java.lang.Double iacf;
	private java.lang.Double facf;
	private java.lang.Double cnca;
	//columns END

	public FinancailStatement(){
	}

	public FinancailStatement(
		java.lang.String code,
		java.lang.String date
	){
		this.code = code;
		this.date = date;
	}

	public void setCode(java.lang.String value) {
		this.code = value;
	}
	
	public java.lang.String getCode() {
		return this.code;
	}
	public void setDate(java.lang.String value) {
		this.date = value;
	}
	
	public java.lang.String getDate() {
		return this.date;
	}
	public void setPe(java.lang.Double value) {
		this.pe = value;
	}
	
	public java.lang.Double getPe() {
		return this.pe;
	}
	public void setBvps(java.lang.Double value) {
		this.bvps = value;
	}
	
	public java.lang.Double getBvps() {
		return this.bvps;
	}
	public void setCps(java.lang.Double value) {
		this.cps = value;
	}
	
	public java.lang.Double getCps() {
		return this.cps;
	}
	public void setRoe(java.lang.Double value) {
		this.roe = value;
	}
	
	public java.lang.Double getRoe() {
		return this.roe;
	}
	public void setJroe(java.lang.Double value) {
		this.jroe = value;
	}
	
	public java.lang.Double getJroe() {
		return this.jroe;
	}
	public void setSgpr(java.lang.Double value) {
		this.sgpr = value;
	}
	
	public java.lang.Double getSgpr() {
		return this.sgpr;
	}
	public void setSmpr(java.lang.Double value) {
		this.smpr = value;
	}
	
	public java.lang.Double getSmpr() {
		return this.smpr;
	}
	public void setDtar(java.lang.Double value) {
		this.dtar = value;
	}
	
	public java.lang.Double getDtar() {
		return this.dtar;
	}
	public void setOpgr(java.lang.Double value) {
		this.opgr = value;
	}
	
	public java.lang.Double getOpgr() {
		return this.opgr;
	}
	public void setToi(java.lang.Double value) {
		this.toi = value;
	}
	
	public java.lang.Double getToi() {
		return this.toi;
	}
	public void setToc(java.lang.Double value) {
		this.toc = value;
	}
	
	public java.lang.Double getToc() {
		return this.toc;
	}
	public void setOi(java.lang.Double value) {
		this.oi = value;
	}
	
	public java.lang.Double getOi() {
		return this.oi;
	}
	public void setOc(java.lang.Double value) {
		this.oc = value;
	}
	
	public java.lang.Double getOc() {
		return this.oc;
	}
	public void setOp(java.lang.Double value) {
		this.op = value;
	}
	
	public java.lang.Double getOp() {
		return this.op;
	}
	public void setTp(java.lang.Double value) {
		this.tp = value;
	}
	
	public java.lang.Double getTp() {
		return this.tp;
	}
	public void setMp(java.lang.Double value) {
		this.mp = value;
	}
	
	public java.lang.Double getMp() {
		return this.mp;
	}
	public void setMpbpc(java.lang.Double value) {
		this.mpbpc = value;
	}
	
	public java.lang.Double getMpbpc() {
		return this.mpbpc;
	}
	public void setTa(java.lang.Double value) {
		this.ta = value;
	}
	
	public java.lang.Double getTa() {
		return this.ta;
	}
	public void setTl(java.lang.Double value) {
		this.tl = value;
	}
	
	public java.lang.Double getTl() {
		return this.tl;
	}
	public void setSe(java.lang.Double value) {
		this.se = value;
	}
	
	public java.lang.Double getSe() {
		return this.se;
	}
	public void setTacf(java.lang.Double value) {
		this.tacf = value;
	}
	
	public java.lang.Double getTacf() {
		return this.tacf;
	}
	public void setIacf(java.lang.Double value) {
		this.iacf = value;
	}
	
	public java.lang.Double getIacf() {
		return this.iacf;
	}
	public void setFacf(java.lang.Double value) {
		this.facf = value;
	}
	
	public java.lang.Double getFacf() {
		return this.facf;
	}
	public void setCnca(java.lang.Double value) {
		this.cnca = value;
	}
	
	public java.lang.Double getCnca() {
		return this.cnca;
	}

	//region toString & equals & clone
    @Override
    public String toString() {
        return "FinancailStatement{" +
				 "code=" + code +  "," +
				 "date=" + date + 
                '}';
    }	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if(obj instanceof FinancailStatement == false) return false;
		FinancailStatement other = (FinancailStatement)obj;
		if (!code.equals(other.code)) return false;
		if (!date.equals(other.date)) return false;
        return true;			
	}
	@Override
    public int hashCode() {
        int result = super.hashCode();
		result = 31 * result + code.hashCode();
		result = 31 * result + date.hashCode();
        return result;
    }
	public FinancailStatement clone(){
		FinancailStatement newobj=new FinancailStatement();
			 newobj.code=this.code;
			 newobj.date=this.date;
			 newobj.pe=this.pe;
			 newobj.bvps=this.bvps;
			 newobj.cps=this.cps;
			 newobj.roe=this.roe;
			 newobj.jroe=this.jroe;
			 newobj.sgpr=this.sgpr;
			 newobj.smpr=this.smpr;
			 newobj.dtar=this.dtar;
			 newobj.opgr=this.opgr;
			 newobj.toi=this.toi;
			 newobj.toc=this.toc;
			 newobj.oi=this.oi;
			 newobj.oc=this.oc;
			 newobj.op=this.op;
			 newobj.tp=this.tp;
			 newobj.mp=this.mp;
			 newobj.mpbpc=this.mpbpc;
			 newobj.ta=this.ta;
			 newobj.tl=this.tl;
			 newobj.se=this.se;
			 newobj.tacf=this.tacf;
			 newobj.iacf=this.iacf;
			 newobj.facf=this.facf;
			 newobj.cnca=this.cnca;
		return newobj;
	}
	//endregion
}

