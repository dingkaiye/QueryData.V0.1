package com.ods.manager;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;

/**
 * 登记并管理系统中交易数量
 * @author ding_kaiye
 *
 */
public class TxnCntContrl {
	private static int sysTxnCnt = 0 ;  //记录系统内交易数量 
	private static int MaxTxnCnt = 10000 ;  // 默认
//	private static int TimeOut = 60 ;  // 默认 60S 超时
	private static Logger logger = OdsLog.getLogger("SysLog");
	
	
	/**
	 * 不允此类被许实例化
	 */
	private TxnCntContrl(){
		
	}
	
	public synchronized static void init() {
		Properties SysConfig = null ;
		String maxTxnCntCfg = null ;
		try {
			SysConfig = Config.getPropertiesConfig(Constant.SysConfig);
		} catch (IOException e) {
			logger.error("读取系统配置文件 " + Constant.SysConfig + "失败");
		}
		
		maxTxnCntCfg = SysConfig.getProperty(Constant.MaxTxnCnt); //读取配置
//		TimeOut = SysConfig.getProperty(Constant.TimeOut); //读取配置
		if (maxTxnCntCfg != null ){
			MaxTxnCnt = new Integer( SysConfig.getProperty(Constant.MaxTxnCnt) );
			logger.error("读取系统配置成功, 系统并发交易上限当前设置为:" + MaxTxnCnt );
		}else {
			logger.error("读取系统配置失败, 系统并发交易上限当前设置为使用默认设置:" + MaxTxnCnt );
		}
	}
	
	/**
	 * 登记交易增加
	 * @throws TxnException 
	 */
	public synchronized static void addCnt() throws TxnException{
		if(sysTxnCnt + 1 > MaxTxnCnt) {  //接入次交易将大于系统上限, 不再累加, 返回false
			logger.error("交易量超过系统上限:" + MaxTxnCnt);
			throw new TxnException("交易量超过系统上限:" + MaxTxnCnt);
		} else {
			sysTxnCnt ++;
		}
	}
	
	/**
	 * 登记交易减少
	 * @throws TxnException 
	 */
	public synchronized static void reduceCnt() throws TxnException{
		if(sysTxnCnt - 1 < 0) {  // 系统中交易数量不可能小于 0 , 小于0, 则表明系统运行存在问题 
			sysTxnCnt = 0;
			logger.error("系统记录中登记交易数量小于0");
			throw new TxnException("系统记录中登记交易数量小于0");
		} else {
			sysTxnCnt --;
		}
	}
	

}
