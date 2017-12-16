package com.ods.message;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

/**
 * @author ding_kaiye
 * @DATE 2017-09-14
 * 对应响应Esb报文中的SysHead
 */
@XmlRootElement(name = "SysHead", namespace=NameSpace.ODS_URL)
@XmlAccessorType(XmlAccessType.FIELD)  
public class SysHeadOut {
	
	@XmlElement(name = "SvcId", namespace = NameSpace.ODS_URL )
	private String SvcId          = new String() ;         //  服务代码
	@XmlElement(name = "SvcScn", namespace = NameSpace.ODS_URL )
	private String SvcScn         = new String() ;         //  服务场景
	@XmlElement(name = "SvcSplrTxnCd", namespace = NameSpace.ODS_URL )
	private String SvcSplrTxnCd   = new String() ;         //  服务提供方交易代码
	@XmlElement(name = "SvcCstTxnCd", namespace = NameSpace.ODS_URL )
	private String SvcCstTxnCd    = new String() ;         //  调用方交易代码
	@XmlElement(name = "MAC", namespace = NameSpace.ODS_URL )
	private String MAC            = new String() ;         //  MAC字段
	@XmlElement(name = "CnsmrSysId", namespace = NameSpace.ODS_URL )
	private String CnsmrSysId     = new String() ;         //  服务调用方系统编号
	@XmlElement(name = "Vrsn", namespace = NameSpace.ODS_URL )
	private String Vrsn           = new String() ;         //  服务版本号
	@XmlElement(name = "CnsmrSeqNo", namespace = NameSpace.ODS_URL )
	private String CnsmrSeqNo     = new String() ;         //  服务调用方系统流水号
	@XmlElement(name = "SvcSplrSysId", namespace = NameSpace.ODS_URL )
	private String SvcSplrSysId   = new String() ;         //  服务提供方系统编号
	@XmlElement(name = "SvcSplrSeqNo", namespace = NameSpace.ODS_URL )
	private String SvcSplrSeqNo   = new String() ;         //  服务提供方流水号
	@XmlElement(name = "TxnDt", namespace = NameSpace.ODS_URL )
	private String TxnDt          = new String() ;         //  交易日期
	@XmlElement(name = "TxnTm", namespace = NameSpace.ODS_URL )
	private String TxnTm          = new String() ;         //  交易时间
	@XmlElement(name = "AcgDt", namespace = NameSpace.ODS_URL )
	private String AcgDt          = new String() ;         //  会计日期
//	@XmlElement(name = "SvcSplrSvrId", namespace = NameSpace.ODS_URL )
//	private String SvcSplrSvrId   = new String() ;         //  服务提供方服务器标识
	@XmlElement(name = "TxnChnlTp", namespace = NameSpace.ODS_URL )
	private String TxnChnlTp      = new String() ;         //  渠道类型
	@XmlElement(name = "ChnlNo", namespace = NameSpace.ODS_URL )
	private String ChnlNo         = new String() ;         //  渠道编号
	@XmlElement(name = "TxnTmlId", namespace = NameSpace.ODS_URL )
	private String TxnTmlId       = new String() ;         //  终端号
	@XmlElement(name = "CnsmrSvrId", namespace = NameSpace.ODS_URL )
	private String CnsmrSvrId     = new String() ;         //  服务调用方服务器标识
	@XmlElement(name = "OrigCnsmrId", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrId    = new String() ;         //  服务原发起方系统编号
	@XmlElement(name = "OrigCnsmrSeqNo", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrSeqNo = new String() ;         //  全局业务流水号
	@XmlElement(name = "OrigTmlId", namespace = NameSpace.ODS_URL )
	private String OrigTmlId      = new String() ;         //  服务原发起方终端号
	@XmlElement(name = "OrigCnsmrSvrId", namespace = NameSpace.ODS_URL )
	private String OrigCnsmrSvrId = new String() ;         //  服务原发起方服务器标识
	@XmlElement(name = "UsrLng", namespace = NameSpace.ODS_URL )
	private String UsrLng         = new String() ;         //  用户语言
	@XmlElement(name = "FileFlg", namespace = NameSpace.ODS_URL )
	private String FileFlg        = new String() ;         //  文件标志
	@XmlElement(name = "TxnSt", namespace = NameSpace.ODS_URL )
    private String TxnSt          = new String() ;         //  交易返回状态
	@XmlElement(name = "RetCd", namespace = NameSpace.ODS_URL )
	private String RetCd          = new String() ;         //  交易返回代码
	@XmlElement(name = "RetMsg", namespace = NameSpace.ODS_URL )
	private String RetMsg         = new String() ;         //  交易返回信息
	
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
	public String getVrsn() {
		return Vrsn;
	}
	public String getCnsmrSeqNo() {
		return CnsmrSeqNo;
	}
	public String getSvcSplrSysId() {
		return SvcSplrSysId;
	}
	public String getSvcSplrSeqNo() {
		return SvcSplrSeqNo;
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
//	public String getSvcSplrSvrId() {
//		return SvcSplrSvrId;
//	}
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
	public String getOrigCnsmrId() {
		return OrigCnsmrId;
	}
	public String getOrigCnsmrSeqNo() {
		return OrigCnsmrSeqNo;
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
	public String getTxnSt() {
		return TxnSt;
	}
	public String getRetCd() {
		return RetCd;
	}
	public String getRetMsg() {
		return RetMsg;
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
	public void setVrsn(String vrsn) {
		Vrsn = vrsn;
	}
	public void setCnsmrSeqNo(String cnsmrSeqNo) {
		CnsmrSeqNo = cnsmrSeqNo;
	}
	public void setSvcSplrSysId(String svcSplrSysId) {
		SvcSplrSysId = svcSplrSysId;
	}
	public void setSvcSplrSeqNo(String svcSplrSeqNo) {
		SvcSplrSeqNo = svcSplrSeqNo;
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
//	public void setSvcSplrSvrId(String svcSplrSvrId) {
//		SvcSplrSvrId = svcSplrSvrId;
//	}
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
	public void setOrigCnsmrId(String origCnsmrId) {
		OrigCnsmrId = origCnsmrId;
	}
	public void setOrigCnsmrSeqNo(String origCnsmrSeqNo) {
		OrigCnsmrSeqNo = origCnsmrSeqNo;
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
	public void setTxnSt(String txnSt) {
		TxnSt = txnSt;
	}
	public void setRetCd(String retCd) {
		RetCd = retCd;
	}
	public void setRetMsg(String retMsg) {
		RetMsg = retMsg;
	}
	
	
	
	
	
}
