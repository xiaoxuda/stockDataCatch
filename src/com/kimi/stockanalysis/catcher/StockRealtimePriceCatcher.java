/**
 * 
 */
package com.kimi.stockanalysis.catcher;

import org.apache.commons.lang.StringUtils;

import com.kimi.stockanalysis.entity.StockInfo;
import com.kimi.stockanalysis.enums.TaskTypeEnum;
import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.StockInfoService;

/**
 * @author kimi
 *
 */
public class StockRealtimePriceCatcher extends BaseCatcher {
	private StockInfoService stockInfoService;

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}

	@Override
	public String getTaskkey() {
		return TaskTypeEnum.SINAJS_PRICE;
	}

	/**
	 * 抓取股票实时价格
	 * 
	 * @author kimi
	 * @see com.kimi.stockanalysis.catcher.BaseCatcher#extract(java.lang.String,
	 *      com.kimi.stockanalysis.service.CatchTask)
	 */
	@Override
	protected boolean extract(String src, CatchTask task) {
		if (StringUtils.isBlank(src)) {
			LOGGER.error("{}:{},返回结果为空", task.getType(), task.getInfoValue("code"));
			return false;
		}

		String arr[] = src.split(",");

		if (arr.length < 4) {
			LOGGER.error("{}:{},当前价格获取失败", task.getType(), task.getInfoValue("code"));
			return false;
		}

		StockInfo stockInfo = stockInfoService.selectOne(task.getInfoValue("code").toString());

		String price = StringUtils.isBlank(arr[3]) || Float.valueOf(arr[3]) == 0 ? arr[2] : arr[3];

		stockInfo.setPrice(Float.valueOf(price));
		stockInfoService.updateSelective(stockInfo);

		return false;
	}

}
