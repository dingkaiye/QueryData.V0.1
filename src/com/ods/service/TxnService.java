package com.ods.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.db.DbDataLine;
import com.ods.log.OdsLog;
import com.ods.manager.QueueManager;
import com.ods.manager.TxnConfigManager;
import com.ods.message.QueryMessager;
import com.ods.message.TxnMessager;
import com.ods.transaction.ITransaction;

/**
 * 交易管理分配类, 系统级交易相关处理
 * @author ding_kaiye
 * @date 2017-09-23
 */
public class TxnService extends Thread {
	
	
	// ArrayBlockingQueue
	private static Logger logger = OdsLog.getTxnLogger("TxnService");
	
	/** 成功处理后, 转入的队列名称  */
	private String nextQueue = Constant.PackQueue ;
	
	private int sleeptime = 50;
	
	Properties properties = null;
	String QueueName = null;
	String confile = "TxnManager.properties" ;
	
 /**
  * @param QueueName   输入队列名称
  * @param nextQueueName  输出队列名称
  * @throws IOException
  */
	public TxnService (String QueueName, String nextQueueName ) throws IOException {
		this.QueueName = QueueName;
		if(nextQueueName != null && ! "".equals(nextQueueName)){
			this.nextQueue = nextQueueName ; 
		}
		logger.info("服务初始化完成, 输入队列:[" + this.QueueName + "] 输出队列:[" + this.nextQueue + "]");	
		
		
		//读取配置文件
		try {
			properties = Config.loadConfigPropertiesFile(confile);
		} catch (IOException e) {
			logger.info(confile + "配置文件载入失败");	
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void run() {
		
		String txnName = null;
		String className = null;
		String SerialNo = null;
		while (true) {
			// 查询对应交易
			TxnMessager txnMessager = null;
			try {
				txnMessager = QueueManager.SysQueuePoll(QueueName);
			} catch (Exception e) {
				logger.error("此次轮询" + QueueName + "出现异常, 稍后再次获取:" + e.getMessage());
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e1) {
					logger.warn("Thread.sleep Interrupted");
				}
			}
			try {
				if (null != txnMessager) {
					txnName = txnMessager.getTxnId(); // 获取交易代号
					SerialNo = txnMessager.getSerialNo(); // 获取 流水号
					logger.info(txnName + "流水号" + SerialNo + "处理开始");
					
					//1.  检查交易代号是否存在
					if (txnName == null ||  "".equals(txnName) ) {
						logger.error("交易代号不能为空,请检查:流水号:" + SerialNo ); 
						txnMessager.setMsg("交易代号不能为空"); 
						QueueManager.moveToFailQueue(txnMessager);
						continue;
					}
					if (txnMessager.getMsgStatus() != true) { // 获取交易状态
						logger.info("交易:" + txnName + " 流水号" + SerialNo + " 状态为" + false + ", 交易不再处理");
						continue;
					}

					//2.  读取配置文件, 获取入参
					Properties txnProperties = null;
					try {
						txnProperties = TxnConfigManager.getTxnConfig(txnName) ;
					} catch (Exception e) {
						logger.error(txnName + "流水号" + SerialNo + "获取交易配置出错" , e); 
						QueueManager.moveToFailQueue(txnMessager); //转入失败交易队列
						continue;
					}
					
					//3.  获取RspBody中的入参加入到inParm列表中
					Map<String, Object> inParm = txnMessager.getInParm();
					try {
						inParm.clear();
						Element reqBody = (Element) txnMessager.getMapReqMsg().get(Constant.EsbReqBody);
						if(null == reqBody){
							QueueManager.moveToFailQueue(txnMessager, Constant.EsbReqBody + "为 NULL");
							throw(new Exception(Constant.EsbReqBody + "为 NULL"));
						}
						//根据配置参数 获取输入报文中 InParm 的数值
						//20171011 重写
//						RspBody=AcctId,StartDt,EndDt,PageSize
//						RspBody.AcctId=AcctId,0       # 账号
//						RspBody.StartDt=StartDt,0     # 开始日期
//						RspBody.EndDt=EndDt,0         # 结束日期
//						RspBody.Page=Page,0           # 当前页码
//						RspBody.PageSize=PageSize,0   # 每页记录数
						String RspBody = txnProperties.getProperty(Constant.inParm);
						for(String parmName : RspBody.split(",")){
							String parmPath = txnProperties.getProperty(Constant.inParm + "." + parmName).split("#")[0].split(",")[0].trim();   
							String parmValue = reqBody.valueOf(parmPath);
							//Node elements = reqBody.selectSingleNode(parmPath);
							//String parmValue = elements.getText();
							inParm.put(parmName, parmValue);
						}
					} catch (Exception e) {
						logger.error(txnName + "流水号" + SerialNo + "组织inParm出错" + e.getMessage());
						QueueManager.moveToFailQueue(txnMessager); // 转入失败交易队列
						continue;
					}
					logger.info(txnName + "流水号" + SerialNo + "获取RspBody中的入参加入到inParm列表完成");
					

					//4. 根据 TxnManager.properties 配置, 实例化 交易对应的处理类  // 此处可以优化
					className = (String) properties.getProperty(txnName);
					// 判断该交易系统是否支持, 不支持则舍弃
					if (className == null) {
						logger.info("获取交易处理类出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 交易不存在");
						txnMessager.setMsg("交易代号:" + txnName + "对应交易不存在"); 
						QueueManager.moveToFailQueue(txnMessager);
						continue;
					}
					
					logger.debug("获取的交易名称是:" + txnName + " 交易处理类名为:" + className);
					ITransaction instance = null;
					try {
						instance = (ITransaction) Class.forName(className).newInstance();
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						logger.error(className + "获取交易处理对象时出现异常", e);
						QueueManager.moveToFailQueue(txnMessager); // 转入失败交易队列
					}
					logger.info(txnName + "流水号" + SerialNo + "实例化 交易对应的处理类");
					
					//5. 启动数据库操作交易处理
					QueryMessager result = null;
					try {
						result = instance.transaction(inParm);
					} catch (Exception e) { 
						logger.error(txnName + "流水号" + SerialNo + "异常:" + e.getLocalizedMessage());
						//获取错误代码
						txnMessager.setReturnCode(result.getReturnCode());
						//获取错误信息
						txnMessager.setMsg(result.getMsg());
						//转入失败消息队列
						QueueManager.moveToFailQueue(txnMessager);  //转入失败交易队列
						continue;
					}
					logger.info(txnName + "流水号" + SerialNo + "数据库操作交易处理完成");
					
					// 判断交易是否成功 	
					if(result.getResult() == false) {
						// 交易失败, 开始交易失败的处理
						txnMessager.setReturnCode(result.getReturnCode());
						//获取错误信息
						txnMessager.setMsg(result.getMsg());
						//转入失败消息队列
						QueueManager.moveToFailQueue(txnMessager);  //转入失败交易队列
						continue;
					}
					
					String keyDefine = (String) txnProperties.getProperty(Constant.keyDefine);
					//6. 获取查询结果, 并根据定义生成key值  
					try{
						ArrayList<DbDataLine> ResultList = result.getResultList();
						int errCnt = 0;
						for(DbDataLine dl : ResultList){
							txnProperties.getProperty(Constant.keyDefine);  //获取交易配置中 key 值的配置信息
							try {
								dl.generateKeyValue(keyDefine); // 生成 Key值
							} catch (Exception e) {
								errCnt ++;
								logger.error(txnName + "流水号" + SerialNo + "第" + errCnt + "行生成KeyValue出错");
							}
						}
						if(errCnt == 0){
							txnMessager.setResultHead(result.getResultHead());
							txnMessager.setResultList(result.getResultList());
						}else{
							throw new Exception(txnName + "流水号" + SerialNo + "生成KayValue出错");
						}
					}catch(Exception e){
						logger.error(txnName + "流水号" + SerialNo + "异常:" + e.getLocalizedMessage()); 
						QueueManager.moveToFailQueue(txnMessager, "系统错误, 请稍候重试:" + e.getLocalizedMessage());
						continue;
					}
					logger.info(txnName + "流水号" + SerialNo + "生成key值 完成");
					
					QueueManager.SysQueueAdd(nextQueue, txnMessager); // 处理完成, 转入下一队列
					logger.info(txnName + "流水号" + SerialNo + "处理完成");
					
					// 继续处理下一交易
					continue;
					
				} else {
					try {
						logger.trace("此次轮询" + QueueName + "未获得待处理交易,稍后再次获取");
						Thread.sleep(sleeptime);
					} catch (InterruptedException e) {
						logger.debug("sleep has be Interrupted", e);
					}
				}

			} catch (Exception e) {
				logger.error("交易处理出现异常[" + SerialNo + "]" + e.getMessage());
				QueueManager.moveToFailQueue(txnMessager, "系统错误, 请稍候重试" + e.getMessage() );
				continue;
			}
		}
	}
	

	
}
