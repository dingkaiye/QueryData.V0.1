package com.ods.common;

public class Constant {
	
	//config Name 
	public static final String SysConfig = "SysConfig.properties";  // 系统队列定义文件
	public static final String ESBPACK_CONFIG_FILE = "esbpack.properties";  // ESB报文定义文件
	public static final String MaxTxnCnt = "MaxTxnCnt";  // 系统允许容纳最大交易数量 
	
	
	// QUEUEName
	public static final String QueueList = "QueueList";  // 系统中队列清单
	
	public static final String HandlerQuene = "HandlerQuene"; // 
	public static final String UnpackQueue = "UnpackQueue";   // 待解包队列
	public static final String PackQueue = "PackQueue";       // 待组包队列
	public static final String TxnQueue = "TxnQueue";         // 待处理交易队列
	public static final String ManagerQuene = "ManagerQuene"; // 管理者队列 
	public static final String FailQueue = "FailQueue";       // 失败消息队列
	public static final String SuccessQueue = "SuccessQueue";       // 成功消息队列
	
	/** ESB组包配置 */ 
	public static final String EsbReqPack = "EsbReqPack";
	public static final String EsbRspPack = "EsbRspPack";
	
	/**EsbReqBody 名称*/
	public static final String EsbReqBody = "ReqBody";
	
	/** keyDefine 在交易配置文件中的名称*/
	public static final String keyDefine = "key"; 

	/** 交易输出列 在交易配置文件中的名称 */
	public static final String RspBody = "RspBody";
	public static final String TimeOut = "TimeOut";
	
	/** ESB 报文中定义, 系统处理成功 代码 */
	public static final String EsbSuccessStatus = "S"; 
	
	/** 交易花名册文件所在的路径 */
	public static final String TxnConfigPath = "./config/TxnConfig/";  
	/** 交易花名册文件的默认文件名 */
	public static final String TxnRosterFileName =  "TxnRoster.properties" ; 
	
	/** 返回报文头中的 服务版本号 */
	public static final String Vrsn = "0.1";
	/** 本系统编号 */
	public static final String SysId = "ODS";
}
