package com.kimi.stockanalysis.catcher.enums;

public enum TaskTypeEnum {
	/**抓取财务报表**/
	EASTMONEYNET_STATEMENT("eastmoney_statement","财务报表",CycleEnum.QUARTER.getCode()),
	
	/**抓取上市公司列表**/
	JUCAONET_COMPANY_LIST("jucaonet_company_list","股票列表",CycleEnum.WEEK.getCode()),
	
	/**获取公司股本数量**/
	JUCAONET_COMPANY_SHARECAPITAL("jucaonet_company_sharecapital","股本",CycleEnum.WEEK.getCode()),
	
	/**获取股票当前交易信息**/
	SINAJS_PRICE("sinajs_price","股票实时价格",CycleEnum.DAY.getCode()),
	
	/**获取股票历史交易信息**/
	SINAJS_HISTORY_TRADE_DETAIL("sinajs_history_trade_detail","股票历史交易信息",CycleEnum.MONTH.getCode());
	
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
