package com.ods.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件操作类
 * @author ding_kaiye
 */
public class Config {
	
	
	public static  Properties getPropertiesConfig(String configFile) throws IOException {
		Properties properties = loadConfigPropertiesFile(configFile);
		return  properties ;
	}
	
	/**
	 * 读取./config/ 文件夹下指定的 properties 配置文件, 并返回 对应的 properties 
	 * @author ding_kaiye
	 * @param string	     配置文件名
	 * @return Properties 对象
	 * @throws IOException 
	 */
	public static  Properties loadConfigPropertiesFile(String configFile) throws IOException {
		String webRootPath = "." + File.separator + "config" + File.separator ;  //存放于 ./config/ 中
		Properties properties = loadPropertiesFile(webRootPath , configFile);
		return properties;
	}
	
	/**
	 * 读取 指定 文件夹下 指定的 properties 配置文件, 并返回 对应的 properties 
	 * @author ding_kaiye
	 * @param rootPath 文件所在路径
	 * @param string   配置文件名
	 * @return Properties 对象
	 * @throws IOException 
	 */
	public static  Properties loadPropertiesFile(String path, String configFile) throws IOException {
		if ( !path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		if (null == configFile || configFile.equals("")) {
			throw new IllegalArgumentException("Properties file path can not be null" + configFile);
		}
		Properties properties = loadPropertiesFile(path + configFile);
		return properties;
	}
	
	
	
	/**
	 * 按照 参数给出的路径 读取 指定的 properties 配置文件, 并返回 对应的 properties 
	 * @author ding_kaiye
	 * @param string    配置文件名称(含路径)
	 * @return Properties 对象
	 * @throws IOException 
	 */
	private static  Properties loadPropertiesFile(String configFile) throws IOException {
		if (null == configFile || configFile.equals("")) {
			//logger.error("Properties file path can not be null" + configFile);
			throw new IllegalArgumentException("Properties file path can not be null" + configFile);
		}
		
		InputStream inputStream = null;
		Properties properties = null;
		try {
			File file = new File(configFile);
			inputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(inputStream);
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

}
