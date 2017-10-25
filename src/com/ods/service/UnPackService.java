package com.ods.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.log.OdsLog;
import com.ods.manager.QueueManager;
import com.ods.message.TxnMessager;

/**
 * 解析ESB报文 
 * @author ding_kaiye
 *
 */
public class UnPackService extends Thread {
	
	private static Logger logger = OdsLog.getTxnLogger("UnPackService");
	
	/** 成功处理后, 转入的队列名称  */
	private String nextQueue = Constant.TxnQueue ;  //默认输出队列
	
	private int sleeptime = 50;
	
	private static Properties properties = null;
	String QueueName = null;
	final String confile = Constant.ESBPACK_CONFIG_FILE ;  //请求报文结构配置文件
	
	
	/**
	 * @param QueueName
	 *            输入队列名称
	 * @param nextQueueName
	 *            输出队列名称
	 * @throws IOException
	 */
	public UnPackService(String QueueName, String nextQueueName) throws IOException {
		this.QueueName = QueueName;
		if (nextQueueName != null && !"".equals(nextQueueName)) {
			this.nextQueue = nextQueueName;
		}
		logger.info("服务初始化完成, 输入队列:[" + this.QueueName + "] 输出队列:[" + this.nextQueue + "]");
		// 读取配置文件
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
		String SerialNo = null;
		TxnMessager txnMessager = null;
		while (true) {
			try { //从队列中获取 txnMessager
				txnMessager = QueueManager.SysQueuePoll(QueueName);
			} catch (Exception e) {
				logger.error("此次轮询" + QueueName + "出现异常, 稍后再次获取:" + e.getMessage());
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e1) {
					logger.warn("Thread.sleep InterruptedException");
				}
			}

			try { //解包服务 
				if (null != txnMessager) {
					SerialNo = txnMessager.getSerialNo(); // 获取 流水号
					if (txnMessager.getMsgStatus() != true) { // 获取交易状态
						logger.info("流水号" + SerialNo + " 状态为" + false + ", 交易不再处理");
						continue;
					}
					// 解包
					boolean result = false;
					try {
						// 保留此处结构,以便扩展, 根据不同报文, 调用对应解析 ->
						result = unpackEsbMsg(txnMessager); 
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("流水号" + SerialNo + " 解包出现异常:" + e.getLocalizedMessage()); 
						txnMessager.setMsg("解包失败");
						QueueManager.moveToFailQueue(txnMessager); // 转入失败队列
						continue;
					}
					
					if (result == true) {
						QueueManager.SysQueueAdd(nextQueue, txnMessager); // 解包完成, 转入下一队列
					} else {
						QueueManager.moveToFailQueue(txnMessager); // 解包失败,转入失败交易队列
					}
					
					continue;
					
				} else {
					try {
						logger.trace("此次轮询" + QueueName + "未获得待处理交易,稍后再次获取");
						Thread.sleep(sleeptime);
					} catch (InterruptedException e) {
						logger.debug("sleep has be Interrupted");
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logger.error("交易处理出现异常[" + SerialNo + "]" + e.getMessage());
				QueueManager.moveToFailQueue(txnMessager);
			}
		}
	}
	
	
	/**
	 * 解析ESB报文并保存到 TxnMessager
	 * @param txnMessager
	 * @return
	 */
	private boolean unpackEsbMsg(TxnMessager txnMessager) {
		
		String separator = ",";
		String SerialNo = txnMessager.getSerialNo() ; // 获取 流水号
		// 检查配置
//		Properties t = null; 
		if(properties == null) {
			//若配置不存在, 尝试重新加载配置
			try {
				logger.info(confile + "未加载,重新加载");	
				properties =  Config.loadConfigPropertiesFile(confile);
			} catch (IOException e) {
				logger.error(confile + "配置文件载入失败");	
				e.printStackTrace();
				return false;
			}
		}
		
		String xmlstring = txnMessager.getRequestMsg(); //获取ESB请求报文
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlstring);
		} catch (DocumentException e) {
			e.printStackTrace();
			txnMessager.setcurrent("unpack", "fail", "解析报文出现DocumentException异常:" + e.getMessage());
			return false;
		}
		
