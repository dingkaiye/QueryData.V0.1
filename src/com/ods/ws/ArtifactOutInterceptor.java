package com.ods.ws;


import java.io.ByteArrayInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.OutputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.Phase; 
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ods.common.NameSpace;
import com.ods.log.OdsLog;

import org.apache.cxf.message.Message; 
import org.apache.cxf.phase.AbstractPhaseInterceptor; 

public class ArtifactOutInterceptor extends AbstractPhaseInterceptor<Message>{   
	 
	private static Logger logger = OdsLog.getTxnLogger("ArtifactOutInterceptor");
	
	public ArtifactOutInterceptor() {   
        //这儿使用pre_stream，意思为在流关闭之前   
        super(Phase.PRE_STREAM);   
    }   
   
 
    @Override
    public void handleMessage(Message message) { 
    	
    	InputStream is = message.getContent(InputStream.class);
    	if (is != null) {
            try {
                String str = IOUtils.toString(is);
                logger.debug("返回响应报文 : \n" + str);
                InputStream ism = new ByteArrayInputStream(str.getBytes());
             //   message.setContent(InputStream.class, ism);
            } catch (IOException e) {
                logger.debug("" , e);
            }
		}
    	
    	try {   
    		   
            OutputStream os = message.getContent(OutputStream.class);   
   
            CachedStream cs = new CachedStream();   
   
            message.setContent(OutputStream.class, cs);   
   
            message.getInterceptorChain().doIntercept(message);   
   
            CachedOutputStream csnew = (CachedOutputStream) message.getContent(OutputStream.class);   
            InputStream in = csnew.getInputStream();   
			
			String xmlstring = IOUtils.toString(in);
            // 处理报文   
			logger.debug("xmlstring \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("</", "</s:");
			logger.debug("A \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("<", "<s:");
			xmlstring = xmlstring.replaceAll("<s:/s:", "</s:");
			logger.debug("B \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("</s:ns2:", "</tns:");
			logger.debug("C \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("<s:ns2:", "<tns:");
			logger.debug("D \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("</s:soap:", "</soap:");
			logger.debug("E \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("<s:soap:", "<soap:");
			logger.debug("F \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("xmlns:ns2=", "xmlns:tns=");
			logger.debug("G \n" + xmlstring);
			xmlstring = xmlstring.replaceAll("xmlns=", "xmlns:s=");
			logger.debug("H \n" + xmlstring);

//			 
			Document document = null;
			try {
				document = DocumentHelper.parseText(xmlstring);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
			logger.debug(" \n" + xmlstring);
			
			Element root = document.getRootElement();
//			root.add(DocumentHelper.createNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/") );
			root.add(DocumentHelper.createNamespace("tns", NameSpace.ODS_WSDL) ); 
			root.add(DocumentHelper.createNamespace("s", NameSpace.ODS_URL) ); 
						
			xmlstring = root.asXML();
			
			// 这里对xml做处理，处理完后同理，写回流中
			IOUtils.copy(new ByteArrayInputStream(xmlstring.getBytes("UTF-8")), os);

			cs.close();
			os.flush();

			message.setContent(OutputStream.class, os);
 
        } catch (Exception e) { 
            logger.error("Error when split original inputStream. CausedBy : " + "\n" + e); 
        } 
    	
    	
    	
    } 
 
    private class CachedStream extends CachedOutputStream { 
    	 
        public CachedStream() { 
            super(); 
        } 
 
        protected void doFlush() throws IOException { 
            currentStream.flush(); 
        } 
 
        protected void doClose() throws IOException { 
 
        } 
 
        protected void onWrite() throws IOException { 
 
        } 
 
    } 
    
 
}
