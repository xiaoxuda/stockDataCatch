package com.kimi.stockanalysis.enums;

public enum TaskTypeEnum {
	EASTMONEYNET_STATEMENT("eastmoney_statement","财务报表",CycleEnum.WEEK.getCode()),
	JUCAONET_COMPANY_LIST("jucaonet_company_list","股票列表",CycleEnum.WEEK.getCode()),
	JUCAONET_COMPANY_SHARECAPITAL("jucaonet_company_sharecapital","股本",CycleEnum.WEEK.getCode()),
	SINAJS_PRICE("sinajs_price","股票实时价格",CycleEnum.DAY.getCode());
	
	/** 任务代码 **/
	private String code;
	/** 任务描述 **/
	private String desc;
	/** 调度周期类型 **/
	private String cycle;
	
	TaskTypeEnum(String code, String desc, String cycle){
		this.code = code;
		this.desc = desc;
		this.cycle=cycle;
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
	 * @return the cycle
	 */
	public String getCycle() {
		return cycle;
	}
	
	
}
