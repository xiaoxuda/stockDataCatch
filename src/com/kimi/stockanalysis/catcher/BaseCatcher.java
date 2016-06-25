package com.kimi.stockanalysis.catcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import com.kimi.stockanalysis.service.CatchTask;
import com.kimi.stockanalysis.service.TaskQueueService;
/*
 * @author kimi
 */
public abstract class BaseCatcher {
	private boolean isRunning = false;	//标识任务正在运行
	private int defaultWaitTime=1000;	//默认抓取间隔
	private int maxWaitTime=60*60*1000;	//最大抓取间隔
	private int waitTime=1000;			//当前抓取间隔
	protected String key;				//任务关键字
	private int againTime=5;			//异常重试次数
	/**
	 * 数据提取及保存逻辑，需要爬虫具体实现
	 * @param src	数据文本
	 * @param task	任务信息
	 * @return
	 */
	protected abstract boolean extract(String src,CatchTask task);
	/**
	 * 启动爬虫,定时请求数据抓取任务
	 */
	public void start(){
		if(isRunning){
			return;
		}
		Thread thread_catcher=new Thread(this.key){
			public void run(){
				while(true){
					CatchTask task=TaskQueueService.getTask(key);
					try {
						if(task==null){//当前没有任务，将抓取间隔调高10倍让出CPU资源
							System.out.println(key+":no task "+(new Date()).toString());
							waitTime*=10;
							waitTime=waitTime>maxWaitTime?maxWaitTime:waitTime;
						}else{//当前任务列表有任务，恢复抓取间隔为默认值
							waitTime=defaultWaitTime;
							catchAction(task,againTime);
						}
						Thread.sleep(waitTime);
					}  catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if(task!=null){
							
						}
					}
				}
			}
		};
		System.out.println(key+":start at "+(new Date()).toString());
		thread_catcher.start();
	}
	/**
	 * 根据任务抓取远程数据
	 * @param task	任务信息
	 * @param againTime	失败重试次数
	 * @return
	 */
	public String catchAction(CatchTask task,int againTime){
		try {
			URL url=new URL(task.getUrl());
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			InputStream inputStream=con.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuilder builder=new StringBuilder();
			//将html文档格式化为单行文本
			String line=null;
			while((line=reader.readLine())!=null){
				builder.append(line);
			}
			extract(builder.toString(),task);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//异常重试
			if(againTime>0){
				catchAction(task,againTime-1);
			}
		}
		return null;
	}
}
