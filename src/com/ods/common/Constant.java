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
	
	/** 交易入参配置名称 */
	public static final String inParm = "inParm";
	public static final String RspBody = "RspBody";
	
	/** keyDefine 在交易配置文件中的名称*/
	public static final String keyDefine = "key"; 

	/** 交易输出列 在交易配置文件中的名称 */
	public static final String outParm = "outParm";
	public static final String TimeOut = "TimeOut";
	
	/** ESB 报文中定义, 系统处理成功 代码 */
	public static final String EsbSuccessStatus = "S"; 
}
