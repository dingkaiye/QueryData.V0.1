package com.ods.transaction.DepositTrans.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.ods.common.NameSpace;
import com.ods.ws.TxnBody;

@XmlRootElement(name = "Body", namespace = NameSpace.ODS_URL)

@XmlType(propOrder = { "TotlNm", "RtrnNm", "Acct","AcctOfNm", "Ccy", "DocTp", "CrtfctNo", "StrDt", "EndDt", "TxnRsltAry"}) 
public class RspBody implements TxnBody {
	
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String TotlNm   = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String RtrnNm  = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String Acct      = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String AcctOfNm  = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String Ccy       = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String DocTp     = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String CrtfctNo  = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String StrDt     = null ; //
	@XmlElement(namespace = NameSpace.ODS_URL )
	public String EndDt     = null ; //
	
//	 //@XmlElementWrapper(name="items")
//	 @XmlElement(name="TxnRsltAry")
	@XmlElement(name="TxnRsltAry", namespace = NameSpace.ODS_URL )
	public List<TransResultLine> TxnRsltAry = null;
	
	@Override
	public void init (Map<String, Object> Map, List<Map<String, Object>> List ) {

		this.TotlNm     = (String) Map.get("TotlNm")  ; //
		this.RtrnNm    = (String) Map.get("RtrnNm")  ; //
		this.Acct      = (String) Map.get("Acct")  ; //
		this.AcctOfNm  = (String) Map.get("AcctOfNm")  ; //
		this.Ccy       = (String) Map.get("Ccy")  ; //
		this.DocTp     = (String) Map.get("DocTp")  ; //
		this.CrtfctNo  = (String) Map.get("CrtfctNo")  ; //
		this.StrDt     = (String) Map.get("StrDt")  ; //
		this.EndDt     = (String) Map.get("EndDt")  ; //
		
		TxnRsltAry = new ArrayList<TransResultLine>();
		for (Map<String, Object> ob: List ){
			TransResultLine tr = new TransResultLine(ob);
			TxnRsltAry.add(tr);
		}
	}
	
	
}
