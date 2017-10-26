package com.ods.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialNo {
	
	private static long serialNo = 0;
	
	//private static Logger thisLogger = LogManager.getLogger("SysLog");  
	private static SimpleDateFormat dfTime = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
	//private static SimpleDateFormat dfTime = new SimpleDateFormat("yyyyMMdd"); //设置日期格式
	private static String currentDate = dfTime.format(new Date()).substring(0, 8); 
	private static DecimalFormat decimalFormat = new DecimalFormat("0000000000"); 
	private static String currentSerialNo = dfTime.format(new Date()) + "0000000000"; //初始流水

	
	/**
	 * 获取系统流水号
	 * 流水号格式为 8位日期 ＋10位数字
	 * 数字每日归零
	 * @author ding_kaiye
	 * @return nextSerialNo
	 */
	public static synchronized String getNextSerialNo() {
		String nextSerialNo = null;
		int dateLength = 8 ;
		String nowDate = dfTime.format(new Date());
		if (! (nowDate.substring(0, dateLength)).equals(currentDate) || Long.MAX_VALUE == serialNo ) {  // 断言
			currentDate = nowDate.substring(0, dateLength);
			serialNo = 1;
		}else {
			serialNo++;
		}

		nextSerialNo = nowDate + decimalFormat.format(serialNo);
		//记录本流水号为当前流水号
		currentSerialNo = nextSerialNo; 
		
		return nextSerialNo;
	}

	public static String getCurrentDate() {
		return currentDate;
	}

	/**
	 * 获取当前系统流水号
	 * 流水号格式为 8位日期 ＋10位数字
	 * 数字每日归零
	 * @author ding_kaiye
	 * @return 系统当前流水号
	 */
	public static String getCurrentSerialNo() {
		return currentSerialNo;
	}


}
