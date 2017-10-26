package com.ods.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ods.exception.TxnException;
import com.ods.log.OdsLog;

/**
 * Esb报文的快速组包, 直接解析传入报文, 快速组应答包
 * 适用于需要快速返回交易失败的场景 
 * @author ding_kaiye
 *
 */
public class PackEsbFailMsg {

	private static Properties properties = null;
	
	private static Logger logger = OdsLog.getTxnLogger("UnPackService");
	private final static String confile = Constant.ESBPACK_CONFIG_FILE ;  //请求报文结构配置文件
	private final static String reqSysHead = "ReqSysHead"; // SysHead在配置文件中的位置
	
	/**
	 * @param inMessage 请求报文原文 
	 * @param txnSt  交易返回状态
	 * @param retCd  交易返回代码
	 * @param retMsg 交易返回信息
	 * @return
	 * @throws TxnException
	 */
	public static String packEsbFailMsg(String inMessage, String serialNo ,
			String txnSt, String retCd, String retMsg )  {
		
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		
		Element root = document.addElement("soapenv:Envelope");
		root.add(DocumentHelper.createNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/") );
		root.add(DocumentHelper.createNamespace("s", "http://esb.dcitsbiz.com/services/S030010001") ); 
		
		Element soapHead = DocumentHelper.createElement("soapenv:Header");
		Element soapBody = DocumentHelper.createElement("soapenv:Body");

		Element S03001000101 = DocumentHelper.createElement("s:RspS03001000101");
		
		Element SysHead = DocumentHelper.createElement("s:ReqSysHead");
		
		root.add(soapHead);
		root.add(soapBody);
		soapBody.add(S03001000101);
		S03001000101.add(SysHead);
		
		//快速解析 SysHead
		HashMap<String, String> mapReqMsg = parseReqSysHead( inMessage) ;
		
        /** 组 Syshead , 对于 ESB 系统, 业务失败和错误相应, 仅需 SysHead */
		
		packSyshead(SysHead, mapReqMsg, serialNo , txnSt, retCd, retMsg);
		
		String xmlTitle = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		
		logger.info(serialNo + "返回报文为: " + xmlTitle + root.asXML() + "\n");
		return xmlTitle + root.asXML();
	}
	
	
	/** 组 Syshead */
	
	/**
	 * @param SysHead  待组装 SysHead
	 * @param mapReqMsg 解析后的报文Map
	 * @param serialNo 交易流水号
	 * @param txnSt  交易返回状态
	 * @param retCd  交易返回状态代码
	 * @param retMsg 交易返回信息
	 * @return
	 * @throws TxnException
	 */
	private static  Element packSyshead(Element SysHead, 
			Map<String, String> mapReqMsg, String serialNo,
			String txnSt, String retCd, String retMsg ) {
		
		Element SvcId          = SysHead.addElement("s:SvcId"         );  // 服务代码
		Element SvcScn         = SysHead.addElement("s:SvcScn"        );  // 服务场景
		Element SvcSpltTxnCd   = SysHead.addElement("s:SvcSpltTxnCd"  );  // 服务提供方交易代码
		Element SvcCstTxnCd    = SysHead.addElement("s:SvcCstTxnCd"   );  // 调用交易方交易代码
		Element MAC            = SysHead.addElement("s:MAC"           );  // MAC信息
		Element CnsmrSysId     = SysHead.addElement("s:CnsmrSysId"    );  // 服务调用方系统代号
		Element Vrsn           = SysHead.addElement("s:Vrsn"          );  // 服务器版本号
		Element CnsmrSeqNo     = SysHead.addElement("s:CnsmrSeqNo"    );  // 服务调用方系统流水号
		Element SvcSplrSysId   = SysHead.addElement("s:SvcSplrSysId"  );  // 服务提供方系统编号
		Element SvcSplrSeqNo   = SysHead.addElement("s:SvcSplrSeqNo"  );  // 服务提供方流水号
		Element TxnDt          = SysHead.addElement("s:TxnDt"         );  // 交易日期
		Element TxnTm          = SysHead.addElement("s:TxnTm"         );  // 交易时间
		Element AcgDt          = SysHead.addElement("s:AcgDt"         );  // 会计日期
		Element SvcSplrSvrId   = SysHead.addElement("s:SvcSplrSvrId"  );  // 服务提供方服务器标识
		Element TxnChnlTp      = SysHead.addElement("s:TxnChnlTp"     );  // 渠道类型
		Element ChnlNo         = SysHead.addElement("s:ChnlNo"        );  // 渠道编号
		Element TxnTmlId       = SysHead.addElement("s:TxnTmlId"      );  // 终端号
		Element OrigCnsmrId    = SysHead.addElement("s:OrigCnsmrId"   );  // 服务原发起方系统编号
		Element OrigCnsmrSeqNo = SysHead.addElement("s:OrigCnsmrSeqNo");  // 全局业务流水号 
		Element OrigtmlId      = SysHead.addElement("s:OrigtmlId"     );  // 服务原发起方终端编号
		Element OrigcnsmrsveId = SysHead.addElement("s:OrigcnsmrsveId");  // 服务原发起方服务器标识
		Element UsrLng         = SysHead.addElement("s:UsrLng"        );  // 用户语言
		Element Fileflg        = SysHead.addElement("s:Fileflg"       );  // 文件标志
		Element TxnSt          = SysHead.addElement("s:TxnSt"         );  // 交易返回状态
		Element RetCd          = SysHead.addElement("s:RetCd"         );  // 交易返回代码
		Element RetMsg         = SysHead.addElement("s:RetMsg"        );  // 交易返回信息        
		
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
		setContent(SvcSplrSeqNo, serialNo);
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
		// 终端号
		setContent(TxnTmlId, "");
		// 服务原发起方系统编号
		setContent(OrigCnsmrId, "");
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
		// 交易返回状态 
		setContent(TxnSt, txnSt);
		// 交易返回代码
		setContent(RetCd, retCd);
		// 交易返回信息
		setContent(RetMsg, retMsg);
		
		return SysHead;
	}
	
	// 解 SysHead 
	private static HashMap<String, String> parseReqSysHead(String  inMessage) {
		
		String separator = ",";
		HashMap<String, String> reqMsg = new HashMap<String, String>();
		// 检查配置
		if (properties == null) {
			// 若配置不存在, 尝试重新加载配置
			try {
				logger.info(confile + "未加载,重新加载");
				properties = Config.loadConfigPropertiesFile(confile);
			} catch (IOException e) {
				logger.error(confile + "配置文件载入失败");
				e.printStackTrace();
				return reqMsg;
			}
		}
		//解析 xml 
		Document document = null;
		try {
			document = DocumentHelper.parseText(inMessage);
		} catch (DocumentException e) {
			return reqMsg;
		}
		
		String path = properties.getProperty(reqSysHead + "Path"); //取得本段报文路径
		if(null == path) {
			logger.warn(reqSysHead +" 未配置");
			return null;
		}
		
		String partName = reqSysHead ;
		String[] columns = properties.getProperty(reqSysHead).split(separator); //取得字段列表
		
		for (int j=0; j<columns.length; j++) {
			//根据路径取得元素
			String columnName = partName + "." + columns[j];
			String columnRelativePath = properties.getProperty(columnName).split("#")[0].split(",")[0].trim(); //取得相对路径
			
			List<Node> elements = document.selectNodes(path + columnRelativePath);  // 取得节点 
			logger.info( columnName + ":[" + path + columnRelativePath + "]" );	
			
			if(elements.size() == 1) {                      // 仅有一个节点情况, 直接存储为字符串
				Element element = (Element) elements.get(0);
				logger.info( columnName + ":[" + element.getText() + "]");	
				reqMsg.put(columnName, element.getText());
			} else {                                        // 多个节点情况, 使用 List 结构存放 
				StringBuffer strelements = new StringBuffer();
				for(int k=0; k<elements.size(); k++) {
					Element element = (Element) elements.get(k);
					strelements.append(element.asXML());
				}
				reqMsg.put( columnName, strelements.toString() );
			}
		}
		return reqMsg;
		
	}
	
	/**
	 * 取  mapReqMsg 中 key 为 Content值付给conEl元素
	 * 
	 */
	private static void setContent(Element conEl, Map<String, String> mapReqMsg, String key) {
		Object content = mapReqMsg.get(key);
		if(content == null ){
		} else if(content != null && !"".equals(content.toString()) ) {
				conEl.setText(content.toString());  
		}		
		logger.debug(conEl.getName() + ":" + conEl.asXML());
	}
	private static void setContent(Element conEl, Object content ) {
		if(content != null && !"".equals(content.toString())) {
			conEl.setText(content.toString());                          
		}else {
			conEl.setText("");
		}
		logger.debug(conEl.getName() + ":" + conEl.asXML());
	}
	
	
	
}
