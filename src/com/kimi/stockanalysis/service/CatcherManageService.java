/**
 * 
 */
package com.kimi.stockanalysis.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kimi.stockanalysis.catcher.BaseCatcher;
import com.kimi.stockanalysis.catcher.FinancailStatementCatcher;
import com.kimi.stockanalysis.catcher.StockInfoCatcher;
import com.kimi.stockanalysis.catcher.StockInfoDetailCatcher;
import com.kimi.stockanalysis.catcher.StockRealtimePriceCatcher;

/**
 * 爬虫管理器
 * 
 * @author kimi
 *
 */
public class CatcherManageService {
	public static final Logger LOGGER = LoggerFactory.getLogger(CatcherManageService.class);
	@Autowired
	private StockInfoCatcher stockInfoCatcher;
	@Autowired
	private StockInfoDetailCatcher stockInfoDetailCatcher;
	@Autowired
	private FinancailStatementCatcher financailStatementCatcher;
	@Autowired
	private StockRealtimePriceCatcher stockRealtimePriceCatcher;

	private Map<String, BaseCatcher> catcherMap = new HashMap<String, BaseCatcher>();

	@PostConstruct
	public void postContruct() {
		// 爬虫注册
		catcherMap.put(stockInfoCatcher.getTaskkey(), stockInfoCatcher);
		catcherMap.put(stockInfoDetailCatcher.getTaskkey(), stockInfoDetailCatcher);
		catcherMap.put(financailStatementCatcher.getTaskkey(), financailStatementCatcher);
		catcherMap.put(stockRealtimePriceCatcher.getTaskkey(), stockRealtimePriceCatcher);

		// 启用爬虫监控
		catcherMonitor();
	}

	/**
	 * 启动已注册的爬虫
	 * 
	 * @author kimi
	 */
	public void startCatcher() {
		for (String key : catcherMap.keySet()) {
			BaseCatcher catcher = catcherMap.get(key);
			if (!catcher.isRunning()) {
				catcher.start();
			}
		}
	}

	/**
	 * 爬虫监控,每隔五分钟按任务类型读取任务队列，唤醒/重启对应的爬虫
	 */
	public void catcherMonitor() {
		Thread thread = new Thread("catcherMonitor") {
			@Override
			public void run() {
				while (true) {
					catcherStateCheck();
					try {
						Thread.sleep(5*60*1000L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}

	/**
	 * 唤醒或者重启爬虫任务
	 */
	public void catcherStateCheck(){
		for(String key:TaskQueueService.getKeySet()){
			startCatcher(key);
		}
	}
	
	/**
	 * 启动已注册的选定key的爬虫
	 * 
	 * @author kimi
	 */
	public void startCatcher(String key) {
		BaseCatcher catcher = catcherMap.get(key);
		if (null != catcher && !catcher.isRunning()) {
			LOGGER.info("{}:爬虫重新启动",key);
			catcher.start();
		}
	}
}
