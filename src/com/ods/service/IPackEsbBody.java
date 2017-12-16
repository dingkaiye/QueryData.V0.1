package com.ods.service;

import java.util.ArrayList;
import java.util.Map;

import com.ods.db.DbDataLine;
import com.ods.ws.TxnBody;

public interface IPackEsbBody {
	/**
	 * 根据查询结果生成一个ESB的应答包
	 * @param resultHead  
	 * @param resultList
	 * @return
	 */
	public TxnBody packEsbBody(Map<String, Object> resultHead,  ArrayList<DbDataLine> resultList); 
}
