package com.kimi.stockanalysis.catcher.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务队列服务
 * 
 * @author kimi
 *
 */
public class TaskQueueService {
	private final Logger LOGGER = LoggerFactory.getLogger(TaskQueueService.class);
	
	//FIXME 考虑将任务落入数据库，避免停机引起的任务丢失
	private final Map<String, ArrayBlockingQueue<CatchTask>> queueMap = 
			new HashMap<String, ArrayBlockingQueue<CatchTask>>();

	private boolean validTask(CatchTask task){
		if(null == task){
			return false;
		}
		if(StringUtils.isBlank(task.getUrl()) || StringUtils.isBlank(task.getType())){
			return false;
		}
		return true;
	}
	
	/**
	 * 提交任务
	 * 
	 * @param task 任务实体
	 */
	public void commitTask(CatchTask task) {
		//任务校验
		assert(validTask(task));
		
		try {
			ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(task.getType());
			if (taskqueue != null) {
				taskqueue.put(task);
			} else {
				taskqueue = new ArrayBlockingQueue<CatchTask>(100000, true);
				taskqueue.put(task);
				queueMap.put(task.getType(), taskqueue);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.error("taskGenerate,异常信息{}", e.getMessage());
		}
	}

	/**
	 * 根据任务类型读取一条任务
	 * 
	 * @param key
	 * @return
	 */
	public CatchTask getTask(String key) {
		ArrayBlockingQueue<CatchTask> taskqueue = queueMap.get(key);
		if (taskqueue != null) {
			return taskqueue.poll();
		} else {
			return null;
		}
	}

	/**
	 * 获取当前任务类型集合，返回结果中排除了没有任务的类型
	 * 
	 * @return
	 */
	public Set<String> getKeySet() {
		Set<String> taskQueue = new HashSet<String>();
		for (String type : queueMap.keySet()) {
			if (queueMap.get(type).size() > 0) {
				taskQueue.add(type);
			}
		}
		return taskQueue;
	}
}
