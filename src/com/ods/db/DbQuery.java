package com.ods.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ods.log.OdsLog;

public class DbQuery {
	private static Logger logger = OdsLog.getTxnLogger("DbQuery");
	
	/**
	 * 匿名参数查询 
	 * @param sql
	 * @param params
	 * @return ArrayList
	 * @throws SQLException 
	 * 使用匿名参数查询数据库,若没有参数, params 赋值 null 
	 */
	public static ArrayList<DbDataLine> excuteQuery(String sql) throws SQLException {
		return  excuteQuery(sql, null);
	}
	
	public static ArrayList<DbDataLine> excuteQuery(String sql, Object[] params) throws SQLException  {
		
		ResultSet queryResult = null; 
		PreparedStatement queryStatement = null;
		Connection conn = null;
		
		logger.info("查询开始,SQL[" + sql + "] 入参[" + params + "] " );
		
		conn = DbPool.getConnection();		//获取数据库连接
		queryStatement = conn.prepareStatement(sql);
		
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryStatement.setObject(i + 1, params[i]);
			}
		}
		
		ArrayList<DbDataLine> resultList = new ArrayList<DbDataLine>();
		queryResult = queryStatement.executeQuery();
		while (queryResult.next()) {
			DbDataLine dbDataLine = new DbDataLine(queryResult);
			resultList.add(dbDataLine);
		}
		closeConnection(queryResult, queryStatement, conn);
		logger.info("查询完成,SQL[" + sql + "] 入参[" + params + "] " );
		return resultList;
	}


	/**
	 * 根据开始行和结束行数实现查询
	 * @param sql
	 * @param params
	 * @param startKey (含)
	 * @param endKey   (含)
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<DbDataLine> excuteQuery(String sql, Object[] params, int startKey, int endKey ) throws Exception {
//		public static ArrayList<DbDataLine> excuteQuery(String sql, Object[] params, long startKey, long endKey ) throws Exception {
		logger.info("查询开始,SQL[" + sql + "] 入参[" + params + "] startKey[" + startKey + "]endKey[" + endKey + "]" );
		ResultSet queryResult = null; 
		PreparedStatement queryStatement = null;
		Connection conn = null;
		 
		conn = DbPool.getConnection();		//获取数据库连接
		queryStatement = conn.prepareStatement(sql);
		
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryStatement.setObject(i + 1, params[i]);
			}
		}
		
		ArrayList<DbDataLine> resultList = new ArrayList<DbDataLine>();
		try {
			queryResult = queryStatement.executeQuery();
			
			queryResult.last();
			int TotleRows = queryResult.getRow();
			queryResult.beforeFirst();
			
			startKey = (startKey <= 0 ? 1 : startKey);
			endKey = (endKey <= TotleRows ?  endKey : TotleRows);
			
			// 移动到 startKey 
			queryResult.absolute(startKey-1);
			// 一下暂不使用   
			//for(long i=1; i<startKey; i++){
			//   queryResult.next();
			//}
			
			while (queryResult.next() && (queryResult.getRow() < endKey) ) {
				DbDataLine dbDataLine = new DbDataLine(queryResult);
				resultList.add(dbDataLine);
			}
		} catch (Exception e) {
			logger.error("excuteQuery Error:" + e.getMessage());
		} finally {
			// 释放资源连接池
			closeConnection(queryResult, queryStatement, conn);
		}
		logger.info("查询完成,SQL[" + sql + "] 入参[" + params + "] startKey[" + startKey + "]endKey[" + endKey + "]" );
		return resultList;
	}

	/**
	 * @author ding_kaiye
	 * @param sql
	 * @param params
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public static ArrayList<ResultSet> excutePageQuery(String sql, Object[] params, long start, long end) throws Exception {
		ArrayList<ResultSet> returnList = null;
		return returnList ;
	}
	
	
	/**
	 * 关闭数据库连接
	 * @param resultSet
	 * @param preparedStatement
	 * @param conn
	 */
	private static void closeConnection(ResultSet resultSet, PreparedStatement preparedStatement, Connection conn) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		// 关闭PreparedStatement对象
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		// 关闭Connection 对象
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("closeConnection ERROR: " + e.getMessage());
			}
		}

	}
	
}
