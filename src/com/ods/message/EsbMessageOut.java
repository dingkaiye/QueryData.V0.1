package com.ods.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

@XmlRootElement(name = "EsbMessage", namespace=NameSpace.ODS_URL)
public class EsbMessageOut extends EsbMessage {
	
	@XmlElement(namespace = NameSpace.ODS_URL )
	private SysHeadOut     SysHead  = null;
	@XmlElement(namespace = NameSpace.ODS_URL )
	private AppHeadOut     AppHead  = null;
	@XmlElement(namespace = NameSpace.ODS_URL )
	private LocalHead   LocalHead  = null;
	@XmlElement(namespace = NameSpace.ODS_URL )
	private Object Body = null;
	
	public SysHeadOut getSysHead() {
		return SysHead;
	}
	public void setSysHead(SysHeadOut sysHead) {
		SysHead = sysHead;
	}
	public AppHeadOut getAppHead() {
		return AppHead;
	}
	public void setAppHead(AppHeadOut appHead) {
		AppHead = appHead;
	}
	public LocalHead getLocalHead() {
		return LocalHead;
	}
	public void setLocalHead(LocalHead localHeadOut) {
		LocalHead = localHeadOut;
	}
	public Object getBody() {
		return Body;
	}
	public void setBody(Object body) {
		Body = body;
	}
	
}
