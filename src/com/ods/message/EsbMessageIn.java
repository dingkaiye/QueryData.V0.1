package com.ods.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;
import com.ods.ws.TxnBody;

@XmlRootElement(name = "EsbMessage", namespace=NameSpace.ODS_URL)
public class EsbMessageIn extends EsbMessage {
	
	@XmlElement(namespace = NameSpace.ODS_URL )
	SysHeadIn     SysHead  = new SysHeadIn();
	@XmlElement(namespace = NameSpace.ODS_URL )
	AppHeadIn     AppHead  = new AppHeadIn();
	@XmlElement(namespace = NameSpace.ODS_URL )
	LocalHead   LocalHead  = new LocalHead();
	@XmlElement(namespace = NameSpace.ODS_URL )
	TxnBody Body = null;
	
	public EsbMessageIn (SysHeadIn SysHead, AppHeadIn AppHead, LocalHead LocalHead, TxnBody Body ) {
		this.SysHead = SysHead;
		this.AppHead = AppHead;
		this.LocalHead = LocalHead;
		this.Body = Body;
	}
	
	public SysHeadIn getSysHead() {
		return SysHead;
	}
	public void setSysHead(SysHeadIn sysHead) {
		SysHead = sysHead;
	}
	public AppHeadIn getAppHead() {
		return AppHead;
	}
	public void setAppHead(AppHeadIn appHead) {
		AppHead = appHead;
	}
	public LocalHead getLocalHead() {
		return LocalHead;
	}
	public void setLocalHead(LocalHead localHead) {
		LocalHead = localHead;
	}
	public TxnBody getBody() {
		return Body;
	}
	public void setBody(TxnBody body) {
		Body = body;
	}

	
}
