package com.ods.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存一行数据库
 * @author ding_kaiye
 *
 */
public class DbDataLine {

	long rows =0 ;       // 行号
	int columnCount = 0 ; // 本行中字段的数量, 对应实际字段个数, 如果存在字段,计数从1开始, 0表示没有字段 
//	private Map<Integer, String> seqNameLine   = new HashMap<Integer, String>();  // <列号, 列名>
	private Map<Integer, Object> seqValueLine  = new HashMap<Integer, Object>();  // <列号, 列对象>
	private Map<String, Object> valueLine      = new HashMap<String, Object>();   // <列名, 数据值>
//	private Map<String, Object> columnTypeLine = new HashMap<String, Object>();   // <列名, 列类型>
	private StringBuffer keyValue = new StringBuffer();   //本行数据的keyValue
	//private HashMap<String, DataColumn> dataLine    = null;  // <列名, 列类型>
	
	public DbDataLine() {
		
	}
	
	public DbDataLine(ResultSet resultSet) throws SQLException {
		this.init(resultSet, null);
	}

	public DbDataLine(ResultSet resultSet, String keyDefine) throws SQLException {
		this.init(resultSet, keyDefine);
	}
	
	/**
	 * 根据指定的定义生成一个key值, 用来唯一的标记一行数据;
	 * 对本行数据生成 keyValue, 并返回生成的 keyValue
	 * @param keyDefine
	 */
	public String generateKeyValue(String keyDefine) {
		// 初始化完成, 生成本行对应KeyValue
		keyValue.setLength(0);
		for (String key : keyDefine.split(",")) {
			String name = key.split(":")[0];
			int length = new Integer(key.split(":")[1]); // 按照指定长度截取
//			String value = (this.getColumnType(name) == null ? "": this.getColumnType(name).toString());
			String value = (this.valueLine.get(key) == null ? "": this.valueLine.get(key).toString());
			value = String.format("%-"+ length +"s", value).substring(0, length);  //按照定义长度补位,数据超长则截取
			keyValue.append(value);
		}
		return keyValue.toString();
	}
	

	/**
	 * 通过 resultSet 指定的行 初始化一行数据
	 * @param resultSet
	 * @return
	 * @throws SQLException 
	 */
	public void init(ResultSet resultSet) throws SQLException {
		this.init(resultSet, null);
	}
	
	public void init(ResultSet resultSet, String keyDefine) throws SQLException {
		rows =0 ;       
		columnCount = 0 ; 
//		seqNameLine.clear();
		seqValueLine.clear();    
		valueLine.clear();       
//		columnTypeLine.clear();  
		keyValue.setLength(0);   
		
		this.addByResultSet(resultSet);   // 根据ResultSet初始化一行数据
		if(keyDefine != null){
			this.generateKeyValue(keyDefine); //生成 keyValue
		}
	}
	
	
	//获取值  
	/**
	 * 通字段对应序号获取字段数据
	 * @param columnNo
	 * @return
	 */
	public Object get(int columnNo) {
		Object columnValue = null;
		//通过字段名获取字典值
		columnValue = seqValueLine.get(columnNo) ;
		return columnValue ;
	}
	
	/**
	 * 通字段名称获取字段数据
	 * @param columnName
	 * @return
	 */
	public Object get(String columnName) {
		Object columnValue = null;
		columnValue = valueLine.get(columnName) ;
		return columnValue ;
	}
	
//	/**获取列属性  
//	 * @param columnName
//	 * @return
//	 */
//	public Object getType(String columnName) {
//		Object columnType = null;
//		columnType = columnTypeLine.get(columnName) ;
//		return columnType ;
//	}
	
//	/**获取对象类型  
//	 * 
//	 * @param columnName
//	 * @return
//	 */
//	public Object getColumnType(String columnName) {
//		Object columnData = null;
//		columnData = columnTypeLine.get(columnName) ;
//		return columnData ;
//	}
	
	/**
	 * 根据字段号获取对应的字段名
	 */
//	public String getColumnNameBySeq(int seq){
//		return seqNameLine.get(seq);
//	}

