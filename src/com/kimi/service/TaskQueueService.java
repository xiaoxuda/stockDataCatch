package com.kimi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class TaskQueueService {
	public static Map<String,ArrayBlockingQueue<CatchTask>> queueMap = new HashMap<String,ArrayBlockingQueue<CatchTask>>();
	public static void commitTask(String key,CatchTask task){
		try {
			ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
			if(taskqueue!=null){
				taskqueue.put(task);
			}else{
				taskqueue = new ArrayBlockingQueue<CatchTask>(100000, true);
				taskqueue.put(task);
				queueMap.put(key, taskqueue);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static CatchTask getTask(String key){
		ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
		if(taskqueue!=null){
			return taskqueue.poll();
		}else{
			return null;
		}
	}
}
