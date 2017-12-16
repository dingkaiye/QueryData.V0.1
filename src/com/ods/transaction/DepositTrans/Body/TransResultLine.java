package com.ods.transaction.DepositTrans.Body;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ods.common.NameSpace;

@XmlRootElement(namespace = NameSpace.ODS_URL)
public class TransResultLine {
	
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String EvntSrlNo  = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String CrdNum     = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String PsbkAcct   = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TxnDt      = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TxnAmt     = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String Balce      = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String DoNotAcct  = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String DbAndCr    = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String Ccy      = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String CorrSign   = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AbstRsm    = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AcctNo     = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String OprNum     = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TdgNtw     = new String() ;
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TelleSrlNo = new String() ;
	
	public TransResultLine (
			String EvntSrlNo,  String CrdNum, String PsbkAcct, String TxnDt, String TxnAmt, 
			String Balce, String DoNotAcct, String DbAndCr, String Ccy, String CorrSign, 
			String AbstRsm, String AcctNo, String OprNum, String TdgNtw, String TelleSrlNo ){
		this.EvntSrlNo  = EvntSrlNo   ;
		this.CrdNum     = CrdNum      ;
		this.PsbkAcct   = PsbkAcct    ;
		this.TxnDt      = TxnDt       ;
		this.TxnAmt     = TxnAmt      ;
		this.Balce      = Balce       ;
		this.DoNotAcct  = DoNotAcct   ;
		this.DbAndCr    = DbAndCr     ;
		this.Ccy      = Ccy       ;
		this.CorrSign   = CorrSign    ;
		this.AbstRsm    = AbstRsm     ;
		this.AcctNo     = AcctNo      ;
		this.OprNum     = OprNum      ;
		this.TdgNtw     = TdgNtw      ;
		this.TelleSrlNo = TelleSrlNo  ;
	}

	public TransResultLine (Map<String, Object> result){
		this.EvntSrlNo  = (String) result.get("EvntSrlNo")   ;
		this.CrdNum     = (String) result.get("CrdNum")   ;
		this.PsbkAcct   = (String) result.get("PsbkAcct")   ;
		this.TxnDt      = (String) result.get("TxnDt")   ;
		this.TxnAmt     = (String) result.get("TxnAmt")   ;
		this.Balce      = (String) result.get("Balce")   ;
		this.DoNotAcct  = (String) result.get("DoNotAcct")   ;
		this.DbAndCr    = (String) result.get("DbAndCr")   ;
		this.Ccy      = (String) result.get("Ccy_A")   ;
		this.CorrSign   = (String) result.get("CorrSign")   ;
		this.AbstRsm    = (String) result.get("AbstRsm")   ;
		this.AcctNo     = (String) result.get("AcctNo")   ;
		this.OprNum     = (String) result.get("OprNum")   ;
		this.TdgNtw     = (String) result.get("TdgNtw")   ;
		this.TelleSrlNo = (String) result.get("TelleSrlNo")  ;
	}
	
	public TransResultLine (){
		
	}
}
