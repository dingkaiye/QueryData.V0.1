package com.ods.manager;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.EndpointImpl;
//import org.apache.cxf.frontend.ServerFactoryBean;
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
import com.ods.ws.ArtifactInInterceptor;
import com.ods.ws.ArtifactOutInterceptor;
import com.ods.ws.ESBWaiter;


import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;


public class InitSystem {
	
	private static Logger logger = OdsLog.getLogger("SysLog"); 
	
	public static void main (String[] args)  {
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

			// 初始化系统交易量控制
			TxnCntContrl.init();
			
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

			String wsUrl = SysConfig.getProperty("WebServiceUrl");
			if ("".equals(wsUrl) || wsUrl == null) {
				logger.error("WebServiceUrl 配置为空或未配置, 系统初始化失败");
				throw new TxnException("WebServiceUrl 配置为空或未配置, 系统初始化失败");
			}

			// 发布webservice
			try {
				ArtifactInInterceptor artifactInInterceptor = new ArtifactInInterceptor();
				ArtifactOutInterceptor artifactOutInterceptor = new ArtifactOutInterceptor();
				ESBWaiter waiter = new ESBWaiter();
				
				EndpointImpl endpointImpl = (EndpointImpl) Endpoint.publish(wsUrl, waiter);
				//添加拦截器 
				endpointImpl.getInInterceptors().add(artifactInInterceptor);
				
				List<Interceptor<? extends Message>> inInterceptors =  endpointImpl.getOutInterceptors();
				inInterceptors.add(artifactOutInterceptor) ;
				System.out.println(endpointImpl);
				
				logger.info("WebService发布完成, 地址:" + wsUrl);
			} catch (Exception e) {
				logger.error("WebService发布失败, 地址:" + wsUrl, e);
				throw new TxnException("WebService发布失败, 地址:" + wsUrl);
			}
			
			logger.info("系统启动成功");
		} catch (Exception e) {
			logger.error("系统启动失败, 进程退出");
			System.exit(-1);
		}

	}

}
