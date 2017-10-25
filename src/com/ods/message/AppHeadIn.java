package com.ods.message;

/**
 * @author ding_kaiye
 * @DATE 2017-09-14
 * 对应接收Esb报文中的AppHead
 */
public class AppHeadIn {
	
	private String TxnTlrId = null;  //柜员号 
	private String OrgId = null ;  //机构代码
	private String TlrPwsd = null; // 柜员密码
	private String TlrLvl = null ; // 柜员级别
	private String TlrTp = null ;  // 柜员类别
	private String aprvFlg = null; // 复核标志
	private String [] AprvTlrInf = null; // 录入柜员数组
	private String AprvTlrId = null; // 录入柜员标识
	private String AprvOrgId = null; // 交易录入机构代码 
	private String AprvTlrLvl = null; // 交易录入柜员级别
	private String AprvTlrTp = null; // 交易录入柜员类别
	private String Ahrflg = null; // 授权标志
	private String [] AhrTlrInf = null ; //授权柜员信息数组
	private String AhrTlrId = null; // 授权柜员标识
	private String AhrOrgId = null; // 授权机构代码 
	private String AhrtlrPswd = null; // 授权柜员密码 
	private String AhrTlrLvl = null; // 授权柜员级别
	private String AhrTlrTp = null; // 授权柜员类别
	private String Scndflg = null ; //二次提交标志
	
	public String getTxnTlrId() {
		return TxnTlrId;
	}
	public void setTxnTlrId(String txnTlrId) {
		TxnTlrId = txnTlrId;
	}
	public String getOrgId() {
		return OrgId;
	}
	public void setOrgId(String orgId) {
		OrgId = orgId;
	}
	public String getTlrPwsd() {
		return TlrPwsd;
	}
	public void setTlrPwsd(String tlrPwsd) {
		TlrPwsd = tlrPwsd;
	}
	public String getTlrLvl() {
		return TlrLvl;
	}
	public void setTlrLvl(String tlrLvl) {
		TlrLvl = tlrLvl;
	}
	public String getTlrTp() {
		return TlrTp;
	}
	public void setTlrTp(String tlrTp) {
		TlrTp = tlrTp;
	}
	public String getAprvFlg() {
		return aprvFlg;
	}
	public void setAprvFlg(String aprvFlg) {
		this.aprvFlg = aprvFlg;
	}
	public String[] getAprvTlrInf() {
		return AprvTlrInf;
	}
	public void setAprvTlrInf(String[] aprvTlrInf) {
		AprvTlrInf = aprvTlrInf;
	}
	public String getAprvTlrId() {
		return AprvTlrId;
	}
	public void setAprvTlrId(String aprvTlrId) {
		AprvTlrId = aprvTlrId;
	}
	public String getAprvOrgId() {
		return AprvOrgId;
	}
	public void setAprvOrgId(String aprvOrgId) {
		AprvOrgId = aprvOrgId;
	}
	public String getAprvTlrLvl() {
		return AprvTlrLvl;
	}
	public void setAprvTlrLvl(String aprvTlrLvl) {
		AprvTlrLvl = aprvTlrLvl;
	}
	public String getAprvTlrTp() {
		return AprvTlrTp;
	}
	public void setAprvTlrTp(String aprvTlrTp) {
		AprvTlrTp = aprvTlrTp;
	}
	public String getAhrflg() {
		return Ahrflg;
	}
	public void setAhrflg(String ahrflg) {
		Ahrflg = ahrflg;
	}
	public String[] getAhrTlrInf() {
		return AhrTlrInf;
	}
	public void setAhrTlrInf(String[] ahrTlrInf) {
		AhrTlrInf = ahrTlrInf;
	}
	public String getAhrTlrId() {
		return AhrTlrId;
	}
	public void setAhrTlrId(String ahrTlrId) {
		AhrTlrId = ahrTlrId;
	}
	public String getAhrOrgId() {
		return AhrOrgId;
	}
	public void setAhrOrgId(String ahrOrgId) {
		AhrOrgId = ahrOrgId;
	}
	public String getAhrtlrPswd() {
		return AhrtlrPswd;
	}
	public void setAhrtlrPswd(String ahrtlrPswd) {
		AhrtlrPswd = ahrtlrPswd;
	}
	public String getAhrTlrLvl() {
		return AhrTlrLvl;
	}
	public void setAhrTlrLvl(String ahrTlrLvl) {
		AhrTlrLvl = ahrTlrLvl;
	}
	public String getAhrTlrTp() {
		return AhrTlrTp;
	}
	public void setAhrTlrTp(String ahrTlrTp) {
		AhrTlrTp = ahrTlrTp;
	}
	public String getScndflg() {
		return Scndflg;
	}
	public void setScndflg(String scndflg) {
		Scndflg = scndflg;
	}
	
	
	
	
	
	
}
