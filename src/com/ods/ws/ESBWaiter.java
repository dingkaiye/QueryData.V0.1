package com.ods.ws;

import java.io.StringWriter;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.cxf.aegis.type.java5.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
import org.apache.cxf.aegis.type.java5.XmlElement;
import org.apache.cxf.aegis.type.java5.XmlParamType;

import javax.xml.ws.Holder;
import javax.xml.ws.ResponseWrapper;
import org.apache.log4j.Logger;

import com.ods.common.Constant;
import com.ods.common.NameSpace;
import com.ods.common.SerialNo;
import com.ods.exception.TxnException;
import com.ods.log.OdsLog;
import com.ods.manager.Handler;
import com.ods.manager.QueueManager;
import com.ods.manager.TxnCntContrl;
import com.ods.manager.TxnConfigManager;
import com.ods.message.AppHeadIn;
import com.ods.message.AppHeadOut;
import com.ods.message.EsbMessageIn;
import com.ods.message.EsbMessageOut;
import com.ods.message.LocalHead;
import com.ods.message.SysHeadIn;
import com.ods.message.SysHeadOut;
import com.ods.message.TxnMessager;
import com.ods.service.PackEsbHead;
import com.ods.transaction.DepositTrans.Body.ReqBody;
import com.ods.transaction.DepositTrans.Body.RspBody;

@WebService(targetNamespace=NameSpace.ODS_WSDL )
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL, parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
public class ESBWaiter extends Thread {

	private static Logger logger = OdsLog.getTxnLogger("ESBWaiter");
	
	@WebResult(name = "ReqS30013001504",  targetNamespace = NameSpace.ODS_WSDL )
	@WebMethod(operationName="ReqS30013001504")
	@ResponseWrapper(localName="RspS30013001504", targetNamespace = NameSpace.ODS_WSDL )
	public void ReqS30013001504( 
			@WebParam(name= "ReqSysHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) SysHeadIn sysHeadIn,
			@WebParam(name= "ReqAppHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) AppHeadIn appHeadIn,
			@WebParam(name= "ReqLocalHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) LocalHead localHead,
			@WebParam(name= "Body", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) ReqBody reqBody ,
			@WebParam(name= "RspSysHead", mode=Mode.OUT,  targetNamespace=NameSpace.ODS_URL) Holder<SysHeadOut> sysHeadOut,
			@WebParam(name= "RspAppHead", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<AppHeadOut> appHeadOut,
			@WebParam(name= "RspLocalHead", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<LocalHead> localHeadOut  ,
			@WebParam(name= "Body", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<RspBody> rspBody

		)  {
		 
		EsbMessageOut esbMessage =  Handler.QueryOdsData( sysHeadIn, appHeadIn, localHead, reqBody );
		sysHeadOut.value   = esbMessage.getSysHead();
		appHeadOut.value   = esbMessage.getAppHead();
		localHeadOut.value = esbMessage.getLocalHead();
		rspBody.value      = (RspBody) esbMessage.getBody();
		
	}
	
//	@WebResult(name = "ReqS30013001505",  targetNamespace = NameSpace.ODS_WSDL )
//	@WebMethod(operationName="ReqS30013001505")
//	@ResponseWrapper(localName="RspS30013001505", targetNamespace = NameSpace.ODS_WSDL )
//	public void ReqS30013001505( 
//			@WebParam(name= "ReqSysHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) SysHeadIn sysHeadIn,
//			@WebParam(name= "ReqAppHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) AppHeadIn appHeadIn,
//			@WebParam(name= "ReqLocalHead", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) LocalHead localHead,
//			@WebParam(name= "Body", mode=Mode.IN, targetNamespace=NameSpace.ODS_URL) ReqBody reqBody ,
//			@WebParam(name= "RspSysHead", mode=Mode.OUT,  targetNamespace=NameSpace.ODS_URL) Holder<SysHeadOut> sysHeadOut,
//			@WebParam(name= "RspAppHead", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<AppHeadOut> appHeadOut,
//			@WebParam(name= "RspLocalHead", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<LocalHead> localHeadOut  ,
//			@WebParam(name= "Body", mode=Mode.OUT, targetNamespace=NameSpace.ODS_URL) Holder<RspBody> rspBody
//
//		)  {
//		 
//		EsbMessageOut esbMessage =  Handler.QueryOdsData( sysHeadIn, appHeadIn, localHead, reqBody );
//		sysHeadOut.value   = esbMessage.getSysHead();
//		appHeadOut.value   = esbMessage.getAppHead();
//		localHeadOut.value = esbMessage.getLocalHead();
//		rspBody.value      = (RspBody) esbMessage.getBody();
//		
//	}
	
}
