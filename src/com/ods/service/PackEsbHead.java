package com.ods.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ods.common.Config;
import com.ods.common.Constant;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.message.AppHeadIn;
import com.ods.message.AppHeadOut;
import com.ods.message.EsbMessageOut;
import com.ods.message.LocalHead;
import com.ods.message.SysHeadIn;
import com.ods.message.SysHeadOut;

/**
 * @author ding_kaiye
 *
 */
public class PackEsbHead {

//	private static Properties properties = null;
	
	private static Logger logger = OdsLog.getTxnLogger("PackEsbHead");
//	private final static String confile = Constant.ESBPACK_CONFIG_FILE ;  //请求报文结构配置文件
//	private final static String reqSysHead = "ReqSysHead"; // SysHead在配置文件中的位置
//	private static Properties sysConfig = null;
//	private static String wsUrl = "";
	
	/**
	 * 
	 * @param inMessage
	 * @param serialNo
	 * @param txnSt
	 * @param retCd
	 * @param retMsg
	 * @return
	 * 2017-11-29
	 */
	
	public static EsbMessageOut packEsbFailMsg(SysHeadIn SysHead, String serialNo ,
			String txnSt, String retCd, String retMsg )  {
		SysHeadOut sysHeadOut = packEsbSysHead(SysHead, serialNo, txnSt, retCd, retMsg );
		EsbMessageOut rspMessage = new EsbMessageOut();
		rspMessage.setSysHead(sysHeadOut); 
		return rspMessage;
	}
	
	public static SysHeadOut packEsbSysHead(SysHeadIn sysHeadIn, String serialNo ,
			String txnSt, String retCd, String retMsg )  {

		SysHeadOut SysHead = new SysHeadOut();
		
		SysHead.setSvcId(sysHeadIn.getSvcId()); 
		SysHead.setSvcScn(sysHeadIn.getSvcScn());
		SysHead.setSvcSplrTxnCd(sysHeadIn.getSvcSplrTxnCd());
		SysHead.setSvcCstTxnCd(sysHeadIn.getSvcCstTxnCd());
		SysHead.setMAC(sysHeadIn.getMAC());
		SysHead.setCnsmrSysId(sysHeadIn.getCnsmrSysId());
		SysHead.setVrsn(Constant.Vrsn);
		SysHead.setCnsmrSeqNo(sysHeadIn.getCnsmrSeqNo());
		SysHead.setSvcSplrSysId(Constant.SysId);  //服务提供方系统编号
		SysHead.setSvcSplrSeqNo(serialNo);        //服务提供方流水号
		SysHead.setTxnDt(sysHeadIn.getTxnDt());
		SysHead.setTxnTm(sysHeadIn.getTxnTm());
		SysHead.setAcgDt(sysHeadIn.getAcgDt());
//		SysHead.setSvcSplrSvrId(sysHeadIn.getSvcSplrSvrId());
		SysHead.setTxnChnlTp(sysHeadIn.getTxnChnlTp());
		SysHead.setChnlNo(sysHeadIn.getChnlNo());
		SysHead.setTxnTmlId(sysHeadIn.getTxnTmlId());
		SysHead.setCnsmrSvrId(sysHeadIn.getCnsmrSvrId());
		SysHead.setOrigCnsmrId(sysHeadIn.getOrigCnsmrId());
		SysHead.setOrigCnsmrSeqNo(sysHeadIn.getOrigCnsmrSeqNo());
		SysHead.setOrigTmlId(sysHeadIn.getOrigTmlId());
		SysHead.setOrigCnsmrSvrId(sysHeadIn.getOrigCnsmrSvrId());
		SysHead.setUsrLng(sysHeadIn.getUsrLng());
		SysHead.setFileFlg(sysHeadIn.getFileFlg());
		SysHead.setTxnSt(txnSt);
		SysHead.setRetCd(retCd);
		SysHead.setRetMsg(retMsg);

		StringWriter xmlStringWriter = null;
		JAXBContext jc = null ;
		Marshaller marshaller = null;
		
		xmlStringWriter = new StringWriter();
		boolean flg = true;
		try {
			jc = JAXBContext.newInstance(SysHeadOut.class);
			marshaller = jc.createMarshaller();
			marshaller.marshal(SysHead, xmlStringWriter);
		} catch (JAXBException e) {
			logger.warn("转换 java 到 xml出现异常 ", e);
			flg = false;
		}
		if (true == flg) {
			String xml = xmlStringWriter.toString();
			logger.info(serialNo + ":返回报文为" + xml);
			xmlStringWriter = null;
		}
		
		return SysHead;
	}
	
