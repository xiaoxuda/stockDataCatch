package com.kimi.stockanalysis.catcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.TaskQueueService;

/*
 * @author kimi
 */
public abstract class BaseCatcher {
	/** 为每个子类提供一个区别化的日志类 **/
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	private boolean isRunning = false; // 标识任务正在运行
	private int defaultWaitTime = 1000; // 默认抓取间隔
	private int maxWaitTime = 2 * 60 * 1000; // 最大抓取间隔2分钟
	private int waitTime = 1000; // 当前抓取间隔
	private int waitMultiplier = 2;//抓取时间乘数
	private int againTime = 5; // 异常重试次数

	/**
	 * 数据提取及保存逻辑，需要爬虫具体实现
	 * 
	 * @param src 数据文本
	 * @param task 任务信息
	 * @return
	 */
	protected abstract boolean extract(String src, CatchTask task);

	/**
	 * 返回任务关键字，不能为空，需要爬虫具体实现
	 * 
	 * @author kimi
	 * @return 返回值不能为空
	 */
	protected abstract String getTaskkey();

	/**
	 * 判断爬虫是否处于运行中
	 * @return
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * 启动爬虫,定时请求数据抓取任务
	 */
	public void start() {
		if (this.isRunning) {
			return;
		}
		Thread thread_catcher = new Thread(getTaskkey()+"_Catcher") {
			public void run() {
				while (true) {
					CatchTask task = TaskQueueService.getTask(getTaskkey());
					try {
						if (task == null) {// 当前没有任务，将抓取间隔调高waitMultiplier倍让出CPU资源
							LOGGER.info("{}:no task", getTaskkey());
							
							if (waitTime == maxWaitTime) {
								LOGGER.info("{}:等待任务时间超时，爬虫退出，等待重新唤起。", getTaskkey());
								break;
							}
							waitTime *= waitMultiplier;
							waitTime = waitTime > maxWaitTime ? maxWaitTime : waitTime;
						} else {// 当前任务列表有任务，恢复抓取间隔为默认值
							waitTime = defaultWaitTime;
							catchAction(task, againTime);
						}
						Thread.sleep(waitTime);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LOGGER.error("任务{},异常信息{}", task, e.getMessage());
					}
				}
				isRunning = false;
			}
		};
		LOGGER.info("{}:start", getTaskkey()+"_Catcher");
		thread_catcher.start();
		this.isRunning = true;
	}

	/**
	 * 根据任务抓取远程数据
	 * 
	 * @param task
	 *            任务信息
	 * @param againTime
	 *            失败重试次数
	 * @return
	 */
	public String catchAction(CatchTask task, int againTime) {
		try {
			URL url = new URL(task.getUrl());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream inputStream = con.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuilder builder = new StringBuilder();
			// 将html文档格式化为单行文本
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			extract(builder.toString(), task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("任务{},异常信息{}", task, e.getMessage());
			// 异常重试
			if (againTime > 0) {
				catchAction(task, againTime - 1);
			}else{
				LOGGER.info("任务重试超过{}次，重新放回任务队列等待调度，参数{}", againTime, task);
			}
		}
		return null;
	}
}
