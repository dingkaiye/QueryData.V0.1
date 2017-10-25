package com.ods.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.db.DbDataLine;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.manager.QueueManager;
import com.ods.manager.TxnConfigManager;
import com.ods.message.TxnMessager;

public class EsbPackService extends Thread {
	
	/**
	 * 组织ESB报文, 此服务处理正常状态的交易  
	 * @author ding_kaiye
	 * @ 2017-09-23
	 */
		
	private static Logger logger = OdsLog.getTxnLogger("PackService");

	/** 成功处理后, 转入的队列名称(默认值) */
	private String nextQueue = Constant.SuccessQueue; //默认
	
	private int sleeptime = 50;

	Properties properties = null;
	String QueueName = null;
	final String confile = Constant.ESBPACK_CONFIG_FILE; // 请求报文结构配置文件

    /**
    * @param QueueName 输入队列名称
    * @param nextQueueName 输出队列名称
    * @throws IOException
    */
	public EsbPackService(String QueueName, String nextQueueName) throws IOException {
		this.QueueName = QueueName;
		if (nextQueueName != null && ! "".equals(nextQueueName)) {
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
			try { // 从队列中获取 txnMessager
				txnMessager = QueueManager.SysQueuePoll(QueueName);
			} catch (Exception e) {
				logger.error("此次轮询" + QueueName + "出现异常, 稍后再次获取:" + e.getMessage());
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e1) {
					logger.warn("Thread.sleep InterruptedException");
				}
			}

			try { // 组包
				if (null != txnMessager) {
					SerialNo = txnMessager.getSerialNo(); // 获取 流水号
					if (txnMessager.getMsgStatus() != true) { // 获取交易状态
						logger.info("流水号" + SerialNo + " 状态为" + false + ", 交易不再处理");
						continue;
					}
					// 组包
					try {
						packEsbMsg(txnMessager); // 保留此处结构,以便扩展
					} catch (TxnException e) { // 交易级错误
						logger.error("流水号" + SerialNo + " 组包出错:" + e.getMessage(), e);
						QueueManager.moveToFailQueue(txnMessager); // 转入失败队列
						continue;
					} catch (Exception e) {
						logger.error("流水号" + SerialNo + " 组包出现Exception异常:", e);
						QueueManager.moveToFailQueue(txnMessager); // 转入失败队列
						continue;
					}

					QueueManager.SysQueueAdd(nextQueue, txnMessager); // 组包完成,
																		// 转入下一队列
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
		 * 组包到XML 
		 * @param txnMessager
		 * @throws TxnException
		 * @date 2017-09-25
		 */
		private  void packEsbMsg(TxnMessager txnMessager) throws TxnException {
	        
			String xmlTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			String SerialNo = txnMessager.getSerialNo() ; 
			logger.debug(SerialNo + " 开始组织应答XML包");
			
			//用于格式化xml内容和设置头部标签
			OutputFormat format = OutputFormat.createPrettyPrint();
			//设置xml文档的编码为utf-8
			format.setEncoding("utf-8");
			
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("UTF-8");
			
			Element root = document.addElement("soapenv:Envelope");
			root.add(DocumentHelper.createNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/") );
			root.add(DocumentHelper.createNamespace("s", "http://esb.dcitsbiz.com/services/S030010001") ); 
			
//			root.setEncoding("UTF-8"); 
			Element soapHead = DocumentHelper.createElement("soapenv:Header");
			Element soapBody = DocumentHelper.createElement("soapenv:Body");

			Element S03001000101 = DocumentHelper.createElement("s:RspS03001000101");
			Element SysHhead = DocumentHelper.createElement("s:ReqSysHead");
			Element AppHead = DocumentHelper.createElement("s:ReqAppHead");
			Element LocalHead = DocumentHelper.createElement("s:ReqLocalHead");
			Element RspBody = DocumentHelper.createElement("s:RspRspBody");
			
			root.add(soapHead);
			root.add(soapBody);
			soapBody.add(S03001000101);
			S03001000101.add(SysHhead);
			S03001000101.add(AppHead);
			S03001000101.add(LocalHead);
			S03001000101.add(RspBody);
			
            /** 组 Syshead */
			packSyshead(SysHhead, txnMessager);
			logger.debug(SerialNo + "组织 SysHhead 完成,共3步,当前完成第1步");
            
    		/** 组 AppHead */
			packAppHead(AppHead, txnMessager);
			logger.debug(SerialNo + "组织 AppHead 完成,共3步,当前完成第2步");
    		
    		/** 组RspBody */
			packRspBody(RspBody, txnMessager);
			logger.debug(SerialNo + "组织 RspBody 完成,共3步,当前完成第3步");
    		
			logger.debug("  " + root.asXML() + "\n");
			
			// <?xml version=\"1.0\" encoding=\"UTF-8\"?> 暂时用 拼接的方式添加吧 
			txnMessager.setRspMsg(xmlTitle + root.asXML());
		}
		
		
		/** 组 Syshead */
		private  Element packSyshead(Element SysHead, TxnMessager txnMessager) throws TxnException {
			Element SvcId          = SysHead.addElement("s:SvcId"         );  // 服务代码
			Element SvcScn         = SysHead.addElement("s:SvcScn"        );  // 服务场景
			Element SvcSpltTxnCd   = SysHead.addElement("s:SvcSpltTxnCd"  );  //服务提供方交易代码
			Element SvcCstTxnCd    = SysHead.addElement("s:SvcCstTxnCd"   );  //调用交易方交易代码
			Element MAC            = SysHead.addElement("s:MAC"           );  // MAC信息
			Element CnsmrSysId     = SysHead.addElement("s:CnsmrSysId"    );  //服务调用方系统代号
			Element Vrsn           = SysHead.addElement("s:Vrsn"          );  //服务器版本号
			Element CnsmrSeqNo     = SysHead.addElement("s:CnsmrSeqNo"    );  // 服务调用方系统流水号
			Element SvcSplrSysId   = SysHead.addElement("s:SvcSplrSysId"  );  // 服务提供方系统编号
			Element SvcSplrSeqNo   = SysHead.addElement("s:SvcSplrSeqNo"  );  // 服务提供方流水号
			Element TxnDt          = SysHead.addElement("s:TxnDt"         );  //交易日期
			Element TxnTm          = SysHead.addElement("s:TxnTm"         );  //交易时间
			Element AcgDt          = SysHead.addElement("s:AcgDt"         );  //会计日期
			Element SvcSplrSvrId   = SysHead.addElement("s:SvcSplrSvrId"  );  // 服务提供方服务器标识
			Element TxnChnlTp      = SysHead.addElement("s:TxnChnlTp"     );  // 渠道类型
			Element ChnlNo         = SysHead.addElement("s:ChnlNo"        );  //渠道编号
			Element TxnTmlId       = SysHead.addElement("s:TxnTmlId"      );  //终端号
			Element OrigCnsmrId    = SysHead.addElement("s:OrigCnsmrId"   );  // 服务原发起方系统编号
			Element OrigCnsmrSeqNo = SysHead.addElement("s:OrigCnsmrSeqNo");  // 全局业务流水号 
			Element OrigtmlId      = SysHead.addElement("s:OrigtmlId"     );  // 服务原发起方终端编号
			Element OrigcnsmrsveId = SysHead.addElement("s:OrigcnsmrsveId");  //  服务原发起方服务器标识
			Element UsrLng         = SysHead.addElement("s:UsrLng"        );  // 用户语言
			Element Fileflg        = SysHead.addElement("s:Fileflg"       );  // 文件标志
			Element TxnSt          = SysHead.addElement("s:TxnSt"         );  // 交易返回状态
			Element RetCd          = SysHead.addElement("s:RetCd"         );  // 交易返回代码
			Element RetMsg         = SysHead.addElement("s:RetMsg"        );  // 交易返回信息        
			
			Map<String, Object> mapReqMsg = txnMessager.getMapReqMsg();
			
			// 服务代码  
			setContent(SvcId, mapReqMsg, "ReqSysHead.SvcId");
			// 服务场景  
			setContent(SvcScn, mapReqMsg, "ReqSysHead.SvcScn");
			// 服务提供方交易代码        
			setContent(SvcSpltTxnCd, mapReqMsg, "ReqSysHead.SvcSpltTxnCd");
			// 调用交易方交易代码        
			setContent(SvcCstTxnCd, mapReqMsg, "ReqSysHead.SvcCstTxnCd");
			// MAC信息                   
			setContent(MAC, mapReqMsg, "ReqSysHead.MAC");
			// 服务调用方系统代号        
			setContent(CnsmrSysId, mapReqMsg, "ReqSysHead.SvcSpltTxnCd");
			// 服务器版本号                                
			setContent(Vrsn, "");
			// 服务调用方系统流水号                        
			setContent(CnsmrSeqNo, "");
			// 服务提供方系统编号                          
			setContent(SvcSplrSysId, "");
			// 服务提供方流水号                            
			setContent(SvcSplrSeqNo, txnMessager.getSerialNo());
			// 交易日期                               
			setContent(TxnDt, mapReqMsg, "ReqSysHead.TxnDt");
			// 交易时间                               
			setContent(TxnTm, mapReqMsg, "ReqSysHead.TxnTm");
			// 会计日期                               
			setContent(AcgDt, mapReqMsg, "ReqSysHead.AcgDt");
			// 服务提供方服务器标识                        
			setContent(SvcSplrSvrId, "");
			// 渠道类型                                    
			setContent(TxnChnlTp, "");
			// 渠道编号                                    
			setContent(ChnlNo, "");
			//终端号                                      
			setContent(TxnTmlId, "");
			// 服务原发起方系统编号        
			setContent( OrigCnsmrId, "");
			// 全局业务流水号        
			setContent(OrigCnsmrSeqNo, mapReqMsg, "ReqSysHead.OrigCnsmrSeqNo");
			// 服务原发起方终端编号  
			setContent(OrigtmlId, mapReqMsg, "ReqSysHead.OrigtmlId");
			// 服务原发起方服务器标识   
			setContent(OrigcnsmrsveId, mapReqMsg, "ReqSysHead.CnsmrsvrId");
			// 用户语言   
			setContent(UsrLng, mapReqMsg, "ReqSysHead.UsrLng");
			// 文件标志  
			setContent(Fileflg, "");
			// 交易返回状态                  // 此处为交易成功
			setContent(TxnSt, Constant.EsbSuccessStatus);
			// 交易返回代码   
			setContent(RetCd, txnMessager.getReturnCode());
			setContent(RetMsg, txnMessager.getMsg()); // 交易返回信息   
			return SysHead;
		}
		
		/** 组 AppHead */
		private  Element packAppHead(Element AppHead, TxnMessager txnMessager) throws TxnException {
			
			Element TxnTlrId    = AppHead.addElement("s:TxnTlrId");   // 柜员号 
			Element OrgId       = AppHead.addElement("s:OrgId");      // 机构代码
			Element TlrPwsd     = AppHead.addElement("s:TlrPwsd");    // 柜员密码
			Element TlrLvl      = AppHead.addElement("s:TlrLvl");     // 柜员级别
			Element TlrTp       = AppHead.addElement("s:TlrTp");      // 柜员类别
			Element aprvFlg     = AppHead.addElement("s:aprvFlg");    // 复核标志
			
			Element AprvTlrInf  = AppHead.addElement("s:AprvTlrInf");   // 录入柜员数组
			Element AprvTlrId   = AprvTlrInf.addElement("s:AprvTlrId");  // 录入柜员标识
			Element AprvOrgId   = AprvTlrInf.addElement("s:AprvOrgId");  // 交易录入机构代码 
			Element AprvTlrLvl  = AprvTlrInf.addElement("s:AprvTlrLvl"); // 交易录入柜员级别
			Element AprvTlrTp   = AprvTlrInf.addElement("s:AprvTlrTp");  // 交易录入柜员类别
			Element Ahrflg      = AprvTlrInf.addElement("s:Ahrflg");     // 授权标志
			
			Element AhrTlrInf   = AppHead.addElement("s:AhrTlrInf");    // 授权柜员信息数组
			Element AhrTlrId    = AhrTlrInf.addElement("s:AhrTlrId");   // 授权柜员标识
			Element AhrOrgId    = AhrTlrInf.addElement("s:AhrOrgId");   // 授权机构代码 
			Element AhrtlrPswd  = AhrTlrInf.addElement("s:AhrtlrPswd"); // 授权柜员密码 
			Element AhrTlrLvl   = AhrTlrInf.addElement("s:AhrTlrLvl");  // 授权柜员级别
			Element AhrTlrTp    = AhrTlrInf.addElement("s:AhrTlrTp");   // 授权柜员类别
			
			Element Scndflg     = AppHead.addElement("s:Scndflg");    // 二次提交标志 
			
			Map<String, Object> mapReqMsg = txnMessager.getMapReqMsg();
			setContent(TxnTlrId,   mapReqMsg, "ReqAppHead.TxnTlrId");       // 柜员号          
			setContent(OrgId   ,   mapReqMsg, "ReqAppHead.OrgId");          // 机构代码         
			setContent(TlrPwsd ,   mapReqMsg, "ReqAppHead.TlrPwsd");        // 柜员密码         
			setContent(TlrLvl  ,   mapReqMsg, "ReqAppHead.TlrLvl");        // 柜员级别         
			setContent(TlrTp   ,   mapReqMsg, "ReqAppHead.TlrTp");         // 柜员类别         
			setContent(aprvFlg ,   mapReqMsg, "ReqAppHead.aprvFlg");        // 复核标志         
			setContent(AprvTlrId , mapReqMsg, "ReqAppHead.AprvTlrId");      // 录入柜员标识         
			setContent(AprvOrgId , mapReqMsg, "ReqAppHead.AprvOrgId");      // 交易录入机构代码       
			setContent(AprvTlrLvl, mapReqMsg, "ReqAppHead.AprvTlrLvl");     // 交易录入柜员级别       
			setContent(AprvTlrTp , mapReqMsg, "ReqAppHead.AprvTlrTp");      // 交易录入柜员类别       
			setContent(Ahrflg    , mapReqMsg, "ReqAppHead.Ahrflg");         // 授权标志           
			setContent(AhrTlrId  , mapReqMsg, "ReqAppHead.AhrTlrId");       // 授权柜员标识    
			setContent(AhrOrgId  , mapReqMsg, "ReqAppHead.AhrOrgId");        // 授权机构代码    
			setContent(AhrtlrPswd, mapReqMsg, "ReqAppHead.AhrtlrPswd");      // 授权柜员密码    
			setContent(AhrTlrLvl , mapReqMsg, "ReqAppHead.AhrTlrLvl");       // 授权柜员级别    
			setContent(AhrTlrTp  , mapReqMsg, "ReqAppHead.AhrTlrTp");        // 授权柜员类别    
			setContent(Scndflg   , mapReqMsg, "ReqAppHead.Scndflg");         // 二次提交标志    
			
			return AppHead;
		}
		
	/** 组RspBody */
	private Element packRspBody(Element RspBody, TxnMessager txnMessager) throws TxnException {
			
		ArrayList<DbDataLine> resultList = txnMessager.getResultList();
		String SerialNo = txnMessager.getSerialNo(); // 获取 流水号
		String txnId = txnMessager.getTxnId(); // 获取交易代号

		// 2. 读取配置文件, 获取出参
		Properties txnProperties = null;
		try {
			txnProperties = TxnConfigManager.getTxnConfig(txnId);
		} catch (Exception e) {
			logger.error(txnId + "流水号" + SerialNo + "获取交易配置出错", e);
			throw new TxnException(txnId + "流水号" + SerialNo + "获取交易配置出错");
		}
		logger.debug(txnId + "流水号" + SerialNo + "获取交易配置完成");

		String[] outColumes = null;
		// 取 Head resultHead
		String strHead = null;
		try {
			strHead = txnProperties.getProperty(Constant.outParm + ".Head");
		} catch (Exception e) {
			throw new TxnException(e);
		}
		logger.debug(txnId + "流水号" + SerialNo + "获取 Head 配置完成");
					
		if (strHead == null || "".equals(strHead)) {
			logger.warn(Constant.outParm + ".Head 为空或未配置 ");
			logger.debug(SerialNo + "当前RspBody为:" + RspBody.asXML());
		} else {
			outColumes = strHead.split(",");
			Map<String, Object> resultHead = txnMessager.getResultHead();
			for(String columnName : outColumes){
				try{
					Element column = DocumentHelper.createElement(columnName);
					Object value = resultHead.get(columnName);
					column.setText(value == null ? "" : value.toString());
					RspBody.add(column);
				} catch (Exception e) {
					logger.error("流水号" + SerialNo + "组返回数据包出现异常,异常位置:数据表头" + columnName + "字段:", e);
					throw e;
				}
			}
			logger.debug(SerialNo + "resultHead组包完毕,当前RspBody为:" + RspBody.asXML());
			// 获取输出参数
		}
		
		// 取 ARRAR , Array数据是重复多次的数据
		String strArray = null;
		try {
			strArray = txnProperties.getProperty(Constant.outParm + ".Array");
		} catch (Exception e) {
			throw new TxnException(e);
		}
					
		if (strArray == null || "".equals(strArray)) {
			logger.warn(Constant.outParm + ".Array 为空或未配置 ");
		} else {
			outColumes = strArray.split(",");
			int i = 0; //计数器, 记录 本次返回数据条数
			for (DbDataLine onelint : resultList) {
				i ++ ; 
				Element Array = DocumentHelper.createElement("TransResult");
				// 获取输出参数
				Array.addAttribute("key", onelint.getKeyValue().toString()); // 属性中添加
																				// key
				String value = "";
				for (String columnName : outColumes) {
					Object dataObj = onelint.get(columnName);
					if(dataObj != null){
						value = dataObj.toString();
					} else {
						value = "";
					}
					Element column = DocumentHelper.createElement(columnName);
					column.setText( value.toString() );
					Array.add(column);
					logger.debug("流水号" + SerialNo + ":" + columnName + "增加到 DataLine 完成" + column.asXML());
				}
				logger.debug("流水号" + SerialNo + "RspBody中Array[" + i + "]组包完毕,当前Array为:" + RspBody.asXML());
				RspBody.add(Array);
			}
		}
		logger.debug("流水号" + SerialNo + " RspBody组包完毕,当前RspBody为:" + RspBody.asXML());
		return RspBody;
	}
		
	/**
	 * 取  mapReqMsg 中 key 为 Content值付给conEl元素
	 * 
	 */
	private void setContent(Element conEl, Map<String, Object> mapReqMsg, String key) {
		Object content = mapReqMsg.get(key);
		if(content == null ){
//			conEl.setText("");
		} else if(content != null && !"".equals(content.toString()) ) {
				conEl.setText(content.toString());  
		}		
		logger.debug(conEl.getName() + ":" + conEl.asXML());
	}
	private void setContent(Element conEl, Object content ) {
		if(content != null && !"".equals(content.toString())) {
			conEl.setText(content.toString());                          
		}else {
			conEl.setText("");
		}
		logger.debug(conEl.getName() + ":" + conEl.asXML());
	}
	
	
}
