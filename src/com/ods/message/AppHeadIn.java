package com.ods.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ods.common.NameSpace;

@XmlRootElement(name = "AppHead", namespace=NameSpace.ODS_URL)
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(propOrder={"TxnTlrId","OrgId","TlrPwsd", "TlrLvl","TlrTp","AprvFlg","AprvTlrInf","AhrFlg","AhrTlrInf","ScndFlg" })  
public class AppHeadIn {
	
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TxnTlrId   = null ;   // 柜员号
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String OrgId      = null ;   // 机构代码
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TlrPwsd    = null ;   // 柜员密码
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TlrLvl     = null ;   // 柜员级别
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TlrTp      = null ;   // 柜员类别
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AprvFlg    = null ;   // 复核标志
	@XmlElement(namespace = NameSpace.ODS_URL )
	public AprvTlrInf AprvTlrInf = new AprvTlrInf() ;   // 录入柜员数组
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AhrFlg     = null ;   // 授权标志
	@XmlElement(namespace = NameSpace.ODS_URL )
	public AhrTlrInf AhrTlrInf = new AhrTlrInf(); 
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String ScndFlg    = null ;   // 二次提交标志
	

}
