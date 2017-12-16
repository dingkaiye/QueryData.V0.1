package com.ods.db;

public class DataColumn {

	long columnNo ;                         // 列号
	private String columnName = null;       // 获得指定列的列名
	private String columnLabel = null;      // 默认的列的标题
	private int columnType ;                // 获得指定列的列值
	private String columnTypeName = null ;  // 获得指定列的数据类型名
	private String catalogName = null ;     // 所在的Catalog名字
	private String columnClassName = null ; // 对应数据类型的类
	private int columnDisplaySize ;         // 在数据库中类型的最大字符个数
	private String schemaName = null ;      // 获得列的模式
	private int precision ;                 // 某列类型的精确度(类型的长度)
	private int scale ;                     // 小数点后的位数
	private String tableName = null ;       // 获取某列对应的表名
	private boolean isAutoInctement ;       // 是否自动递增
	private boolean isCurrency ;            // 在数据库中是否为货币型
	private int isNullable ;                // 是否为空
	private boolean isReadOnly ;            // 是否为只读
	private boolean isSearchable ;          // 能否出现在where中
	private Object columnData = null ;      // 数据值
	
	public long getRows() {
		return columnNo;
	}
	public void setRows(long columnNo) {
		this.columnNo = columnNo;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnLabel() {
		return columnLabel;
	}
	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}
	public int getColumnType() {
		return columnType;
	}
	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	public String getColumnClassName() {
		return columnClassName;
	}
	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}
	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}
	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public boolean isAutoInctement() {
		return isAutoInctement;
	}
	public void setAutoInctement(boolean isAutoInctement) {
		this.isAutoInctement = isAutoInctement;
	}
	public boolean isCurrency() {
		return isCurrency;
	}
	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}
	public int getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(int isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isReadOnly() {
		return isReadOnly;
	}
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public boolean isSearchable() {
		return isSearchable;
	}
	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}
	public Object getColumnData() {
		return columnData;
	}
	public void setColumnData(Object columnData) {
		this.columnData = columnData;
	}

}
