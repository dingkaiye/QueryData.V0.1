package com.ods.log;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * @author ding_kaiye
 * @date 2017-09-12
 */
public class LogDateCheckService extends Thread {

	private static Logger thisLogger = LogManager.getLogger("log4j"); 

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				thisLogger.warn("Thread.sleep 出现异常");
				e.printStackTrace();
			}
			OdsLog.editLogPathDate();
		}
	}
}
