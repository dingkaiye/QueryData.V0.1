package com.ods.manager;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.ods.common.Constant;
import com.ods.common.PackEsbFailMsg;
import com.ods.common.SerialNo;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.message.TxnMessager;

@WebService
public class Handler extends Thread {
	private static Logger logger = OdsLog.getTxnLogger("Handler");
	private static int timeOut = InitSystem.getTimeOut();  //系统级超时时间 
	
	public Handler (){
		
	}
	
	public String QueryODS(String message) {
		
		String recvMessage = message;
		String serialNo = SerialNo.getNextSerialNo();
		String rspMessage = "" ;
		try {
			TxnCntContrl.addCnt();
		} catch (TxnException e) {
			//返回系统繁忙
			logger.warn(serialNo + "系统繁忙");
			rspMessage = PackEsbFailMsg.packEsbFailMsg(recvMessage, serialNo, "F", "SysBusy", "系统繁忙");
			return rspMessage;
		}
		
		try {

			TxnMessager txnMessager = new TxnMessager(recvMessage, serialNo); 
			serialNo = txnMessager.getSerialNo();
			txnMessager.setHeadler(Thread.currentThread());
			
			//检查交易代号
			
			//加入解包队列 

			QueueManager.SysQueueAdd(Constant.UnpackQueue, txnMessager);
			boolean timeOutFlg = true;
			try {
				Thread.sleep(timeOut);
			} catch (InterruptedException e) {
				// 如果
				logger.info("wake UP");
				timeOutFlg = false;
				rspMessage = txnMessager.getRspMsg();
			}

			if (timeOutFlg && "".equals(rspMessage)) {
				logger.warn(serialNo + "交易超时");
				txnMessager.setMsgStatus(false); // 设置消息无效, 如果消息在队列中,
												 // 服务线程渠道后可直接丢弃
				rspMessage = PackEsbFailMsg.packEsbFailMsg(recvMessage, serialNo, "F", "TimeOut", "交易超时");
				return rspMessage;
			} else {
				logger.debug(serialNo + "交易正常返回" + rspMessage);
				return rspMessage;
			}

		} catch (Exception e) {
			rspMessage = PackEsbFailMsg.packEsbFailMsg(recvMessage,serialNo, "F", "TimeOut", "遇到系统错误,请重新查询");
			return rspMessage ;
			 // 返回交易失败报文
		} finally {
			logger.trace(serialNo + "已进入 finally 代码块");
			try {
				TxnCntContrl.reduceCnt();
			} catch (TxnException e) {
				logger.warn(serialNo + "登记交易挂出是出错,系统记录交易数<0");
			} 
		}
		
	}
	
}
