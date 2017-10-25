package com.ods.message;

/**
 * @author ding_kaiye
 * @DATE 2017-09-14
 * 对应响应Esb报文中的SysHead
 */
public class SysHeadOut {
	
	private String SvcId = null; // 服务代码
	private String SvcScn = null; // 服务场景
	private String SvcSpltTxnCd = null; //服务提供方交易代码
	private String SvcCstTxnCd = null;  //调用交易方交易代码
	private String MAC = null;     // MAC信息
	private String CnsmrSysId = null; //服务调用方系统代号
	private String Vrsn = null; //服务器版本号
	private String CnsmrSeqNo = null; // 服务调用方系统流水号
	private String SvcSplrSysId = null; // 服务提供方系统编号
	private String SvcSplrSeqNo = null; // 服务提供方流水号
	private String TxnDt = null; //交易日期
	private String TxnTm = null; //交易时间
	private String AcgDt = null; //会计日期
	private String SvcSplrSvrId = null ; // 服务提供方服务器标识
	private String TxnChnlTp = null;  // 渠道类型
	private String ChnlNo = null; //渠道编号
	private String TxnTmlId = null; //终端号
	private String OrigCnsmrId = null;  // 服务原发起方系统编号
	private String OrigCnsmrSeqNo = null;  // 全局业务流水号 
	private String OrigtmlId = null;  // 服务原发起方终端编号
	private String OrigcnsmrsveId = null; //  服务原发起方服务器标识
	private String UsrLng = null;  // 用户语言
	private String Fileflg = null; // 文件标志
	private String TxnSt = null; // 交易返回状态
	private String RetCd = null; // 交易返回代码
	private String RetMsg = null; // 交易返回信息 
	
	
	public String getSvcId() {
		return SvcId;
	}
	public void setSvcId(String svcId) {
		SvcId = svcId;
	}
	public String getSvcScn() {
		return SvcScn;
	}
	public void setSvcScn(String svcScn) {
		SvcScn = svcScn;
	}
	public String getSvcSpltTxnCd() {
		return SvcSpltTxnCd;
	}
	public void setSvcSpltTxnCd(String svcSpltTxnCd) {
		SvcSpltTxnCd = svcSpltTxnCd;
	}
	public String getSvcCstTxnCd() {
		return SvcCstTxnCd;
	}
	public void setSvcCstTxnCd(String svcCstTxnCd) {
		SvcCstTxnCd = svcCstTxnCd;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public String getCnsmrSysId() {
		return CnsmrSysId;
	}
	public void setCnsmrSysId(String cnsmrSysId) {
		CnsmrSysId = cnsmrSysId;
	}
	public String getVrsn() {
		return Vrsn;
	}
	public void setVrsn(String vrsn) {
		Vrsn = vrsn;
	}
	public String getCnsmrSeqNo() {
		return CnsmrSeqNo;
	}
	public void setCnsmrSeqNo(String cnsmrSeqNo) {
		CnsmrSeqNo = cnsmrSeqNo;
	}
	public String getSvcSplrSysId() {
		return SvcSplrSysId;
	}
	public void setSvcSplrSysId(String svcSplrSysId) {
		SvcSplrSysId = svcSplrSysId;
	}
	public String getSvcSplrSeqNo() {
		return SvcSplrSeqNo;
	}
	public void setSvcSplrSeqNo(String svcSplrSeqNo) {
		SvcSplrSeqNo = svcSplrSeqNo;
	}
	public String getTxnDt() {
		return TxnDt;
	}
	public void setTxnDt(String txnDt) {
		TxnDt = txnDt;
	}
	public String getTxnTm() {
		return TxnTm;
	}
	public void setTxnTm(String txnTm) {
		TxnTm = txnTm;
	}
	public String getAcgDt() {
		return AcgDt;
	}
	public void setAcgDt(String acgDt) {
		AcgDt = acgDt;
	}
	public String getSvcSplrSvrId() {
		return SvcSplrSvrId;
	}
	public void setSvcSplrSvrId(String svcSplrSvrId) {
		SvcSplrSvrId = svcSplrSvrId;
	}
	public String getTxnChnlTp() {
		return TxnChnlTp;
	}
	public void setTxnChnlTp(String txnChnlTp) {
		TxnChnlTp = txnChnlTp;
	}
	public String getChnlNo() {
		return ChnlNo;
	}
	public void setChnlNo(String chnlNo) {
		ChnlNo = chnlNo;
	}
	public String getTxnTmlId() {
		return TxnTmlId;
	}
	public void setTxnTmlId(String txnTmlId) {
		TxnTmlId = txnTmlId;
	}
	public String getOrigCnsmrId() {
		return OrigCnsmrId;
	}
	public void setOrigCnsmrId(String origCnsmrId) {
		OrigCnsmrId = origCnsmrId;
	}
	public String getOrigCnsmrSeqNo() {
		return OrigCnsmrSeqNo;
	}
	public void setOrigCnsmrSeqNo(String origCnsmrSeqNo) {
		OrigCnsmrSeqNo = origCnsmrSeqNo;
	}
	public String getOrigtmlId() {
		return OrigtmlId;
	}
	public void setOrigtmlId(String origtmlId) {
		OrigtmlId = origtmlId;
	}
	public String getOrigcnsmrsveId() {
		return OrigcnsmrsveId;
	}
	public void setOrigcnsmrsveId(String origcnsmrsveId) {
		OrigcnsmrsveId = origcnsmrsveId;
	}
	public String getUsrLng() {
		return UsrLng;
	}
	public void setUsrLng(String usrLng) {
		UsrLng = usrLng;
	}
	public String getFileflg() {
		return Fileflg;
	}
	public void setFileflg(String fileflg) {
		Fileflg = fileflg;
	}
	public String getTxnSt() {
		return TxnSt;
	}
	public void setTxnSt(String txnSt) {
		TxnSt = txnSt;
	}
	public String getRetCd() {
		return RetCd;
	}
	public void setRetCd(String retCd) {
		RetCd = retCd;
	}
	public String getRetMsg() {
		return RetMsg;
	}
	public void setRetMsg(String retMsg) {
		RetMsg = retMsg;
	}
	
	

}
