
package com.ods.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

@XmlRootElement(name="SysHeadIn", namespace = NameSpace.ODS_URL )  
@XmlAccessorType(XmlAccessType.FIELD)  
public  class  SysHeadIn {

	
	@XmlElement(name = "SvcId", namespace = NameSpace.ODS_URL )
	private String SvcId = null  ;  // 服务代码
	@XmlElement(name = "SvcScn", namespace = NameSpace.ODS_URL )
	private String SvcScn         = null;  // 服务场景         
	@XmlElement(name="SvcSplrTxnCd", namespace = NameSpace.ODS_URL )
	private String SvcSplrTxnCd   = null;  // 服务提供方交易代码    
	@XmlElement(name="SvcCstTxnCd", namespace = NameSpace.ODS_URL )
	private String SvcCstTxnCd    = null;  // 调用方交易代码        
	@XmlElement(namespace = NameSpace.ODS_URL )
	private String MAC            = null;  // MAC字段               
	
	@XmlElement(name="CnsmrSysId", namespace = NameSpace.ODS_URL )
	private String CnsmrSysId     = null;  // 服务调用方系统编号    
	@XmlElement(name="TxnDt", namespace = NameSpace.ODS_URL )
	private String TxnDt          = null;  // 交易日期              
	@XmlElement(name="TxnTm", namespace = NameSpace.ODS_URL )
	private String TxnTm          = null;  // 交易时间              
	@XmlElement(name="AcgDt", namespace = NameSpace.ODS_URL )
	private String AcgDt          = null;  // 会计日期              
	@XmlElement(name="CnsmrSeqNo", namespace = NameSpace.ODS_URL )
	private String CnsmrSeqNo     = null;  // 服务调用方系统流水号  
	@XmlElement(name="TxnChnlTp", namespace = NameSpace.ODS_URL )
	private String TxnChnlTp      = null;  // 渠道类型              
	@XmlElement(name="ChnlNo", namespace = NameSpace.ODS_URL )
	private String ChnlNo         = null;  // 渠道编号              
	@XmlElement(name="TxnTmlId", namespace = NameSpace.ODS_URL )
	private String TxnTmlId       = null;  // 终端号                
	@XmlElement(name="CnsmrSvrId", namespace = NameSpace.ODS_URL )
	private String CnsmrSvrId     = null;  // 服务调用方服务器标识  
	@XmlElement(name="OrigCnsmrSeqNo", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrSeqNo = null;  // 全局业务流水号        
	@XmlElement(name="OrigCnsmrId", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrId    = null;  // 服务原发起方系统编号  
	@XmlElement(name="OrigTmlId", namespace = NameSpace.ODS_URL )
	private String OrigTmlId      = null;  // 服务原发起方终端号    
	@XmlElement(name="OrigCnsmrSvrId", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrSvrId = null;  // 服务原发起方服务器标识
	@XmlElement(name="UsrLng", namespace = NameSpace.ODS_URL )
	private String UsrLng         = null;  // 用户语言              
	@XmlElement(name="FileFlg", namespace = NameSpace.ODS_URL )
	private String FileFlg        = null;  // 文件标志          
	
//	private String SvcId = null  ;  // 服务代码
//	private String SvcScn         = null;  // 服务场景         
//	private String SvcSplrTxnCd   = null;  // 服务提供方交易代码    
//	private String SvcCstTxnCd    = null;  // 调用方交易代码        
//	private String MAC            = null;  // MAC字段               
//	private String CnsmrSysId     = null;  // 服务调用方系统编号    
//	private String TxnDt          = null;  // 交易日期              
//	private String TxnTm          = null;  // 交易时间              
//	private String AcgDt          = null;  // 会计日期              
//	private String CnsmrSeqNo     = null;  // 服务调用方系统流水号  
//	private String TxnChnlTp      = null;  // 渠道类型              
//	private String ChnlNo         = null;  // 渠道编号              
//	private String TxnTmlId       = null;  // 终端号                
//	private String CnsmrSvrId     = null;  // 服务调用方服务器标识  
//	private String OrigCnsmrSeqNo = null;  // 全局业务流水号        
//	private String OrigCnsmrId    = null;  // 服务原发起方系统编号  
//	private String OrigTmlId      = null;  // 服务原发起方终端号    
//	private String OrigCnsmrSvrId = null;  // 服务原发起方服务器标识
//	private String UsrLng         = null;  // 用户语言              
//	private String FileFlg        = null;  // 文件标志          
	
	public String getSvcId() {
		return SvcId;
	}
	public String getSvcScn() {
		return SvcScn;
	}
	public String getSvcSplrTxnCd() {
		return SvcSplrTxnCd;
	}
	public String getSvcCstTxnCd() {
		return SvcCstTxnCd;
	}
	public String getMAC() {
		return MAC;
	}
	public String getCnsmrSysId() {
		return CnsmrSysId;
	}
	public String getTxnDt() {
		return TxnDt;
	}
	public String getTxnTm() {
		return TxnTm;
	}
	public String getAcgDt() {
		return AcgDt;
	}
	public String getCnsmrSeqNo() {
		return CnsmrSeqNo;
	}
	public String getTxnChnlTp() {
		return TxnChnlTp;
	}
	public String getChnlNo() {
		return ChnlNo;
	}
	public String getTxnTmlId() {
		return TxnTmlId;
	}
	public String getCnsmrSvrId() {
		return CnsmrSvrId;
	}
	public String getOrigCnsmrSeqNo() {
		return OrigCnsmrSeqNo;
	}
	public String getOrigCnsmrId() {
		return OrigCnsmrId;
	}
	public String getOrigTmlId() {
		return OrigTmlId;
	}
	public String getOrigCnsmrSvrId() {
		return OrigCnsmrSvrId;
	}
	public String getUsrLng() {
		return UsrLng;
	}
	public String getFileFlg() {
		return FileFlg;
	}
	public void setSvcId(String svcId) {
		SvcId = svcId;
	}
	public void setSvcScn(String svcScn) {
		SvcScn = svcScn;
	}
	public void setSvcSplrTxnCd(String svcSplrTxnCd) {
		SvcSplrTxnCd = svcSplrTxnCd;
	}
	public void setSvcCstTxnCd(String svcCstTxnCd) {
		SvcCstTxnCd = svcCstTxnCd;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public void setCnsmrSysId(String cnsmrSysId) {
		CnsmrSysId = cnsmrSysId;
	}
	public void setTxnDt(String txnDt) {
		TxnDt = txnDt;
	}
	public void setTxnTm(String txnTm) {
		TxnTm = txnTm;
	}
	public void setAcgDt(String acgDt) {
		AcgDt = acgDt;
	}
	public void setCnsmrSeqNo(String cnsmrSeqNo) {
		CnsmrSeqNo = cnsmrSeqNo;
	}
	public void setTxnChnlTp(String txnChnlTp) {
		TxnChnlTp = txnChnlTp;
	}
	public void setChnlNo(String chnlNo) {
		ChnlNo = chnlNo;
	}
	public void setTxnTmlId(String txnTmlId) {
		TxnTmlId = txnTmlId;
	}
	public void setCnsmrSvrId(String cnsmrSvrId) {
		CnsmrSvrId = cnsmrSvrId;
	}
	public void setOrigCnsmrSeqNo(String origCnsmrSeqNo) {
		OrigCnsmrSeqNo = origCnsmrSeqNo;
	}
	public void setOrigCnsmrId(String origCnsmrId) {
		OrigCnsmrId = origCnsmrId;
	}
	public void setOrigTmlId(String origTmlId) {
		OrigTmlId = origTmlId;
	}
	public void setOrigCnsmrSvrId(String origCnsmrSvrId) {
		OrigCnsmrSvrId = origCnsmrSvrId;
	}
	public void setUsrLng(String usrLng) {
		UsrLng = usrLng;
	}
	public void setFileFlg(String fileFlg) {
		FileFlg = fileFlg;
	}
	
	

}
