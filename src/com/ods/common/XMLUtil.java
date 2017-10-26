package com.ods.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import com.ods.log.OdsLog;

/**
 * Soap 处理类 
 * @author ding_kaiye
 */

public class XMLUtil {
	private static Logger logger = OdsLog.getTxnLogger("XMLUtil");
	
/**
 * 获取root节点
 * @param xmlstring
 * @return
 */
	public Element getRootElement(String xmlstring) {
		Document document = null;
		Element root = null;
		try {
			document = DocumentHelper.parseText(xmlstring);
			root = document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//List<Element> elements = root.elements();
		return root ;
	}
	
	public Node getRootElement(Document document , String path) {
		return document.selectSingleNode(path); 
	}
	
	/**
	 * 返回xml字符串中的根节点
	 * @param xml
	 * @return
	 */
	public List<Element> getElements(String xml) {
		Element root = getRootElement(xml);
		List<Element> elements = root.elements();
		return elements ;
	}
	
	/**
	 * 遍历Element中的节点列表
	 * @param element
	 * @return
	 */
	public List<Element> getElements(Element element) {
		List<Element> elements = element.elements();
		return elements ;
	}
    
	/**
	 * 从指定节点开始,递归遍历所有子节点
	 * http://blog.csdn.net/chenleixing/article/details/44353491
	 */
	public void getNodes(Element node) {
		
		// 当前节点的名称、文本内容和属性
		logger.info("当前节点名称：" + node.getName());// 当前节点名称
		logger.info("当前节点的内容：" + node.getTextTrim()); // 当前节点的内容
		logger.info("当前节点的内容：" + node.getData()); // 当前节点的内容
		logger.info("当前节点的内容：" + node.getStringValue() ); // 当前节点Value
		
		List<Attribute> listAttr = node.attributes(); // 当前节点的所有属性的list
		
		for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			String value = attr.getValue();// 属性的值
			
			logger.info("属性名称：" + name + "属性值：" + value);
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodes(e);// 递归
		}
	}

	
/*	public Element getRoot(String fileName) throws Exception {
		SAXReader sax = new SAXReader();      // 创建一个SAXReader对象
		File xmlFile = new File(fileName);    // 根据指定的路径创建file对象
		Document document = sax.read(xmlFile);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		Element root = document.getRootElement();// 获取根节点
		return root;
		//this.getNodes(root);// 从根节点开始遍历所有节点
	}*/

	
	

	
/*	*//**
	 * 把soap字符串格式化为SOAPMessage
	 * @param soapString
	 * @return
	 *//*
	
	private static SOAPMessage formatSoapString(String soapString) {
		MessageFactory msgFactory;
		try {
			msgFactory = MessageFactory.newInstance();
			MimeHeaders headers = new MimeHeaders(); 
			InputStream in = new ByteArrayInputStream(soapString.getBytes("UTF-8"));
			SOAPMessage reqMsg = msgFactory.createMessage( headers, in);
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	*/
	
/*	*//**
	 * 解析soapXML
	 * 
	 * @param soapXML
	 * @return
	 *//*
	public static void parseSoapMessage(String soapXML) {
		try {
			SOAPMessage msg = formatSoapString(soapXML);
			SOAPBody body = msg.getSOAPBody();
			SOAPHeader header = msg.getSOAPHeader();
			// msg.getSOAPPart()
			Iterator<SOAPElement> iterator = body.getChildElements();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return resultBean;
	}
*/
	
	
	

}