	/**
	 * 组返回报文中的 AppHead
	 * @param appHeadIn
	 * @return
	 * 2017-11-29
	 */
	
	public static AppHeadOut packEsbAppHead(AppHeadIn appHeadIn)  {
		
		AppHeadOut appHeadOut = new AppHeadOut();
		appHeadOut.TxnTlrId    = appHeadIn.TxnTlrId   ;
		appHeadOut.OrgId       = appHeadIn.OrgId      ;
		appHeadOut.TlrPwsd     = appHeadIn.TlrPwsd    ;
		appHeadOut.AprvFlg     = appHeadIn.AprvFlg    ;
		appHeadOut.AprvTlrInf  = appHeadIn.AprvTlrInf ;
		appHeadOut.AhrTlrInf   = appHeadIn.AhrTlrInf  ;
		appHeadOut.ScndFlg     = appHeadIn.ScndFlg    ;
		
		return appHeadOut;
	}
	
	/**
	 * 组返回报文的 LocalHead
	 * @param localHead
	 * @return
	 * 2017-11-29
	 */
	public static LocalHead packEsbLocalHead(LocalHead localHead)  {
		return localHead;
	}
	
	
//	
//	
//	public static String packEsbFailMsg(String inMessage, String serialNo ,
//			String txnSt, String retCd, String retMsg )  {
//		if (sysConfig == null) {
//			try {
//				sysConfig = Config.getPropertiesConfig(Constant.SysConfig);
//			} catch (IOException e) {
//				logger.error("启动失败, 读取系统配置文件" + Constant.SysConfig + "失败");
//			}
//
//			wsUrl = sysConfig.getProperty("WebServiceUrl");
//			if ("".equals(wsUrl) || wsUrl == null) {
//				logger.error("WebServiceUrl 配置为空或未配置, 系统初始化失败");
//			}
//		}
//		
//		Document document = DocumentHelper.createDocument();
//		document.setXMLEncoding("UTF-8");
//		
//		Element root = document.addElement("soap:Envelope");
//		root.add(DocumentHelper.createNamespace("soap", "http://schemas.xmlsoap.org/wsdl/soap/") );
//		root.add(DocumentHelper.createNamespace("s", wsUrl) ); 
//		root.add(DocumentHelper.createNamespace("tns", wsUrl + "/wsdl") ); 
//		
//		Element soapHead = DocumentHelper.createElement("soap:Header");
//		Element soapBody = DocumentHelper.createElement("soap:Body");
//
//		Element S03001000101 = DocumentHelper.createElement("tns:RspS300130015");
//		
//		Element SysHead = DocumentHelper.createElement("s:ReqSysHead");
//		
//		root.add(soapHead);
//		root.add(soapBody);
//		soapBody.add(S03001000101);
//		S03001000101.add(SysHead);
//		
//		//快速解析 SysHead
//		HashMap<String, String> mapReqMsg = parseReqSysHead( inMessage) ;
//		
//        /** 组 Syshead , 对于 ESB 系统, 业务失败和错误相应, 仅需 SysHead */
//		
//		packSyshead(SysHead, mapReqMsg, serialNo , txnSt, retCd, retMsg);
//		
//		String xmlTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//		
//		logger.info(serialNo + "返回报文为: " + xmlTitle + root.asXML() + "\n");
//		return xmlTitle + root.asXML();
//	}
//	
//	
//
//	
//	/** 组 Syshead */
//	
//	/**
//	 * @param SysHead  待组装 SysHead
//	 * @param mapReqMsg 解析后的报文Map
//	 * @param serialNo 交易流水号
//	 * @param txnSt  交易返回状态
//	 * @param retCd  交易返回状态代码
//	 * @param retMsg 交易返回信息
//	 * @return
//	 * @throws TxnException
//	 */
//	private static  Element packSyshead(Element SysHead, 
//			Map<String, String> mapReqMsg, String serialNo,
//			String txnSt, String retCd, String retMsg ) {
//		
//		Element SvcId          = SysHead.addElement("s:SvcId"         );  // 服务代码
//		Element SvcScn         = SysHead.addElement("s:SvcScn"        );  // 服务场景
//		Element SvcSplrTxnCd   = SysHead.addElement("s:SvcSplrTxnCd"  );  // 服务提供方交易代码
//		Element SvcCstTxnCd    = SysHead.addElement("s:SvcCstTxnCd"   );  // 调用交易方交易代码
//		Element MAC            = SysHead.addElement("s:MAC"           );  // MAC信息
//		Element CnsmrSysId     = SysHead.addElement("s:CnsmrSysId"    );  // 服务调用方系统代号
//		Element Vrsn           = SysHead.addElement("s:Vrsn"          );  // 服务版本号
//		Element CnsmrSeqNo     = SysHead.addElement("s:CnsmrSeqNo"    );  // 服务调用方系统流水号
//		Element SvcSplrSysId   = SysHead.addElement("s:SvcSplrSysId"  );  // 服务提供方系统编号  SvcSplrSvrId
//		Element SvcSplrSeqNo   = SysHead.addElement("s:SvcSplrSeqNo"  );  // 服务提供方流水号
//		Element TxnDt          = SysHead.addElement("s:TxnDt"         );  // 交易日期
//		Element TxnTm          = SysHead.addElement("s:TxnTm"         );  // 交易时间
//		Element AcgDt          = SysHead.addElement("s:AcgDt"         );  // 会计日期
//		Element TxnChnlTp      = SysHead.addElement("s:TxnChnlTp"     );  // 渠道类型
//		Element ChnlNo         = SysHead.addElement("s:ChnlNo"        );  // 渠道编号
//		Element TxnTmlId       = SysHead.addElement("s:TxnTmlId"      );  // 终端号
//		Element CnsmrSvrId     = SysHead.addElement("s:CnsmrSvrId"    );  // 服务调用方服务器标识 //20171120  根据 esb 报文增加
//		Element OrigCnsmrSeqNo = SysHead.addElement("s:OrigCnsmrSeqNo");  // 全局业务流水号 
//		Element OrigCnsmrId    = SysHead.addElement("s:OrigCnsmrId"   );  // 服务原发起方系统编号
//		Element OrigTmlId      = SysHead.addElement("s:OrigTmlId"     );  // 服务原发起方终端编号
//		Element OrigCnsmrSvrId = SysHead.addElement("s:OrigCnsmrSvrId");  // 服务原发起方服务器标识
//		Element UsrLng         = SysHead.addElement("s:UsrLng"        );  // 用户语言
//		Element FileFlg        = SysHead.addElement("s:FileFlg"       );  // 文件标志
//		Element TxnSt          = SysHead.addElement("s:TxnSt"         );  // 交易返回状态
//		Element RetCd          = SysHead.addElement("s:RetCd"         );  // 交易返回代码
//		Element RetMsg         = SysHead.addElement("s:RetMsg"        );  // 交易返回信息        
//		
//		// 服务代码  
//		setContent(SvcId, mapReqMsg, "ReqSysHead.SvcId");
//		// 服务场景  
//		setContent(SvcScn, mapReqMsg, "ReqSysHead.SvcScn");
//		// 服务提供方交易代码        
//		setContent(SvcSplrTxnCd, mapReqMsg, "ReqSysHead.SvcSplrTxnCd");
//		// 调用交易方交易代码        
//		setContent(SvcCstTxnCd, mapReqMsg, "ReqSysHead.SvcCstTxnCd");
//		// MAC信息                   
//		setContent(MAC, mapReqMsg, "ReqSysHead.MAC");
//		// 服务调用方系统代号        
//		setContent(CnsmrSysId, mapReqMsg, "ReqSysHead.CnsmrSysId");
//		// 服务版本号                                
//		setContent(Vrsn, Constant.Vrsn);
//		// 服务调用方系统流水号                        
//		setContent(CnsmrSeqNo, mapReqMsg, "ReqSysHead.CnsmrSeqNo");
//		// 服务原发起方系统编号
//		setContent(OrigCnsmrId, mapReqMsg, "ReqSysHead.CnsmrSvrId");
//		// 服务提供方系统编号                            
//		setContent(SvcSplrSysId, Constant.SysId);
//		// 服务提供方流水号                            
//		setContent(SvcSplrSeqNo, serialNo);
//		// 交易日期                               
//		setContent(TxnDt, mapReqMsg, "ReqSysHead.TxnDt");
//		// 交易时间                               
//		setContent(TxnTm, mapReqMsg, "ReqSysHead.TxnTm");
//		// 会计日期                               
//		setContent(AcgDt, mapReqMsg, "ReqSysHead.AcgDt");
//
//		// 渠道类型                                    
//		setContent(TxnChnlTp, mapReqMsg, "ReqSysHead.TxnChnlTp");
//		// 渠道编号                                    
//		setContent(ChnlNo, mapReqMsg, "ReqSysHead.ChnlNo");
//		//终端号                                      
//		setContent(TxnTmlId, mapReqMsg, "ReqSysHead.TxnTmlId");
//		// 服务调用方服务器标识 //20171120  根据 esb 报文增加 CnsmrSvrId
//		setContent(CnsmrSvrId, mapReqMsg, "ReqSysHead.CnsmrSvrId");
//		// 服务原发起方系统编号        
//		setContent( OrigCnsmrId, "");
//		// 全局业务流水号        
//		setContent(OrigCnsmrSeqNo, mapReqMsg, "ReqSysHead.OrigCnsmrSeqNo");
//		// 服务原发起方终端编号  
//		setContent(OrigTmlId, mapReqMsg, "ReqSysHead.OrigTmlId");
//		// 服务原发起方服务器标识   
//		setContent(OrigCnsmrSvrId, mapReqMsg, "ReqSysHead.OrigCnsmrSvrId");
//		// 用户语言   
//		setContent(UsrLng, mapReqMsg, "ReqSysHead.UsrLng");
//		// 文件标志  
//		setContent(FileFlg, "0");
//		// 交易返回状态 
//		setContent(TxnSt, txnSt);
//		// 交易返回代码
//		setContent(RetCd, retCd);
//		// 交易返回信息
//		setContent(RetMsg, retMsg);
//		
//		return SysHead;
//	}
//	
//	// 解 SysHead 
//	private static HashMap<String, String> parseReqSysHead(String  inMessage) {
//		
//		String separator = ",";
//		HashMap<String, String> reqMsg = new HashMap<String, String>();
//		// 检查配置
//		if (properties == null) {
//			// 若配置不存在, 尝试重新加载配置
//			try {
//				logger.info(confile + "未加载,重新加载");
//				properties = Config.loadConfigPropertiesFile(confile);
//			} catch (IOException e) {
//				logger.error(confile + "配置文件载入失败");
//				e.printStackTrace();
//				return reqMsg;
//			}
//		}
//		//解析 xml 
//		Document document = null;
//		try {
//			document = DocumentHelper.parseText(inMessage);
//		} catch (DocumentException e) {
//			return reqMsg;
//		}
//		
//		String path = properties.getProperty(reqSysHead + "Path"); //取得本段报文路径
//		if(null == path) {
//			logger.warn(reqSysHead +" 未配置");
//			return null;
//		}
//		
//		String partName = reqSysHead ;
//		String[] columns = properties.getProperty(reqSysHead).split(separator); //取得字段列表
//		
//		for (int j=0; j<columns.length; j++) {
//			//根据路径取得元素
//			String columnName = partName + "." + columns[j];
//			String columnRelativePath = properties.getProperty(columnName).split("#")[0].split(",")[0].trim(); //取得相对路径
//			
//			List<Node> elements = document.selectNodes(path + columnRelativePath);  // 取得节点 
//			logger.info( columnName + ":[" + path + columnRelativePath + "]" );	
//			
//			if(elements.size() == 1) {                      // 仅有一个节点情况, 直接存储为字符串
//				Element element = (Element) elements.get(0);
//				logger.info( columnName + ":[" + element.getText() + "]");	
//				reqMsg.put(columnName, element.getText());
//			} else {                                        // 多个节点情况, 使用 List 结构存放 
//				StringBuffer strelements = new StringBuffer();
//				for(int k=0; k<elements.size(); k++) {
//					Element element = (Element) elements.get(k);
//					strelements.append(element.asXML());
//				}
//				reqMsg.put( columnName, strelements.toString() );
//			}
//		}
//		return reqMsg;
//		
//	}
//	
//	/**
//	 * 取  mapReqMsg 中 key 为 Content值付给conEl元素
//	 * 
//	 */
//	private static void setContent(Element conEl, Map<String, String> mapReqMsg, String key) {
//		Object content = mapReqMsg.get(key);
//		if(content == null ){
//		} else if(content != null && !"".equals(content.toString()) ) {
//				conEl.setText(content.toString());  
//		}		
//		logger.debug(conEl.getName() + ":" + conEl.asXML());
//	}
//	private static void setContent(Element conEl, Object content ) {
//		if(content != null && !"".equals(content.toString())) {
//			conEl.setText(content.toString());                          
//		}else {
//			conEl.setText("");
//		}
//		logger.debug(conEl.getName() + ":" + conEl.asXML());
//	}
//	
	
	
}
