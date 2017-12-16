package com.ods.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ods.log.OdsLog;
import com.ods.message.QueryResult;

/**
 * @author ding_kaiye
 * 提供操作数据库的接口
 */
public class DbQuery {
	private static Logger logger = OdsLog.getTxnLogger("DbQuery");
	
//	/**
//	 * 匿名参数查询 , 仅使用SQL查询且不需要总记录数时使用
//	 * @param sql
//	 * @param params
//	 * @return ArrayList
//	 * @throws SQLException 
//	 * 使用匿名参数查询数据库,若没有参数, params 赋值 null 
//	 */
//	public static QueryResult excuteQuery(String sql) throws SQLException {
//		int TotleRows = -1;
//		return  excuteQuery(sql, null, TotleRows);
//	}
	
//	/**
//	 * 使用匿名参数查询数据库,若没有参数, params 赋值  null 
//	 * 不需要获取总记录数时, 可使用此查询, 屏蔽 TotleRows
//	 * @param sql        SQL语句
//	 * @param params     参数列表
//	 * @param TotleRows  用于记录本次查询数据的总记录数
//	 * @return ArrayList 本次查询数据的记录
//	 * @throws SQLException
//	 */
//	
//	public static QueryResult excuteQuery(String sql, Object[] params) throws SQLException  {
//		int TotleRows = -1;
//		return excuteQuery(sql, params, TotleRows);
//	}
	
	
	/**
	 * 匿名参数查询 , 仅使用SQL查询需要总记录数时使用
	 * @param sql
	 * @param params
	 * @return ArrayList
	 * @throws SQLException 
	 * 使用匿名参数查询数据库,若没有参数, params 赋值 null 
	 */
	public static QueryResult excuteQuery(String sql) throws SQLException {
		return  excuteQuery(sql, null);
	}
	
	/**
	 * 使用匿名参数查询数据库,若没有参数, params 赋值  null 
	 * @param sql        SQL语句
	 * @param params     参数列表
	 * @param TotleRows  用于记录本次查询数据的总记录数
	 * @return ArrayList 本次查询数据的记录
	 * @throws SQLException
	 */
	public static QueryResult excuteQuery(String sql, Object[] params) throws SQLException  {
		
		ResultSet queryResult = null; 
		PreparedStatement queryStatement = null;
		Connection conn = null;
		int totleRows = 0;
		
		logger.info("查询开始,SQL[" + sql + "] 入参[" + params + "] " );
		
		conn = DbPool.getConnection();		//获取数据库连接
		queryStatement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryStatement.setObject(i + 1, params[i]);
			}
		}
		
		ArrayList<DbDataLine> resultList = new ArrayList<DbDataLine>();
		queryResult = queryStatement.executeQuery();
		
		queryResult.last();
		totleRows = queryResult.getRow();
		logger.debug(totleRows);
		queryResult.beforeFirst();
		
		while (queryResult.next()) {
			DbDataLine dbDataLine = new DbDataLine(queryResult);
			resultList.add(dbDataLine);
		}
		closeConnection(queryResult, queryStatement, conn);
		logger.info("查询完成,SQL[" + sql + "] 入参[" + params + "] " );
		return new QueryResult(resultList, totleRows);
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
	public static QueryResult excuteQuery(String sql, Object[] params, int startKey, int endKey) throws SQLException {
//		public static ArrayList<DbDataLine> excuteQuery(String sql, Object[] params, long startKey, long endKey ) throws Exception {
		logger.info("查询开始,SQL[" + sql + "] 入参[" + params.toString() + "] startKey[" + startKey + "]endKey[" + endKey + "]" );
		ResultSet queryResult = null; 
		PreparedStatement queryStatement = null;
		Connection conn = null;
		int totleRows = 0;
		 
		conn = DbPool.getConnection();		//获取数据库连接
//		queryStatement = conn.prepareStatement(sql);
		queryStatement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryStatement.setObject(i + 1, params[i]);
			}
		}
		
		ArrayList<DbDataLine> resultList = new ArrayList<DbDataLine>();
		try {
			queryResult = queryStatement.executeQuery();
			
			queryResult.last();
			totleRows = queryResult.getRow();
			queryResult.beforeFirst();
			
			startKey = (startKey  <= 0 ? 1 : startKey );
			endKey = (endKey <= totleRows ?  endKey : totleRows);
			
			// 移动到 startKey 
			if(startKey > 1){
				queryResult.absolute(startKey -1 );
			}
			
			while (queryResult.next() && (queryResult.getRow() <= endKey) ) {
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
		
		return new QueryResult(resultList, totleRows);
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
//	
//	public static ArrayList<ResultSet> excutePageQuery(String sql, Object[] params, long start, long end) throws Exception {
//		ArrayList<ResultSet> returnList = null;
//		return returnList ;
//	}
//	
	
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
