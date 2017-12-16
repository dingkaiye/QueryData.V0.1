package com.ods.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ods.common.SerialNo;
import com.ods.db.DbDataLine;

/**
 * 信使, 信息的携带者, 用于数据库查询流程中线程间信息传递
 * @author ding_kaiye
 */

public class TxnMessager {
	
	private String serialNo = null ;  // 系统流水号
	
	private EsbMessageIn messageIn = null;  //接收到的报文
	private EsbMessageOut messageOut = null; //返回报文
	
	private String TxnId = null;        // 请求方请求调用的交易代号
	private boolean msgStatus = true ;  //消息是否有效
	private Object  headlerThread = null;      //处理本交易的 HeadlerThread 
	
	
	private Map<String, Object> inParm = null;        //入参
	private Map<String, Object> resultHead = null;      //数据库查询结果,表头数据
	private ArrayList<DbDataLine> resultList = null;    //数据库查询结果的数据
	private String returnCode = null;  // 返回码
	private String msg = null;         // 返回信息
	
	private int stepCnt = 0 ;   //步骤计数器
	private String currentStep = null;   //当前步骤
	private String currentStatus = null; //当前状态

	
	
	private ArrayList<TxnStep> trans = null; // 记录 步骤, 状态, 记录执行踪迹, 信息

	public TxnMessager(EsbMessageIn recvMessage, String serialNo, String TxnId ) {
		if (serialNo == null || "".equals(serialNo) ) {
			this.serialNo = SerialNo.getNextSerialNo() ;  // 获取系统流水号
		} else {
			this.serialNo = serialNo;
		}
		
		this.TxnId = TxnId;
		messageIn = recvMessage;    //ESB请求报文内容
		messageOut = new EsbMessageOut(); //返回ESB报文内容
		
		inParm = new HashMap<String, Object>();   //入参
		resultHead = new HashMap<String, Object>(); //返回结果 的表头
		resultList = new ArrayList<DbDataLine>(); //返回结果的数据
		
		//初始化参数
		msgStatus =  true;     //消息是否有效; 
		startTime=System.currentTimeMillis();  //开始时间戳,辅助判断交易超时(未启用)
		stepCnt = 0 ;          //步骤计数器
		currentStep = new String("initial");   //当前步骤
		currentStatus = new String("initial"); //当前状态
		msg = new String("");
		trans = new ArrayList<TxnStep>(); // 记录 步骤, 状态, 记录执行踪迹, 信息
	}


	public EsbMessageIn getMessageIn() {
		return messageIn;
	}


	public void setMessageIn(EsbMessageIn messageIn) {
		this.messageIn = messageIn;
	}


	public EsbMessageOut getMessageOut() {
		return messageOut;
	}


	public void setMessageOut(EsbMessageOut messageOut) {
		this.messageOut = messageOut;
	}


	/**
	 * @param currentStep
	 * @param currentStatus
	 * @param msg
	 */
	public void setcurrent(String currentStep, String currentStatus, String msg) {
		this.setCurrentStep(currentStep);
		this.setCurrentStatus(currentStatus);
		TxnStep txnStep = new TxnStep();
		stepCnt++;
		txnStep.setStepCnt(stepCnt);
		txnStep.setCurrentStep(currentStep);
		txnStep.setCurrentStatus(currentStatus);
		txnStep.setMsg(msg);
		txnStep.setCurrentStatus(currentStatus);
		trans.add(txnStep);
		this.msg = msg;
	}
	
	public void setcurrent(String currentStatus, String msg) {
		this.setCurrentStatus(currentStatus);
		TxnStep txnStep = new TxnStep();
		stepCnt++;
		txnStep.setStepCnt(stepCnt);
		txnStep.setCurrentStep(currentStep);
		txnStep.setCurrentStatus(currentStatus);
		txnStep.setMsg(msg);
		txnStep.setCurrentStatus(currentStatus);
		trans.add(txnStep);
		this.msg = msg;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public boolean getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(boolean msgStatus) {
		this.msgStatus = msgStatus;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	private void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}

	private void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	
	public String getTxnId() {
		return TxnId;
	}

	public void setTxnId(String TxnId) {
		// 此处是否需要记录日志
		this.TxnId = TxnId;
	}

	public Map<String, Object> getInParm() {
		return inParm;
	}

	public void setInParm(Map<String, Object> inParm) {
		this.inParm = inParm;
	}

	public String getCurrentStep() {
		return currentStep;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}
	
	public int getStepCnt() {
		return stepCnt;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<DbDataLine> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<DbDataLine> resultList) {
		this.resultList = resultList;
	}

	public ArrayList<TxnStep> getTrans() {
		return trans;
	}


	/**
	 * 解析成 HashMap 的请求报文, 方便通过键值对获取
	 * @return
	 */
//	public Map<String, Object> getMapReqMsg() {
//		return mapReqMsg;
//	}
//
//	
//	public Map<String, Object> getMapRspMsg() {
//		return mapRspMsg;
//	}

	/**
	 * 获取消息初始化时间
	 * @return
	 */
	public long getStartTime() {  
		return startTime;
	}


	private long startTime = 0; //开始时间戳,辅助判断交易超时
	public Map<String, Object> getResultHead() {
		return resultHead;
	}

	public void setResultHead(Map<String, Object> resultHead) {
		this.resultHead = resultHead;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}


	public Object getHeadlerThread() {
		return headlerThread;
	}

	public void setHeadlerThread(Object headlerThread) {
		this.headlerThread = headlerThread;
	}
	
	
	
}
