package com.ods.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.db.DbDataLine;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.manager.QueueManager;
import com.ods.manager.TxnConfigManager;
import com.ods.message.QueryMessager;
import com.ods.message.TxnMessager;
import com.ods.transaction.ITransaction;
import com.ods.ws.TxnBody;

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
	
	/** 交易处理类名称  **/
	private final String txnClassPropertie = "ClassNeme" ;
	/** 请求报文中 body 实现类 配置 */
	private final String repClassPropertie = "ReqBody" ; 
	/** 返回报文中 body 实现类 配置 */
	private final String rspClassPropertie = "RspBody" ;   
	private int sleeptime = 50;
	
//	Properties properties = null;
	String QueueName = null;
//	String confile = "TxnManager.properties" ;
	
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
		
// 20171120  delete : 不再分散管理,面向交易管理,交易具体实现类配置信息放入交易对应的配置文件中 
//		//读取配置文件
//		String path = Constant.TxnConfigPath;  //默认文件存放在此目录下
//		String TxnRosterFileName =  Constant.TxnRosterFileName ; //交易花名册文件的默认文件名
//		try {
//			properties = Config.loadConfigPropertiesFile(path + TxnRosterFileName);
//		} catch (IOException e) {
//			logger.info(path + TxnRosterFileName + "配置文件载入失败");	
//			e.printStackTrace();
//			throw e;
//		}
	}
	
	@Override
	public void run() {
		
		String txnName = null;
		
		String txnClassName = null;
//		String reqBodyClassName = null;
//		String rspBodyClassName = null;
		
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
						logger.error("交易代号不能为空,请检查:流水号:" + SerialNo); 
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
						QueueManager.moveToFailQueue(txnMessager, "获取交易配置出错" ); //转入失败交易队列
						continue;
					}
					if (txnProperties == null) {
						logger.error("流水号" + SerialNo + "对应的交易" + txnName +  "不存在" ); 
						QueueManager.moveToFailQueue(txnMessager, "流水号:" + SerialNo + "对应的交易:" + txnName +  "不存在" ); //转入失败交易队列
						continue;
					}
					//3.  获取RspBody中的入参加入到inParm列表中
					// 20171129 重写  将 请求报文中 的 body 直接传递给 交易 
					
//					Map<String, Object> inParm = txnMessager.getInParm();
//					try {
//						inParm.clear();
//						Element reqBody = (Element) txnMessager.getMessageIn();
//						if(null == reqBody){
//							QueueManager.moveToFailQueue(txnMessager, Constant.EsbReqBody + "为 NULL");
//							throw(new Exception(Constant.EsbReqBody + "为 NULL"));
//						}
//						logger.debug("ReqBody:[" + reqBody.asXML() + "]");
//						//根据配置参数 获取输入报文中 InParm 的数值
//						//20171011 重写
//						String inParms = txnProperties.getProperty(Constant.inParm);
//						for(String parmName : inParms.split(",")){
//							String parmPath = txnProperties.getProperty(Constant.inParm + "." + parmName).split("#")[0].split(",")[0].trim();   
//							String parmValue = reqBody.valueOf(parmPath);
//							//Node elements = reqBody.selectSingleNode(parmPath);
//							//String parmValue = elements.getText();
//							inParm.put(parmName, parmValue);
//							logger.debug(SerialNo + " " + Constant.inParm + "增加:[" + parmName + " ][" + parmValue + "]");
//						}
//					} catch (Exception e) {
//						logger.error(txnName + "流水号" + SerialNo + "组织inParm出错" + e.getMessage());
//						QueueManager.moveToFailQueue(txnMessager); // 转入失败交易队列
//						continue;
//					}
//					logger.info(txnName + "流水号" + SerialNo + "获取RspBody中的入参加入到inParm列表完成");
					

					//4. 根据 properties 配置, 实例化 交易对应的处理类  (此处可以优化,可进一步拆分和扩展)
					// className = (String) properties.getProperty(txnName);  // 20171120 使用配置放入具体交易中
					txnClassName = (String) txnProperties.getProperty(txnClassPropertie);
					
					// 检查是否配置 
					if (txnClassName == null || "".equals(txnClassName.trim()) ) {
						logger.info("获取交易处理类出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在");
						txnMessager.setMsg("获取交易处理类出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在"); 
						QueueManager.moveToFailQueue(txnMessager);
						continue;
					}
					logger.debug("获取的交易名称是:" + txnName + " 交易处理类名为:[" + txnClassName + "]");

