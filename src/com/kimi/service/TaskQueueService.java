package com.kimi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * @see 任务队列，大型爬虫系统中，任务队列需要作为一个独立项目部署在单独服务器中，并用分布式锁保证分布式爬虫系统的正常工作 
 * @author kimi
 *
 */
public class TaskQueueService {
	public static Map<String,LinkedBlockingQueue<CatchTask>> queueMap = new HashMap<String,LinkedBlockingQueue<CatchTask>>();
	public static void commitTask(String key,CatchTask task){
		try {
			LinkedBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
			if(taskqueue!=null){
				taskqueue.put(task);
			}else{
				taskqueue = new LinkedBlockingQueue<CatchTask>(100000);
				taskqueue.put(task);
				queueMap.put(key, taskqueue);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static CatchTask getTask(String key){
		LinkedBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
		if(taskqueue!=null){
			return taskqueue.poll();
		}else{
			return null;
		}
	}
}
