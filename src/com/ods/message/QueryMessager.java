package com.ods.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ods.db.DbDataLine;

/**
 * TxnService 和 具体交易服务之间的信使 
 * 对具体交易程序提供最小集合的所需数据,
 * TxnMessager 不对 具体交易程序 可见
 * @author ding_kaiye
 */
public class QueryMessager {
	private boolean result = true;  //查询结果, true 成功, false 失败
	private Map<String, Object> inParm = null; // 入参
	
	private Map<String, Object> resultHead = null;   //数据库查询结果,表头数据
	private ArrayList<DbDataLine> resultList = null; //数据库查询结果的数据
	
	private String returnCode = null;  // 返回码
	private String msg = null;         // 返回信息
	
	
	public QueryMessager(Map<String, Object> resultHead, ArrayList<DbDataLine> resultList){
		this.result = true;   // 成功 
		this.resultHead = resultHead;
		this.resultList = resultList;
	}
	
	public QueryMessager(ArrayList<DbDataLine> resultHead, ArrayList<DbDataLine> resultList){
		this.result = true;  //成功
		this.resultList = resultList;
		this.resultHead = new HashMap<String, Object >();
		
		//将 ArrayList<DbDataLine> 类型的 resultHead 转换为 Map<String, Object>
		if (null != resultHead && resultHead.size() != 0) {
			// 此处为初始化resultHead, 因此此处仅取 resultHead 的 第一行
			DbDataLine valueLine = resultHead.get(0); 
			int columnCount = valueLine.getColumnCount(); // 字段数
			for (int i = 1; i <= columnCount; i++) {
				String Name = valueLine.getColumnNameBySeq(i);
				Object data = valueLine.get(i);
				this.resultHead.put(Name, data);
			}
		}
	}
	
	/**
	 * 构造含有错误信息的返回
	 */
	public QueryMessager(boolean result, String returnCode, String msg){
		this.result = false;
		this.returnCode = returnCode;
		this.msg = msg;
	}
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public Map<String, Object> getInParm() {
		return inParm;
	}
	public void setInParm(Map<String, Object> inParm) {
		this.inParm = inParm;
	}
	public ArrayList<DbDataLine> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<DbDataLine> resultList) {
		this.resultList = resultList;
	}

	public Map<String, Object> getResultHead() {
		return resultHead;
	}

	public void setResultHead(Map<String, Object> resultHead) {
		this.resultHead = resultHead;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