//					reqBodyClassName = (String) txnProperties.getProperty(repClassPropertie);
//					rspBodyClassName = (String) txnProperties.getProperty(rspClassPropertie);
//
//					if (reqBodyClassName == null || "".equals(reqBodyClassName.trim()) ) {
//						logger.info("获取请求Body配置出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在");
//						txnMessager.setMsg("获取请求Body配置出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在"); 
//						QueueManager.moveToFailQueue(txnMessager);
//						continue;
//					}
//					if (rspBodyClassName == null || "".equals(rspBodyClassName.trim()) ) {
//						logger.info("获取响应Body配置出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在");
//						txnMessager.setMsg("获取响应Body配置出错, 交易代号:" + txnName + " 流水号" + SerialNo + ", 配置不存在"); 
//						QueueManager.moveToFailQueue(txnMessager);
//						continue;
//					}
//					logger.debug("获取的交易名称是:" + txnName + " 交易处理类名为:[" + txnClassName + "]"
//							+ "请求Body: [" + reqBodyClassName + "]"
//							+ "响应Body: [" + rspBodyClassName + "]");

					ITransaction instance = null;
					try {
						instance = (ITransaction) Class.forName(txnClassName).newInstance();
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
						logger.error(txnClassName + "获取交易处理对象时出现异常", e);
						QueueManager.moveToFailQueue(txnMessager); // 转入失败交易队列
					}
					logger.info(txnName + "流水号" + SerialNo + "实例化 交易对应的处理类完成");
					
					//5. 启动数据库操作交易处理
					QueryMessager resultMessager = null;
					
					TxnBody txnBody = txnMessager.getMessageIn().getBody();
					try {
						resultMessager = instance.transaction(txnBody, SerialNo);
					} catch (TxnException e) { 
						logger.error("交易号:" + txnName + "流水号" + SerialNo + "TxnException 异常:" + e.getLocalizedMessage());
						//获取错误代码
						txnMessager.setReturnCode(e.getErrorCode());
						//获取错误信息
						txnMessager.setMsg(e.getMessage());
						//转入失败消息队列
						QueueManager.moveToFailQueue(txnMessager);  //转入失败交易队列
						continue;
					} catch (SQLException e) {
						logger.error(txnName + "流水号" + SerialNo + "SQLException 异常:" + e.getLocalizedMessage());
						//获取错误信息
						txnMessager.setMsg(e.getMessage());
						//转入失败消息队列
						QueueManager.moveToFailQueue(txnMessager);  //转入失败交易队列
						continue;
					}
					
					logger.info(txnName + "流水号" + SerialNo + "数据库操作交易处理完成");
					
					// 判断交易是否成功 	
					if(resultMessager.getResult() == false) {
						// 交易失败, 开始交易失败的处理
						txnMessager.setReturnCode(resultMessager.getReturnCode());
						//获取错误信息
						txnMessager.setMsg(resultMessager.getMsg());
						//转入失败消息队列
						QueueManager.moveToFailQueue(txnMessager);  //转入失败交易队列
						continue;
					}
					
					String keyDefine = (String) txnProperties.getProperty(Constant.keyDefine);
					//6. 获取查询结果, 并根据定义生成key值  
//					try{
//						ArrayList<DbDataLine> ResultList = resultMessager.getResultList();
//						int errCnt = 0;
//						for(DbDataLine dl : ResultList){
//							txnProperties.getProperty(Constant.keyDefine);  //获取交易配置中 key 值的配置信息
//							try {
//								dl.generateKeyValue(keyDefine); // 生成 Key值
//							} catch (Exception e) {
//								errCnt ++;
//								logger.error(txnName + "流水号" + SerialNo + "第" + errCnt + "行生成KeyValue出错");
//							}
//						}
//						if(errCnt == 0){
							txnMessager.setResultHead(resultMessager.getResultHead());
							txnMessager.setResultList(resultMessager.getResultList());
//						}else{
//							throw new Exception(txnName + "流水号" + SerialNo + "生成KayValue出错");
//						}
//					}catch(Exception e){
//						logger.error(txnName + "流水号" + SerialNo + "异常:" + e.getLocalizedMessage()); 
//						QueueManager.moveToFailQueue(txnMessager, "系统错误, 请稍候重试:" + e.getLocalizedMessage());
//						continue;
//					}
//					logger.info(txnName + "流水号" + SerialNo + "生成key值 完成");
					
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
