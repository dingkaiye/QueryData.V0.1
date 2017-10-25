package com.ods.manager;

import org.apache.log4j.Logger;

import com.ods.log.OdsLog;

public class Testrun extends Thread {
	private static Logger logger = OdsLog.getTxnLogger("Testrun"); 
	@Override
	public void run(){
		int sleep = 100000 ;
		boolean quickflg = false;
		//测试报文解析 
		//String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><test>test</test><ENTITY><FCCODE>1001</FCCODE><MTCODE>SDSD</MTCODE><CURRSTOCK>1234566</CURRSTOCK><UPLOADTIME>2016-08-10 15:26:14</UPLOADTIME></ENTITY><ENTITY><FCCODE>1002</FCCODE><MTCODE>GRDFF</MTCODE><CURRSTOCK>L6T7824SXGN175448</CURRSTOCK><UPLOADTIME>2016-08-10 15:26:14</UPLOADTIME></ENTITY></DATA>";
		String message = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> "
				+ "<DATA>                                            " 
				+ "<test>test</test>	"
				+ "<ENTITY>                                         "
				+ "		<FCCODE>1001</FCCODE>                        "
				+ "		<MTCODE>SDSD</MTCODE>                        "
				+ "		<CURRSTOCK>1234566</CURRSTOCK>               "
				+ "		<UPLOADTIME>2016-08-10 15:26:14</UPLOADTIME> "
				+ "	</ENTITY>                                        "
				+ "	<ENTITY>                                         "
				+ "		<FCCODE>1002</FCCODE>                        "
				+ "		<MTCODE>GRDFF</MTCODE>                       "
				+ "		<CURRSTOCK>L6T7824SXGN175448</CURRSTOCK>     "
				+ "		<UPLOADTIME>2016-08-10 15:26:14</UPLOADTIME> "
				+ "	</ENTITY>                                        "
				+ "	<ReqBody>                                        "
				+ "		<ft_no>123</ft_no>                           "
				+ "		<num>987</num>                               "
				+ "		<sa_acct_no>963</sa_acct_no>                 " 
				+ "	</ReqBody>        " + "</DATA>  ";

		while (true) {
			try{
//				synchronized (this) {
					Handler handler = new Handler();
					
					handler.run();
//					TxnMessager testmessage = new TxnMessager(message);
//					 QueueManager.SysQueueAdd(Constant.UnpackQueue, testmessage);
					logger.debug("AAAAAAAAAAAAAAAAA");
					
					sleep = 5000;
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					logger.debug("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf");
//				}
				//Thread.sleep(sleep);
			}catch (Exception e){
				logger.error(e);
				/*sleep = 10000;
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			}
			//Thread.sleep(100);
		}
	}

}
