package com.ods.transaction;

import java.sql.SQLException;
import java.util.Map;

import com.ods.exception.TxnException;
import com.ods.message.QueryMessager;

public interface ITransaction {
	/**
	 * 根据 Inparm 传入的参数值, 实现对数据库的查询 
	 * @param inParm 入参  
	 * @return  QueryMessager
	 * @throws SQLException
	 */
	public QueryMessager transaction (Map<String, Object> inParm) throws TxnException, SQLException;  //实现交易的具体流程 
	
	//public ArrayList<DbDataLine> transaction (TxnMessager txnMessager);  //实现交易的具体流程 
}
