package com.ods.manager;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.db.DbPool;
import com.ods.exception.TxnException;
import com.ods.log.LogDateCheckService;
import com.ods.log.OdsLog;
import com.ods.service.EsbPackService;
import com.ods.service.SendFailMsgService;
import com.ods.service.SendSuccMsgService;
import com.ods.service.TxnService;
import com.ods.service.UnPackService;


public class InitSystem {
	
	private static Logger logger = OdsLog.getLogger("SysLog"); 
	private static int timeOut = 60 * 1000 ;
	
	public static void main (String[] args) throws Exception {
		try {
			QueueManager.QueueInit(); // 初始化队列

			// 初始化数据库连接池
			DbPool.init();

			// 加载 TxnConfig
			TxnConfigManager.Init();

			// 启动日志日期监控
			Thread logDateCheckService = new LogDateCheckService();
			logDateCheckService.start();

			// 读取配置文件
			Properties SysConfig = null;
			try {
				SysConfig = Config.getPropertiesConfig(Constant.SysConfig);
			} catch (IOException e) {
				logger.error("启动失败, 读取队列配置文件" + Constant.SysConfig + "失败");
				throw e;
			}

			// 获取系统级交易超时时间
			try {
				timeOut = new Integer(SysConfig.getProperty("TxnTimeOut")); // 获取交易时间,
																			// 供系统交易使用
			} catch (Exception e) {
				timeOut = 60 * 1000;
				logger.warn("系统交易超时时间获取出错, 设置为默认时间" + timeOut / 1000);
			}

			// 读取服务线程数配置
			String UnPackService = SysConfig.getProperty("UnPackService");
			String TxnService = SysConfig.getProperty("TxnService");
			String EsbPackService = SysConfig.getProperty("EsbPackService");
			String SendSuccMsgService = SysConfig.getProperty("SendSuccMsgService");
			String SendFailMsgService = SysConfig.getProperty("SendFailMsgService");

			// 检查服务线程数配置
			if ("".equals(UnPackService) || UnPackService == null) {
				logger.error("UnPackService 配置为空或为配置, 系统初始化失败");
				throw new TxnException("UnPackService 配置为空或为配置, 系统初始化失败");
			}
			if ("".equals(TxnService) || TxnService == null) {
				logger.error("TxnService 配置为空或为配置, 系统初始化失败");
				throw new TxnException("TxnService 配置为空或为配置, 系统初始化失败");
			}
			if ("".equals(EsbPackService) || EsbPackService == null) {
				logger.error("EsbPackService 配置为空或为配置, 系统初始化失败");
				throw new TxnException("EsbPackService 配置为空或为配置, 系统初始化失败");
			}
			if ("".equals(SendSuccMsgService) || SendSuccMsgService == null) {
				logger.error("SendSuccMsgService 配置为空或为配置, 系统初始化失败");
				throw new TxnException("SendSuccMsgService 配置为空或为配置, 系统初始化失败");
			}
			if ("".equals(SendFailMsgService) || SendFailMsgService == null) {
				logger.error("SendFailMsgService 配置为空或为配置, 系统初始化失败");
				throw new TxnException("SendFailMsgService 配置为空或为配置, 系统初始化失败");
			}

			// 实例化服务线程

			int threadcnt = 0;
			String thredName = null;

			threadcnt = new Integer(UnPackService);
			thredName = "";
			for (int i = 0; i < threadcnt; i++) {
				UnPackService service = new UnPackService(Constant.UnpackQueue, Constant.TxnQueue);
				service.setName("UnPackService-" + i);
				service.start();
				thredName = thredName + " " + service.getName();
			}
			logger.info("UnPackService 启动成功, 启动线程数:" + threadcnt + " 线程名 " + thredName);

			threadcnt = new Integer(TxnService);
			thredName = "";
			for (int i = 0; i < threadcnt; i++) {
				TxnService service = new TxnService(Constant.TxnQueue, Constant.PackQueue);
				service.setName("TxnService-" + i);
				service.start();
				thredName = thredName + " " + service.getName();
			}
			logger.info("TxnService 启动成功, 启动线程数:" + threadcnt + " 线程名 " + thredName);

			threadcnt = new Integer(EsbPackService);
			thredName = "";
			for (int i = 0; i < threadcnt; i++) {
				EsbPackService service = new EsbPackService(Constant.PackQueue, Constant.SuccessQueue);
				service.setName("EsbPackService-" + i);
				service.start();
				thredName = thredName + " " + service.getName();
			}
			logger.info("EsbPackService 启动成功, 启动线程数:" + threadcnt + " 线程名 " + thredName);

			threadcnt = new Integer(SendSuccMsgService);
			thredName = "";
			for (int i = 0; i < threadcnt; i++) {
				SendSuccMsgService service = new SendSuccMsgService(Constant.SuccessQueue, null);
				service.setName("SendSuccMsgService-" + i);
				service.start();
				thredName = thredName + " " + service.getName();
			}
			logger.info("SendSuccMsgService 启动成功, 启动线程数:" + threadcnt + " 线程名: " + thredName);

			threadcnt = new Integer(SendFailMsgService);
			thredName = "";
			for (int i = 0; i < threadcnt; i++) {
				SendFailMsgService service = new SendFailMsgService(Constant.FailQueue, null);
				service.setName("SendFailMsgService-" + i);
				service.start();
				thredName = thredName + " " + service.getName();
			}
			logger.info("SendFailMsgService 启动成功, 启动线程数:" + threadcnt + " 线程名 " + thredName);

			// 获取本机 IP 地址

			String localname = null; // 主机名称
			// try {
			// InetAddress inetAddress = InetAddress.getLocalHost();
			// localname = inetAddress.getHostName();
			// localip = inetAddress.getHostAddress();
			// logger.info("本机名称是:"+ localname);
			// logger.info("本机的ip是:" + localip);
			// } catch (Exception e) {
			// logger.error("启动失败, 获取本机IP地址失败");
			// throw new TxnException("启动失败, 获取本机IP地址失败");
			// }

			// 读取配置参数中的 IP, port 配置
			String localip = SysConfig.getProperty("WebServiceIP");
			if ("".equals(localip) || localip == null) {
				logger.error("WebServiceIP 配置为空或未配置, 系统初始化失败");
				throw new TxnException("WebServiceIP 配置为空或未配置, 系统初始化失败");
			}
			String port = SysConfig.getProperty("WebServicePort");
			if ("".equals(port) || port == null) {
				logger.error("WebServicePort 配置为空或未配置, 系统初始化失败");
				throw new TxnException("WebServicePort 配置为空或未配置, 系统初始化失败");
			}

			// 发布webservice
			try {
				Endpoint.publish("http://" + localip + ":" + port + "/queryods", new Handler());
				logger.info("WebService发布完成, 地址:" + "http://" + localip + ":" + port + "/queryods");
			} catch (Exception e) {
				logger.error("WebService发布失败, 地址:" + "http://" + localip + ":" + port + "/queryods", e);
				throw new TxnException("WebService发布失败, 地址:" + "http://" + localip + ":" + port + "/queryods");
			}
			logger.info("系统启动成功");
		} catch (Exception e) {
			logger.error("系统启动失败, 进程退出");
			System.exit(-1);
		}

	}

	public static int getTimeOut() {
		return timeOut;
	}

	public static void setTimeOut(int timeOut) {
		InitSystem.timeOut = timeOut;
	}

}
