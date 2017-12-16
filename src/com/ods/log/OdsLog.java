package com.ods.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class OdsLog {
		
	// TRACE DEBUG INFO WARNING ERROR FATAL OFF
	
	//private static Logger rootLogger = LogManager.getRootLogger();  
	private static Logger thisLogger = LogManager.getLogger("log4j");  
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	private static String currentDate = df.format(new Date()); 
	private static ArrayList <String> dateLoggerList = new ArrayList <String>();  //按日期存放日志文件的logger清单
	
/**
 * @author ding_kaiye
 * @param ArrayList<String> loggerList
 * @date  20170912
 * 添加日期到log日期的目录中
 * 最终日志格式为 ......./日期/日志文件名
 * 当发现日期改变,更改所有配有YYYYMMDD路径中的日期
 */
	synchronized private static void  editLogPathDate ( ArrayList<String> loggerList) {
		String currentDateTem = df.format(new Date());
		thisLogger.debug("检查 日志路径日期 当前日期:" + currentDateTem + " 原日期:" +  currentDate );
		if (currentDate.equals(currentDateTem)) {
			return;
		} else {
			currentDate = currentDateTem;
		}
		
		for(int i = 0; i < loggerList.size(); i++) {
			
			String loggerName = loggerList.get(i);
			Logger localLogger = LogManager.getLogger(loggerName);  //logger 
			String strFilePath = null ;   //新日志文件路径
			
			@SuppressWarnings("unchecked")
			Enumeration<Appender>  appenderEa =  localLogger.getAllAppenders();
			Appender appender = null ;
			
			while (  appenderEa.hasMoreElements() ) { //遍历 logger 下的 Appender
				appender = appenderEa.nextElement();
				if ( !(appender instanceof  FileAppender) ) { // 仅处理 FileAppender 类型的 Appender
					thisLogger.info("跳过 logger:[" + loggerName + "] " + " appender:[" + appender.getName() + "]" );
					continue;
				}
				thisLogger.info("开始处理 logger:[" + loggerName + "] " + " appender:[" + appender.getName() + "]" );
				
				String splitFlg = "/";
				String configFileName = ((FileAppender) appender).getFile();
				String[] filepaths = configFileName.split(splitFlg);
				StringBuffer filePath = new StringBuffer();

				if (filepaths.length <= 1) {
					thisLogger.info("logger:[" + loggerName + "] " + " appender: " + appender.getName() + "路径深度:" + filepaths.length + ", 文件路径修改跳过");
					continue;
				} else {
					filepaths[filepaths.length - 2] = currentDate;

					filePath = filePath.append(filepaths[0]);
					for (int j = 1; j < filepaths.length; j++) {
						filePath.append(splitFlg);
						filePath = filePath.append(filepaths[j]);
					}
					strFilePath = new String(filePath);
					//thisLogger.info("logger:[" + loggerName + "] " + " appender: " + appender.getName() + ":" + strFilePath);
					((FileAppender) appender).setFile(strFilePath);
					((FileAppender) appender).activateOptions(); // 激活设置
					thisLogger.info( loggerName + "." + appender.getName() + " 日志修改完成: " + strFilePath);
				}
			}
		}
	}
	
	protected static void  editLogPathDate () {
		editLogPathDate(dateLoggerList);
	}
	

	
	/**
	 * @author ding_kaiye
	 * @param <E>
	 * @param loggerName
	 * @date  20170912
	 * 以 log4j.logger.TxnLog 为模板, 创建新 logger (loggerName)
	 * TxnLog 交易日志模板 
	 * 修改日志路径,日志路径添加日期,日志文件名修改为 入参 loggerName
	 * @return 
	 */
	
	public static Logger getTxnLogger(Class<?> inclass) {
		 Logger logger = getTxnLogger(inclass.getName());
		 return logger;
	} 
	
	 public static Logger getTxnLogger(String loggerName) {

		Logger  templetLogger = Logger.getLogger("TxnLog");
		
		Logger logger =  LogManager.exists(loggerName) ;
		if ( logger != null) {
			thisLogger.debug(loggerName + " 已经存在在,不再创建新实例");
			return logger;
		}else {
			thisLogger.info(loggerName + " 不存在,创建新实例");
			logger =  LogManager.getLogger(loggerName) ;
		}
		
		logger.removeAllAppenders();
		
		// 赋值属性
		logger.setLevel(templetLogger.getLevel());		
		logger.setAdditivity(templetLogger.getAdditivity());
		
		// 处理 Appender 
		@SuppressWarnings("unchecked")
		Enumeration<Appender>  appenderEa =  templetLogger.getAllAppenders();
		
		while ( appenderEa.hasMoreElements() ) { //遍历 logger 下的 Appender
			Appender templetAppender = appenderEa.nextElement();
			if (templetAppender instanceof ConsoleAppender) { //控制台输出,直接添加 
				thisLogger.debug("开始创建 ConsoleAppender: " + logger.getName() + "." + templetAppender.getName() );
				logger.addAppender(templetAppender);
			}else if (templetAppender instanceof  FileAppender) { //FileAppender ,创建新 FileAppender 后添加 
				thisLogger.debug("开始创建 FileAppender: " + logger.getName() + "." + templetAppender.getName() );
				
				FileAppender sourceAppender = (FileAppender) templetAppender;  

				RollingFileAppender appender = new RollingFileAppender();  // appender 
				appender.setName(loggerName + "FileAppender");
				 
				//设置 maxFileSize 和 maxBackupIndex
				if ( templetAppender instanceof RollingFileAppender) {
					long maxFileSize = ((RollingFileAppender)sourceAppender).getMaximumFileSize();
					int maxBackupIndex = ((RollingFileAppender)sourceAppender).getMaxBackupIndex();
					appender.setMaximumFileSize(maxFileSize);
					appender.setMaxBackupIndex(maxBackupIndex);
				}else {
					// 使用log4j 默认值 
				}
				
				//设置文件名,文件路径中增加日期
				String splitFlg = "/"; //路径分隔符
				String configFileName = sourceAppender.getFile();
				String[] filepaths = configFileName.split(splitFlg);
				filepaths[filepaths.length-1] = currentDate;
				
				StringBuffer fileName = new StringBuffer();
				for (int j = 0; j < filepaths.length; j++) { 
					fileName = fileName.append(filepaths[j]);
					fileName.append(splitFlg);
				}
				fileName = fileName.append(loggerName).append(".log");
				appender.setFile(new String(fileName));
				
				//设置 Layout 
				Layout sourceLayout = ((FileAppender) sourceAppender).getLayout();
				String conversionPattern = null;
				if (sourceLayout instanceof PatternLayout) {
					conversionPattern = ((PatternLayout) sourceLayout).getConversionPattern();
				} else {
					conversionPattern = "[%d{ISO8601}][%t] [%-5p] %m%n";
				}
				PatternLayout patternLayout = new PatternLayout ();
				patternLayout.setConversionPattern(conversionPattern);
				appender.setLayout(patternLayout);
				appender.setAppend(true);
				
				appender.activateOptions(); // 激活设置
				logger.addAppender(appender);
			}
			else {
				thisLogger.info("跳过 Appender" + logger.getName() + "." + templetAppender.getName() );
				continue;
			}
		}
		thisLogger.info("日志记录实例:" + loggerName + "实例创建完成");
		if( ! dateLoggerList.contains(loggerName) ) {
			dateLoggerList.add(loggerName);
			thisLogger.debug(loggerName + "加入检查列表完成");
		}
		return logger;
	}

	 /**
	  * 提供一个获取 log4j logger 的接口
	  * @param loggerName
	  * @return
	  */
	public static Logger getLogger(String loggerName) {
		Logger logger = LogManager.getLogger(loggerName);  
		return logger;
	}
	
}
