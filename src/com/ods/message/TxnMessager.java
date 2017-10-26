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
	
	private String TxnId = null;      // 请求方请求调用的交易代号
	private boolean msgStatus = true ;  //消息是否有效
	private Object Headler = null; //处理本交易的 HeadlerThread 
	
	private String reqMsg = null;  //ESB请求报文内容
	private String rspMsg = null;  //返回ESB报文内容
	private Map<String, Object> mapReqMsg = null; //解析后的报文
	private Map<String, Object> mapRspMsg = null; //组装前的报文
	
	private Map<String, Object> inParm = null;   //入参
	private Map<String, Object> resultHead = null;  //数据库查询结果,表头数据
	private ArrayList<DbDataLine> resultList = null;    //数据库查询结果的数据
	private String returnCode = null;  // 返回码
	private String msg = null;         // 返回信息
	// 解析后的报文头信息 
	//AppHeadIn  appHeadIn;
	//AppHeadOut appHeadOut;
	//SysHeadIn  sysHeadIn; 
	//SysHeadOut sysHeadOut;
	
	private int stepCnt = 0 ;   //步骤计数器
	private String currentStep = null;   //当前步骤
	private String currentStatus = null; //当前状态
	
	
	private ArrayList<TxnStep> trans = null; // 记录 步骤, 状态, 记录执行踪迹, 信息

	public TxnMessager(String recvMessage, String serialNo) {
		if (serialNo == null || "".equals(serialNo) ) {
			this.serialNo = SerialNo.getNextSerialNo() ;  // 获取系统流水号
		} else {
			this.serialNo = serialNo;
		}
		
		TxnId = new String();
		reqMsg = recvMessage;  //ESB请求报文内容
		rspMsg = new String(); //返回ESB报文内容
		
		mapReqMsg = new HashMap<String, Object>() ; //解析后的报文
		mapRspMsg = new HashMap<String, Object>() ; //组装前的报文
		
		inParm = new HashMap<String, Object>();   //入参
		resultHead = new HashMap<String, Object>(); //返回结果 的表头
		resultList = new ArrayList<DbDataLine>(); //返回结果的数据
		
		//初始化参数
		msgStatus =  true;     //消息是否有效; 
		startTime=System.currentTimeMillis();  //开始时间戳,辅助判断交易超时(未启用)
		stepCnt = 0 ;    //步骤计数器
		currentStep = new String("initial");   //当前步骤
		currentStatus = new String("initial"); //当前状态
		msg = new String("");
		trans = new ArrayList<TxnStep>(); // 记录 步骤, 状态, 记录执行踪迹, 信息
	}


	public String getRequestMsg() {
		return reqMsg;
	}

	public void setRequestMsg(String requestMsg) {
		this.reqMsg = requestMsg;
	}

	public String getResponseMsg() {
		return rspMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.rspMsg = responseMsg;
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

	public String getReqMsg() {
		return reqMsg;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}


	/**
	 * 解析成 HashMap 的请求报文, 方便通过键值对获取
	 * @return
	 */
	public Map<String, Object> getMapReqMsg() {
		return mapReqMsg;
	}

	
	public Map<String, Object> getMapRspMsg() {
		return mapRspMsg;
	}

	/**
	 * 获取消息初始化时间
	 * @return
	 */
	public long getStartTime() {  
		return startTime;
	}

	public Object getHeadler() {
		return Headler;
	}

	public void setHeadler(Object handler) {
		Headler = handler;
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
	
	
	
}
