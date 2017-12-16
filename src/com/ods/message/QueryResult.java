package com.ods.message;

import java.util.ArrayList;

import com.ods.db.DbDataLine;

/**
 * 保存数据库查询结果
 * @author ding_kaiye
 *
 */
public class QueryResult {
	int totalRows = -1;
	public ArrayList<DbDataLine> resultList = null;
	
	public QueryResult(){
		resultList = new ArrayList<DbDataLine>();
	}
	
	public QueryResult(ArrayList<DbDataLine> resultList , int totalRows){
		this.totalRows = totalRows;
		this.resultList = resultList;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public ArrayList<DbDataLine> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<DbDataLine> resultList) {
		this.resultList = resultList;
	}
	
	
	
}
