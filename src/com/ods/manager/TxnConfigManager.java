package com.ods.manager;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.ods.common.Config;
import com.ods.log.OdsLog;

/**
 * 维护各交易的配置信息
 * @author ding_kaiye
 * @date 2017-09-21
 */
public class TxnConfigManager {
	
	private static Logger logger = OdsLog.getLogger("SysLog"); 
	
	private static Properties  TxnRoster = null; // 交易与交易配置文件对照关系的花名册 
	//系统队列 Map
	private static Hashtable<String, Properties> txnConfigMap = new Hashtable<String, Properties> () ;
	
	private static  String path = "./config/TxnConfig/";  //默认文件存放在此目录下
	private static  String TxnRosterFileName =  "TxnRoster.properties" ; //交易花名册文件的默认文件名
		
	/**
	 * 初始化各交易的配置信息到系统
	 * 初始化不成功报错但不中断系统启动
	 * 若交易配置信息系统中不存在,新交易发起后重新加载
	 * @return
	 */
	protected static boolean Init() {

		logger.info("加载交易与交易配置文件对照关系的花名册:开始");
		try{
			TxnRoster = Config.loadPropertiesFile(path, TxnRosterFileName);  // 交易与交易配置文件对照关系 固定为此文件名
		}catch(IOException e){
			logger.error("登记交易与交易配置文件对照关系的配置文件[" + path + TxnRosterFileName + "]不存在,本次加载失败");
			return false;
		}
		
		txnConfigMap.clear();   // 清空队列 
		for (String key : TxnRoster.stringPropertyNames()) {
			try {
				Properties txnProperties = Config.loadPropertiesFile(path, TxnRoster.getProperty(key));
				txnConfigMap.put(key, txnProperties);
			} catch (IOException e) {
				logger.error(key + " 交易配置文件[" + path + TxnRoster.getProperty(key) + "]不存在,本文件加载失败");
				continue;
			}
		}
		logger.info("加载交易与交易配置文件对照关系的花名册:完成");
		return true;
	}
	
	/**
	 * 根据交易代号获取交易配置
	 * @param txnid
	 * @return
	 */
	public static Properties getTxnConfig (String txnid) {
		
		if(TxnRoster == null ||txnConfigMap == null){
			Init(); //尝试重新加载配置花名册
		}
		
		//获取交易配置
		Properties txnProperties = txnConfigMap.get(txnid);
		//交易配置不存在,尝试重新加载配置
		if(txnProperties == null){
			logger.warn(txnid + " 交易配置不存在,尝试重新加载");
			String txnConfigFile = TxnRoster.getProperty(txnid);
			if(null == txnConfigFile){
				logger.error(txnid + " 未在 " + path + TxnRosterFileName + "中配置,获取交易配置失败");
			}
			else{
				try {
					txnProperties = Config.loadPropertiesFile(path, TxnRoster.getProperty(txnid));
					txnConfigMap.put(txnid, txnProperties);
				} catch (IOException e) {
					logger.error(txnid + " 交易配置文件[" + path + TxnRoster.getProperty(txnid) + "]不存在,本文件加载失败");
				}
			}
		}
		
		return txnProperties;
	}

	/**
	 * 设置交易配置文件所在路径的接口
	 * @return
	 */
	public static void setPath(String path) {
		TxnConfigManager.path = path;
	}

	/**
	 * 设置交易花名册文件名称的接口
	 * @return
	 */
	public static void setTxnRosterFileName(String txnRosterFileName) {
		TxnRosterFileName = txnRosterFileName;
	}
	
	public static String getPath() {
		return path;
	}
	public static String getTxnRosterFileName() {
		return TxnRosterFileName;
	}
	
}