	/**
	 * 将本行  ResultSet 指向的数据初始化到  本类中 
	 * @param resultSet
	 * @param keyDefine
	 * @return
	 * @throws SQLException
	 */
	private boolean addByResultSet(ResultSet resultSet) throws SQLException {
		rows = resultSet.getRow(); //当前行数 
		
		ResultSetMetaData data = resultSet.getMetaData();  //获取MetaData, 为从 MetaData中获取属性
		// 获得所有列的数目及实际列数
		columnCount = data.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String columnName = data.getColumnLabel(i); // 取列标签
//			seqNameLine.put(i, columnName);             // <列号, 列名>
//			columnTypeLine.put(columnName, data.getColumnType(i)); // <列名, 列类型>

			Object valueObject = resultSet.getObject(i);
			if (valueObject != null) {
				valueLine.put(columnName, valueObject);      // <列名, 列对象>
				seqValueLine.put(i, valueObject);            // <列序号, 列对象>
			} else { //如果为取到对象, 使用空串 
				valueLine.put(columnName, "");
				seqValueLine.put(i, "");                     // <列序号, 列对象>
			}
			
			//节约资源,暂不使用 dataColumn 保留此处代码,需要扩展使用时放开  20170923 
//			DataColumn dataColumn = new DataColumn();
//			// 获得指定列的列名
//			dataColumn.setCatalogName(data.getColumnName(i));
//			// 默认的列的标题
//			dataColumn.setColumnClassName(data.getColumnLabel(i));
//			// 获得指定列的列值
//			dataColumn.setColumnType(data.getColumnType(i));
//			// 获得指定列的数据类型名
//			dataColumn.setColumnTypeName(data.getColumnTypeName(i));
//			// 所在的Catalog名字
//			dataColumn.setCatalogName(data.getCatalogName(i));
//			// 对应数据类型的类
//			dataColumn.setColumnClassName(data.getColumnClassName(i));
//			// 在数据库中类型的最大字符个数
//			dataColumn.setColumnDisplaySize(data.getColumnDisplaySize(i));
//			// 获得列的模式
//			dataColumn.setSchemaName(data.getSchemaName(i));
//			// 某列类型的精确度(类型的长度)
//			dataColumn.setPrecision(data.getPrecision(i));
//			// 小数点后的位数
//			dataColumn.setScale(data.getScale(i));
//			// 获取某列对应的表名
//			dataColumn.setTableName(data.getTableName(i));
//			// 是否自动递增
//			dataColumn.setAutoInctement(data.isAutoIncrement(i));
//			// 在数据库中是否为货币型
//			dataColumn.setCurrency(data.isCurrency(i));
//			// 是否为空
//			dataColumn.setIsNullable(data.isNullable(i));
//			// 是否为只读
//			dataColumn.setReadOnly(data.isReadOnly(i));
//			// 能否出现在where中
//			dataColumn.setSearchable(data.isSearchable(i));
//			// 数据值
//			dataColumn.setColumnData(resultSet.getObject(i));
//			dataLine.put(columnName, dataColumn);

		}
		return true;
	}
	
	public long getRows() {
		return rows;
	}

	public void setRows(long rows) {
		this.rows = rows;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

//	public Map<Integer, String> getColumnNoLine() {
//		return seqNameLine;
//	}
//
//	public void setColumnNoLine(Map<Integer, String> columnNoLine) {
//		this.seqNameLine = columnNoLine;
//	}

	public Map<Integer, Object> getSeqValueLine() {
		return seqValueLine;
	}

	public void setSeqValueLine(HashMap<Integer, Object> seqValueLine) {
		this.seqValueLine = seqValueLine;
	}

	public Map<String, Object> getValueLine() {
		return valueLine;
	}

	public void setValueLine(Map<String, Object> valueLine) {
		this.valueLine = valueLine;
	}

//	public Map<String, Object> getColumnTypeLine() {
//		return columnTypeLine;
//	}
//
//	public void setColumnTypeLine(Map<String, Object> columnTypeLine) {
//		this.columnTypeLine = columnTypeLine;
//	}

//	public HashMap<String, DataColumn> getDataLine() {
//		return dataLine;
//	}
//
//	public void setDataLine(HashMap<String, DataColumn> dataLine) {
//		this.dataLine = dataLine;
//	}

	public StringBuffer getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(StringBuffer keyValue) {
		this.keyValue = keyValue;
	}

}
