package com.ods.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

/**
 * 录入柜员数组
 * @author ding_kaiye
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "AprvTlrInf", namespace=NameSpace.ODS_URL)
public class AprvTlrInf {

	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AprvTlrId  = null; //  交易录入柜员标识 
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AprvOrgId  = null; //  交易录入机构代码
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AprvTlrLvl = null; //  交易录入柜员级别
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AprvTlrTp  = null; //  交易录入柜员类别
	
}
