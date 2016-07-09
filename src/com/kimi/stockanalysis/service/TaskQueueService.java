package com.kimi.stockanalysis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
/**
 * 任务队列服务
 * @author kimi
 *
 */
public class TaskQueueService {
	private static Map<String,ArrayBlockingQueue<CatchTask>> queueMap = new 
			HashMap<String,ArrayBlockingQueue<CatchTask>>();
	
	/**
	 * 提交任务
	 * @param task 任务实体
	 */
	public static void commitTask(CatchTask task){
		try {
			ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(task.getType());
			if(taskqueue!=null){
				taskqueue.put(task);
			}else{
				taskqueue = new ArrayBlockingQueue<CatchTask>(100000, true);
				taskqueue.put(task);
				queueMap.put(task.getType(), taskqueue);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据任务类型读取一条任务
	 * @param key
	 * @return
	 */
	public static CatchTask getTask(String key){
		ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
		if(taskqueue!=null){
			return taskqueue.poll();
		}else{
			return null;
		}
	}
	
	/**
	 * 获取当前任务类型集合
	 * @return
	 */
	public static Set<String> getKeySet(){
		return queueMap.keySet();
	}
}
