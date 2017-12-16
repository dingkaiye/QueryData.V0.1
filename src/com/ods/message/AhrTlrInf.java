package com.ods.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ods.common.NameSpace;

/**
 * 授权柜员信息数组
 * @author ding_kaiye
 *
 */

@XmlRootElement(name = "AhrTlrInf", namespace=NameSpace.ODS_URL)
@XmlType(propOrder = {"ahrTlrId", "ahrOrgId", "ahrTlrPswd","ahrTlrLvl","ahrTlrTp"} )
public class AhrTlrInf {

	
	private String AhrTlrId   = null ;  //  授权柜员标识
	private String AhrOrgId   = null ;  //  授权机构代码
	private String AhrTlrPswd = null ;  //  授权柜员密码
	private String AhrTlrLvl  = null ;  //  授权柜员级别
	private String AhrTlrTp   = null ;  //  授权柜员类别
	

	public String getAhrTlrId() {
		return AhrTlrId;
	}
	
	public String getAhrOrgId() {
		return AhrOrgId;
	}
	
	public String getAhrTlrPswd() {
		return AhrTlrPswd;
	}
	
	public String getAhrTlrLvl() {
		return AhrTlrLvl;
	}
	
	public String getAhrTlrTp() {
		return AhrTlrTp;
	}
	
	@XmlElement(name="AhrTlrId", namespace = NameSpace.ODS_URL )
	public void setAhrTlrId(String ahrTlrId) {
		AhrTlrId = ahrTlrId;
	}
	@XmlElement(name="AhrOrgId", namespace = NameSpace.ODS_URL )
	public void setAhrOrgId(String ahrOrgId) {
		AhrOrgId = ahrOrgId;
	}
	@XmlElement(name="AhrTlrPswd", namespace = NameSpace.ODS_URL )
	public void setAhrTlrPswd(String ahrTlrPswd) {
		AhrTlrPswd = ahrTlrPswd;
	}
	@XmlElement(name="AhrTlrLvl", namespace = NameSpace.ODS_URL )
	public void setAhrTlrLvl(String ahrTlrLvl) {
		AhrTlrLvl = ahrTlrLvl;
	}
	@XmlElement(name="AhrTlrTp", namespace = NameSpace.ODS_URL )
	public void setAhrTlrTp(String ahrTlrTp) {
		AhrTlrTp = ahrTlrTp;
	}  //  授权柜员类别
	
}
