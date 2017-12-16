package com.ods.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;

import com.ods.log.OdsLog;

public class ArtifactInInterceptor extends AbstractPhaseInterceptor<Message> {
	
	
	private static Logger logger = OdsLog.getTxnLogger("ArtifactInInterceptor");
	
	public ArtifactInInterceptor() {   
        //这儿使用pre_stream，意思为在流关闭之前   
        super(Phase.PRE_STREAM);   
    }   
	
	@Override
	public void handleMessage(Message message) { 
		InputStream is = message.getContent(InputStream.class);
		if (is != null) {
            try {
                String str = IOUtils.toString(is);
                logger.debug("收到请求报文 : \n" + str);
                InputStream ism = new ByteArrayInputStream(str.getBytes());
                message.setContent(InputStream.class, ism);
            } catch (IOException e) {
                logger.debug("" , e);
            }
            
		}
	}
	
	
}
