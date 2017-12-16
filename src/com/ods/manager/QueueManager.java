package com.ods.manager;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import org.apache.log4j.Logger;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.log.OdsLog;
import com.ods.message.TxnMessager;

/**
 * 操作系统队列
 * @author ding_kaiye 
 */
public class QueueManager {
	
	private static Logger thisLogger = OdsLog.getTxnLogger("QueueManager"); 
	
	//系统队列 Map
	private static Hashtable<String, BlockingQueue<TxnMessager>> sysQueueMap = new Hashtable<String, BlockingQueue<TxnMessager>>() ;
		
	protected static boolean QueueInit() {
		// 读取队列配置参数
		Properties QueueConfig = null;
		try {
			QueueConfig = Config.getPropertiesConfig(Constant.SysConfig);
		} catch (IOException e) {
			thisLogger.error("读取队列配置文件" + Constant.SysConfig + "失败");
			e.printStackTrace();
			return false;
		}

		String QueueList = QueueConfig.getProperty(Constant.QueueList).toString();

		String[] Queues = QueueList.split(",");
		// 读取配置初始化队列
		for (int i = 0; i < Queues.length; i++) {
			String QueueName = Queues[i];
			String QueueType = QueueConfig.getProperty(QueueName + "Type").toString();
			int TxnQueueSize = new Integer((String) QueueConfig.getProperty(QueueName + "Size"));
			BlockingQueue<TxnMessager> Queue = null;
			switch (QueueType) {
			case "ArrayBlockingQueue":
				Queue = new ArrayBlockingQueue<TxnMessager>(TxnQueueSize);
				break;
			case "LinkedBlockingDeque":
				Queue = new LinkedBlockingDeque<TxnMessager>(TxnQueueSize);
				break;
			case "LinkedBlockingQueue":
				Queue = new LinkedBlockingQueue<TxnMessager>(TxnQueueSize);
				break;
			case "PriorityBlockingQueue":
				Queue = new PriorityBlockingQueue<TxnMessager>(TxnQueueSize);
				break;
			case "SynchronousQueue":
				Queue = new SynchronousQueue<TxnMessager>();
				break;
			default:
				Queue = new PriorityBlockingQueue<TxnMessager>(TxnQueueSize);
				break;
			}
			thisLogger.info("初始化队列" + QueueName + "完成");
			sysQueueMap.put(QueueName, Queue);
		}
		return true;
	}
	
	
	/**
	 * 将指定交易加入到指定系统队列中并记录日志
	 * @param QueueName
	 * @param txnMessager
	 */
	public static boolean SysQueueAdd(String QueueName, TxnMessager txnMessager) {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		String txnSerialNo = txnMessager.getSerialNo() ;  // 获取系统流水号 
		//记录日志
		//thisLogger.info( txnSerialNo + "加入到队列" + QueueName + "开始");
		boolean result = Queue.add(txnMessager);
		thisLogger.info( txnSerialNo + "加入到队列" + QueueName + "完成,操作结果:" + result);
		return result;
	}
	
		
	/**
	 * 获取并移除此队列的头部，在指定的等待时间前等待可用的元素（如果有必要）。
	 * @param QueueName
	 * @param txnMessager 
	 */
	public static TxnMessager SysQueuePoll(String QueueName) {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		thisLogger.trace( "查找系统队列完成,开始获取 " + QueueName + "队列元素"); //记录日志
		TxnMessager txnMessager = Queue.poll();
		if (txnMessager != null) {
			String txnSerialNo = txnMessager.getSerialNo() ;  // 获取系统流水号 
			thisLogger.info( "获取 " + QueueName + "队列元素完成,获取元素的系统流水号:" + txnSerialNo);
		}else {
			thisLogger.trace( QueueName + "队列本次 poll 操作没有获取到元素"); //记录日志
		}
		return txnMessager;
	}
	
	/**
	 * 获取并移除此队列的头部，在指定的等待时间前等待可用的元素（如果有必要）。
	 * @param QueueName
	 * @param txnMessager 
	 * @throws InterruptedException 
	 */
	public static TxnMessager SysQueueTake(String QueueName) throws InterruptedException {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		thisLogger.trace( "查找系统队列完成,开始获取 " + QueueName + "队列元素"); //记录日志
		TxnMessager txnMessager = Queue.take();
		if (txnMessager != null) {
			String txnSerialNo = txnMessager.getSerialNo() ;  // 获取系统流水号 
			thisLogger.info( "获取 " + QueueName + "队列元素完成,获取元素的系统流水号:" + txnSerialNo);
		}else {
			thisLogger.trace( QueueName + "队列本次 take 操作没有获取到元素"); //记录日志
		}
		return txnMessager;
	}
	
	/**
	 * 如果此队列包含指定元素，则返回 true
	 * @param QueueName
	 * @param txnMessager 
	 */
	public static boolean SysQueueContains(String QueueName, TxnMessager txnMessager) {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		String txnSerialNo = txnMessager.getSerialNo() ;  // 获取系统流水号 
		thisLogger.trace( "查找系统队列完成,开始查找 " + QueueName + "队列是否包含该元素" + txnSerialNo); //记录日志
		boolean result = Queue.contains(txnMessager);
		thisLogger.trace( QueueName + "Contains " + txnSerialNo + "is " + result);
		return result;
	}
	
	
	/**
	 * 从此队列中移除指定元素的单个实例（如果存在）。
	 * @param QueueName
	 * @param txnMessager
	 */
	public static boolean SysQueueRemove(String QueueName, TxnMessager txnMessager) {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		String txnSerialNo = txnMessager.getSerialNo() ;  // 获取系统流水号 
		//记录日志
		thisLogger.trace( txnSerialNo + "移出" + QueueName + "开始");
		boolean result = Queue.remove(txnMessager);
		thisLogger.info( txnSerialNo + "移出" + QueueName + "完成" + result);
		return result;
	}
	
	/**
	 * 返回在无阻塞的理想情况下（不存在内存或资源约束）此队列能接受的附加元素数量；如果没有内部限制，则返回 Integer.MAX_VALUE
	 * @param QueueName
	 * @param txnMessager
	 */
	public static int SysQueueRemainingCapacity(String QueueName) {
		BlockingQueue<TxnMessager> Queue = SysQueueGet(QueueName);
		//记录日志
		int result = Queue.remainingCapacity();
		thisLogger.info("获取" + QueueName + "能接受的附加元素数量完成:" + result);
		return result;
	}

	
	/**
	 * 根据 队列名 从系统队列中获取对应的队列
	 * @param QueueName
	 * @return
	 */
	private static BlockingQueue<TxnMessager> SysQueueGet(String QueueName) {
		return sysQueueMap.get(QueueName);
	}
	
	
	/**
	 * 转入失败队列
	 */
	public static void moveToFailQueue(TxnMessager txnMessager) {
		txnMessager.setMsgStatus(false);
		QueueManager.SysQueueAdd(Constant.FailQueue, txnMessager);			//转入失败队列
	}


	public static void moveToFailQueue(TxnMessager txnMessager, String msg) {
		txnMessager.setMsgStatus(false);
		txnMessager.setcurrent("fail", msg);
		QueueManager.SysQueueAdd(Constant.FailQueue, txnMessager);	
	}
	

}
