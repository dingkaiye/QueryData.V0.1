package com.ods.manager;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.ods.common.Constant;
import com.ods.common.SerialNo;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.message.AppHeadIn;
import com.ods.message.EsbMessageIn;
import com.ods.message.EsbMessageOut;
import com.ods.message.LocalHead;
import com.ods.message.SysHeadIn;
import com.ods.message.TxnMessager;
import com.ods.service.PackEsbHead;
import com.ods.transaction.DepositTrans.Body.ReqBody;

public class Handler {
	private static Logger logger = OdsLog.getTxnLogger("Handler");
	
	public static EsbMessageOut QueryOdsData(SysHeadIn sysHeadIn, AppHeadIn appHeadIn, LocalHead localHead, ReqBody reqBody )  {
		
		int timeOut = 60; // 超时时间, 默认60s
		
		String serialNo = SerialNo.getNextSerialNo();
		String txnName = sysHeadIn.getSvcSplrTxnCd();
		
		EsbMessageOut esbMessage = null;
//		DepositTransDetailRsp rspMessage = null;  // 返回报文 
		TxnMessager txnMessager = null;
		 
		StringWriter xmlStringWriter = null;
		JAXBContext jc = null ;
		Marshaller marshaller = null;
		
		// 获取交易超时时间 
		timeOut = TxnConfigManager.getTxnTimeout(txnName);
		if(timeOut == -1){
			esbMessage = PackEsbHead.packEsbFailMsg(sysHeadIn, serialNo, "F", "Error", "交易不存在");
//			rspMessage = new DepositTransDetailRsp(esbMessage);
			return esbMessage;
		}
		
		logger.info(serialNo + " 开始交付系统处理,报文内容:");
		
		
		try {
			xmlStringWriter = new StringWriter();
			jc = JAXBContext.newInstance(SysHeadIn.class);
			marshaller = jc.createMarshaller();
			marshaller.marshal(sysHeadIn, xmlStringWriter);
			String xmlSysHeadIn = xmlStringWriter.toString();
			logger.info(serialNo + ":SysHeadIn" + xmlSysHeadIn);
			xmlStringWriter = null;

			xmlStringWriter = new StringWriter();
			jc = JAXBContext.newInstance(AppHeadIn.class);

			marshaller = jc.createMarshaller();
			marshaller.marshal(appHeadIn, xmlStringWriter);
			String xmlAppHeadIn = xmlStringWriter.toString();
			logger.info(serialNo + ":AppHeadIn" + xmlAppHeadIn);
			xmlStringWriter = null;

			xmlStringWriter = new StringWriter();
			jc = JAXBContext.newInstance(LocalHead.class);
			marshaller = jc.createMarshaller();
			marshaller.marshal(localHead, xmlStringWriter);
			String xmlLocalHead = xmlStringWriter.toString();
			logger.info(serialNo + ":LocalHead" + xmlLocalHead);
			xmlStringWriter = null;

			xmlStringWriter = new StringWriter();
			jc = JAXBContext.newInstance(reqBody.getClass());
			marshaller = jc.createMarshaller();
			marshaller.marshal(reqBody, xmlStringWriter);
			String xmlBody = xmlStringWriter.toString();
			logger.info(serialNo + ":Body" + xmlBody);
			xmlStringWriter = null;
		} catch (JAXBException e) {
			logger.error(serialNo + "打印报文出错" , e);
			esbMessage = PackEsbHead.packEsbFailMsg(sysHeadIn, serialNo, "F", "SysBusy", "系统繁忙,请稍候再试");
//			rspMessage = new DepositTransDetailRsp(esbMessage);
			return esbMessage;
		}
	    
		EsbMessageIn recvMessage = new EsbMessageIn(sysHeadIn, appHeadIn, localHead, reqBody);
	    
		try {
			TxnCntContrl.addCnt();
		} catch (TxnException e) {
			// 返回系统繁忙
			logger.warn(serialNo + "系统繁忙");
			esbMessage = PackEsbHead.packEsbFailMsg(sysHeadIn, serialNo, "F", "SysBusy", "系统繁忙,请稍候再试");
//			rspMessage = new DepositTransDetailRsp(esbMessage);
			return esbMessage;
		}

		try {

			txnMessager = new TxnMessager(recvMessage, serialNo, sysHeadIn.getSvcSplrTxnCd());
			txnMessager.setHeadlerThread(Thread.currentThread());

			// 检查交易代号

			// 放入交易队列
			QueueManager.SysQueueAdd(Constant.TxnQueue, txnMessager);
			boolean timeOutFlg = true;
			try {
				Thread.sleep(timeOut);
			} catch (InterruptedException e) {
				logger.debug(serialNo + " 已接收到系统返回信息");
				timeOutFlg = false;
				esbMessage = txnMessager.getMessageOut();
//				rspMessage = new DepositTransDetailRsp(esbMessage);
			}

			if (timeOutFlg) {
				logger.warn(serialNo + "交易超时");
				txnMessager.setMsgStatus(false); // 设置消息无效, 如果消息在队列中, 服务线程取到后可直接丢弃
				esbMessage = PackEsbHead.packEsbFailMsg(sysHeadIn, serialNo, "F", "TimeOut", "交易超时,请稍候再试");
//				rspMessage = new DepositTransDetailRsp(esbMessage);
			} else {

				logger.info(serialNo + "交易未超时");
			}
			
//			xmlStringWriter = new StringWriter();
//			jc = JAXBContext.newInstance(esbMessage.getClass());
//			marshaller = jc.createMarshaller();
//			marshaller.marshal(esbMessage, xmlStringWriter);
//			String xmlBody = xmlStringWriter.toString();
//			xmlStringWriter = null;
//			logger.info(serialNo + "返回数据:" + xmlBody);
//			rspMessage = new DepositTransDetailRsp(esbMessage);
			return esbMessage;
		} catch (Exception e) {
			esbMessage = PackEsbHead.packEsbFailMsg(sysHeadIn, serialNo, "F", "SysError", "遇到系统错误,请重新查询");
//			rspMessage = new DepositTransDetailRsp(esbMessage);
			return esbMessage;
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
