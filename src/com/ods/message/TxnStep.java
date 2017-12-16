package com.ods.message;

public class TxnStep {
	private int stepCnt = 0 ;
	private String currentStep = null;   //当前步骤
	private String currentStatus = null; //当前状态
	private String msg = null; //步骤信息
	
	
	public int getStepCnt() {
		return stepCnt;
	}
	public void setStepCnt(int stepCnt) {
		this.stepCnt = stepCnt;
	}
	public String getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
