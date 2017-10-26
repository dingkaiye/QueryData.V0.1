package com.ods.db;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ods.common.Config;
import com.ods.log.OdsLog;

/**
 * 数据库连接池
 * @author ding_kaiye
 *
 */
public class DbPool {
	private static DruidDataSource dataSource = null;
	private static Logger logger = OdsLog.getTxnLogger("DbPool");
	
	private DbPool() {
		
	}
	
	public static void init() {
		try {
			dataSource = getDataSource();
		} catch (Exception e) {
			logger.error("数据库连接池建立失败,稍后连接时将重试");
			e.printStackTrace();
		}
	}
	
	/**
	 * @author ding_kaiye
	 * @return DruidPooledConnection
	 * @throws Exception
	 * 获取数据库连接, 成功则返回数据库连接, 失败返回 null
	 */
	public static DruidPooledConnection  getConnection() {
		if ( dataSource == null ) {
			logger.info("数据库连接池不存在, 重新建立数据库连接池");
			try {
				dataSource = getDataSource();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("数据库连接池建立失败,稍后连接时将重试");
				return null;
			}
			logger.info("数据库连接池建立完毕");
		}
		
		DruidPooledConnection connnection = null;
		try {
			connnection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("获取数据库连接失败", e);
		}
		logger.debug("数据库连接获取完毕");
		return connnection;
	}

	
	//获取配置数据
	private static DruidDataSource getDataSource() throws Exception {
		DruidDataSource druidDataSource = null;
		Properties properties = Config.loadConfigPropertiesFile("druid.properties");
		druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
		logger.info("数据库连接池建立完毕");
		return druidDataSource; 
	}
	
}