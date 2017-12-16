package com.ods.ws;

import java.util.List;
import java.util.Map;

public interface TxnBody {
	/**
	 * 初始化TxnBody 的方法
	 */
	public void init(Map<String, Object> Map, List<Map<String, Object>> List ) ;
	
	
}
