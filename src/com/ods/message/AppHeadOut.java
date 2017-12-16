package com.ods.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

/**
 * @author ding_kaiye
 * @DATE 2017-09-14
 * 想要Esb报文中的AppHead
 */

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "AppHead", namespace=NameSpace.ODS_URL)
public class AppHeadOut {
	
	@XmlElement(name = "TxnTlrId", namespace = NameSpace.ODS_URL )
	public String TxnTlrId   = null ;  // 柜员号
	@XmlElement(name = "OrgId", namespace = NameSpace.ODS_URL )
	public String OrgId      = null ;  // 机构代码
	@XmlElement(name = "TlrPwsd", namespace = NameSpace.ODS_URL )
	public String TlrPwsd    = null ;  // 柜员密码
	@XmlElement(name = "TlrLvl", namespace = NameSpace.ODS_URL )
	public String TlrLvl     = null ;  // 柜员级别
	@XmlElement(name = "TlrTp", namespace = NameSpace.ODS_URL )
	public String TlrTp      = null ;  // 柜员类别
	@XmlElement(name = "AprvFlg", namespace = NameSpace.ODS_URL )
	public String AprvFlg    = null ;  // 复核标志
	@XmlElement(name = "AprvTlrInf", namespace = NameSpace.ODS_URL )
	public AprvTlrInf AprvTlrInf = new AprvTlrInf() ;  // 录入柜员数组
	@XmlElement(name = "AhrFlg", namespace = NameSpace.ODS_URL )
	public String AhrFlg     = null ;  // 授权标志
	@XmlElement(name = "AhrTlrInf", namespace = NameSpace.ODS_URL )
	public AhrTlrInf AhrTlrInf  = new AhrTlrInf() ;  // 授权柜员信息数组
	@XmlElement(name = "ScndFlg", namespace = NameSpace.ODS_URL )
	public String ScndFlg    = null ;  // 二次提交标志
	
}
