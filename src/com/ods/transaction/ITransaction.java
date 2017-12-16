package com.ods.transaction;

import java.sql.SQLException;
import java.util.Map;

import com.ods.exception.TxnException;
import com.ods.message.QueryMessager;
import com.ods.ws.TxnBody;

public interface ITransaction {
	/**
	 * @author ding_kaiye
	 * @param inParm   入参
	 * @param SerialNo  系统流水号
	 * @return
	 * @throws TxnException
	 * @throws SQLException
	 */
//	public QueryMessager transaction (Map<String, Object> inParm, String SerialNo) throws TxnException, SQLException;  //实现交易的具体流程 
	public QueryMessager transaction (TxnBody txnBody, String SerialNo) throws TxnException, SQLException;  //实现交易的具体流程 
	
	//public ArrayList<DbDataLine> transaction (TxnMessager txnMessager);  //实现交易的具体流程 
}
