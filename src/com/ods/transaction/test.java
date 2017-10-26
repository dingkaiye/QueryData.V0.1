package com.ods.transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ods.db.DbDataLine;
import com.ods.db.DbQuery;
import com.ods.log.OdsLog;
import com.ods.message.QueryMessager;

public class test implements ITransaction{
	Logger logger = OdsLog.getTxnLogger("AccnoQuary");
	
	public QueryMessager transaction(Map<String, Object> inParm) throws SQLException  {
		//public ArrayList<DbDataLine> transaction(Hashtable<String, Object> inParm) {

		ArrayList<DbDataLine> resultList = null;

		logger.info(inParm);
		resultList = DbQuery.excuteQuery("select * from TEST", null);

		for (int i = 0; i < resultList.size(); i++) {
			DbDataLine result = resultList.get(i);
			logger.debug(result.get("ID"));
			logger.debug(result.get("CC"));
			logger.debug(result.get("CCC"));
			logger.debug(result.get(2).toString());
		}
		
		QueryMessager  Result = new QueryMessager(resultList , resultList);
//		return Result;
		return new QueryMessager("qwertyuiop", "safdjas;oifha;oie;oaewihakas阿鲁卡多减肥啦");
	}
	
}
