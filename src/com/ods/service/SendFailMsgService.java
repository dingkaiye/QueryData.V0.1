package com.ods.service;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.log.OdsLog;
import com.ods.manager.QueueManager;
import com.ods.message.EsbMessageIn;
import com.ods.message.EsbMessageOut;
import com.ods.message.EsbMessage;
import com.ods.message.TxnMessager;

public class SendFailMsgService extends Thread {
	
	/**
	 * 处理失败的交易, 组织失败报文并返回
	 * @author ding_kaiye
	 * @ 2017-09-27
	 */
		
	private static Logger logger = OdsLog.getTxnLogger("SendService");

	/** 成功处理后, 转入的队列名称(默认值) */
	private String nextQueue = null; //默认
	
	private int sleeptime = 50;

	Properties properties = null;
	String QueueName = null;
	final String confile = Constant.ESBPACK_CONFIG_FILE; // 请求报文结构配置文件

    /**
    * @param QueueName 输入队列名称
    * @param nextQueueName 输出队列名称
    * @throws IOException
    */
	public SendFailMsgService(String QueueName, String nextQueueName) throws IOException {
		this.QueueName = QueueName;
		if (nextQueueName != null && ! "".equals(nextQueueName)) {
			this.nextQueue = nextQueueName;
		}
		logger.info("服务初始化完成, 输入队列:[" + this.QueueName + "] 输出队列:[" + this.nextQueue + "]");
		
	}

	@Override
	public void run() {
		String SerialNo = null;
		TxnMessager txnMessager = null;
	
		while (true) {
			try { //服务不退出
				try { // 从队列中获取 txnMessager
					txnMessager = QueueManager.SysQueuePoll(QueueName);
				} catch (Exception e) {
					logger.error("此次轮询" + QueueName + "出现异常, 稍后再次获取:" + e.getMessage());
					try {
						Thread.sleep(sleeptime);
					} catch (InterruptedException e1) {
						logger.warn("Thread.sleep InterruptedException");
					}
				}

				if (null != txnMessager) {
					try {
						// 取得通讯方式 同步, 异步
						// 组失败报文 
						EsbMessageIn esbMessage = null;
						EsbMessage reqMessage = txnMessager.getMessageIn(); 
						if (reqMessage instanceof EsbMessageIn ){
							esbMessage = (EsbMessageIn) reqMessage;
						
						SerialNo = txnMessager.getSerialNo();
						String txnSt = "F";  // F－系统处理失败
						String RetCd = txnMessager.getReturnCode() ;
						String retMsg = txnMessager.getMsg();
						EsbMessageOut rspMessage = PackEsbHead.packEsbFailMsg(esbMessage.getSysHead(), SerialNo, txnSt, RetCd, retMsg);
						logger.debug(SerialNo + " 组失败包完成");
						txnMessager.setMessageOut(rspMessage);
						
						// 同步方式, 取得TxnMessage中记录的线程, 中断线程 sleep
						// 先实现同步方式 相应, 后期开发异步相应方式
						Thread headler = (Thread) txnMessager.getHeadlerThread();
						headler.interrupt(); // 中断 headler的等待
						}
						// 异步方式, 放入 对应的 Gate 队列

						continue;

					} catch (Exception e) {
						logger.error("交易处理出现异常[" + SerialNo + "]" + e.getMessage());
						//QueueManager.moveToFailQueue(txnMessager);
					}
				} else {
					try {
						logger.trace("此次轮询" + QueueName + "未获得待处理交易,稍后再次获取");
						Thread.sleep(sleeptime);
					} catch (InterruptedException e) {
						logger.debug("sleep has be Interrupted", e);
					}
				}

			} catch (Exception e) {
				logger.error("交易处理出现异常[" + SerialNo + "]" + e.getMessage());
				//QueueManager.moveToFailQueue(txnMessager);
			}

		}
	}

}