		Map<String, Object> reqMsg = txnMessager.getMapReqMsg(); //取得 messager 中记录 解析结果的 table
		//String PartsStr = properties.get(Constant.EsbReqPack); //取得报文结构定义
		logger.info(SerialNo + " 解包开始,待解析报文:" + xmlstring);
		// 取得报文 字段
		String[] parts = properties.getProperty(Constant.EsbReqPack).split(separator); //需要测试此处是否为null
		for(int i=0; i<parts.length; i++) {
			String partName = parts[i];
			String path = properties.getProperty(partName + "Path"); //取得本段报文路径
			if(null == path || "".trim().equals(path)) {
				logger.warn(partName + "节点对应路径" + partName + "Path 为空或未配置,跳过对" + partName + "的解析");
				continue;
			}
			
			String strColumns = properties.getProperty(partName);
			if(null == strColumns || "".trim().equals(strColumns)) {
				logger.warn(partName + "节点对应字段为空或未配置[" + strColumns + "],跳过对" + partName + "的解析");
				continue;
			}
			
			String[] columns = strColumns.split(separator); //取得字段列表
			logger.info("开始解析 " + partName + ", 包含字段:" +  properties.getProperty(partName) );
			
			if (columns.length == 0) {
				continue;
			}
			for (int j=0; j<columns.length; j++) {
				//根据路径取得元素
				String columnName = partName + "." + columns[j];
				String columnRelativePath = null;
				try{
					String strColumnRelativePath = properties.getProperty(columnName);
					if(strColumnRelativePath == null || "".equals(strColumnRelativePath)){
						logger.warn( columnName + "对应Path为空或未配置, 跳过对" + columnName + "的解析" );
						continue;
					}else{
						columnRelativePath = strColumnRelativePath.split("#")[0].split(",")[0].trim(); //取得相对路径
					}
				}catch (Exception e){
					logger.error("获取" + columnName + "对应Path出错,跳过" , e );
					continue;
				}
				
//				logger.debug("开始获取" + columnName);
				try{
					List<Node> elements = document.selectNodes(path + columnRelativePath);  // 取得节点 
					if(elements.size() == 1) {                      // 仅有一个节点情况, 直接存储为字符串
						logger.debug("YYYYYYYYYYYYYYYYYYYYYY");
						Element element = (Element) elements.get(0);
						logger.info("流水号 " + SerialNo + " 中解析出 " + columnName + " 为 [" + element.getText() + "]");	
						reqMsg.put(columnName, element.getText());
					} else if (elements.size() == 0) {
//						reqMsg.put(columnName, null);
						logger.info("流水号 " + SerialNo + " 中解析出 " + columnName + "为 NULL ");	
					} else {                                        // 多个节点情况, 使用 List 结构存放 
						reqMsg.put(columnName, elements);
						for(int k=0; k<elements.size(); k++) {
							Element element = (Element) elements.get(k);
							logger.info("流水号 " + SerialNo + " 中解析出 " + columnName +" [" + k + "]" + " 为 :[" + element.getText() + "]");	
						}
					}
				}catch (Exception e){
					logger.debug("开始获取" + columnName + "错误", e);
					throw e;
				}
				logger.debug("获取完成" + columnName);
			}
		}
		
		//txnMessager 中 交易代号 字段赋值 
		String txnIdConfig = null;
		try {
			txnIdConfig = (String) properties.getProperty("TxnId"); // 从配置文件中获取交易代号字段在ESB报文中的位置     
			if (txnIdConfig == null || "".equals(txnIdConfig)) {
				logger.warn( confile + "文件中交易代号未配置");
			} else {
				Element el = (Element) document.selectSingleNode(txnIdConfig);
				if(el == null){
					logger.error(SerialNo + "交易代号获取失败,\n查找路径[" + txnIdConfig + "]\n报文[" + document.asXML() + "]");
				}
				txnMessager.setTxnId(el.getText());
				logger.info( SerialNo + " 交易代号为: " + txnMessager.getTxnId() );
			}
		} catch (Exception e) {
			logger.error("获取交易代号失败", e);
		}
		
		//EsbReqBody 特殊处理 , EsbReqBody 对应的 Element 元素加入到解包结果中
		try {
			//String reqBody = Constant.EsbReqBody;
			String reqBodyPath = properties.getProperty(Constant.EsbReqBody + "Path");
			if (reqBodyPath == null || "".equals(reqBodyPath)) {
				logger.warn("配置文件中" + Constant.EsbReqBody + "Path 未配置,获取 " + Constant.EsbReqBody + " 失败");
			} else {
				if (reqBodyPath.endsWith("/") ||reqBodyPath.endsWith("\\")){
					reqBodyPath = reqBodyPath.substring(0, reqBodyPath.length() -1 ); 
				}
				Element el = (Element) document.selectSingleNode(reqBodyPath);
				reqMsg.put(Constant.EsbReqBody, el);  //特殊处理 , ReqBody 以 Element 类型加入
			}
		} catch (Exception e) {
			logger.error("获取EsbReqBody失败", e);
		}
		
		// 根据交易代码, 解析入参到 InParm 中 (解包操作不具体处理, TxnService中具体解析)
		
		logger.info(SerialNo + "解包完成" );
		return true;
	}
	

}
