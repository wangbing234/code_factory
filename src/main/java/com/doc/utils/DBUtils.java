package com.doc.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.codefactory.CodeGenerator;
import com.doc.core.DBDriverAutoLoad;
import com.doc.core.DBInfo;
import com.doc.core.Log;
import com.doc.core.Parameters;
import com.mysql.jdbc.StringUtils;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public final class DBUtils {

	private Parameters parameters = null;

	private Connection conn = null;

	private Statement stm = null;

	private ResultSet rs = null;
	
	private Map<String, String> tableinfo = new LinkedHashMap<String, String>();

	public DBUtils(Parameters parameters) {
		this.parameters = parameters;
		DBDriverAutoLoad.load();
	}
	
	private Table getRemarks(List<Table> tableList,String tableName){
		for (Table table : tableList) {
			String sqlName=table.getSqlName();
			 if(sqlName.equalsIgnoreCase(tableName)){
				 return table;
			 }
		}
		return null;
	}
	
	private Column getTableCol(Table table,String tcol){
		for ( Column _col : table.getColumns()) {
			if(_col.getSqlName().equalsIgnoreCase(tcol)){
				return _col;
			}
		}
		return null;
	}

	public Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> getDatabaseInfo()
			throws Exception {
		Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> info = new LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>();
		conn = DriverManager.getConnection(DBInfo.getCurrentDriverUrl(parameters.getHost(),parameters.getPort(), parameters.getDatabase()),parameters.getUser(), parameters.getPassword());
		DatabaseMetaData dmd = conn.getMetaData();
		String schema = null;
		if (parameters.getSchema() != null) {
			schema = parameters.getSchema().toUpperCase();
		}
		ResultSet dbrs = dmd.getTables(null, schema, null, new String[] { "TABLE" });
		//获取所有的表
		List<Table> tableList = TableFactory.getInstance().getAllTables();
		while (dbrs.next()) {
			String table_name = dbrs.getString("TABLE_NAME");
			if (!parameters.getTables().isEmpty()) {
				if (!parameters.getTables().contains(table_name)) {
					continue;
				}
			}
			LinkedHashMap<String, LinkedHashMap<String, String>> tablesMap = info.get(table_name);
			if (tablesMap == null) {
				tablesMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
			}

			// tablesMap.put(column_name, columnInfo);

			Log.info("==========================="+ table_name +"===========================");
			ResultSetMetaData rsmd = dbrs.getMetaData();
			Table table = getRemarks(tableList, table_name);
			String remark = table.getRemarks();
			tableinfo.put(dbrs.getString("TABLE_NAME"), StringUtils.isNullOrEmpty(remark)?"":remark);
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				rsmd.getColumnName(i);
				Log.info(rsmd.getColumnName(i) + ":" + dbrs.getString(i));
			}

			Log.info("----------------------------------------------------------");
			ResultSet rsc = getColumns(table_name);
			ResultSetMetaData rscmd = rsc.getMetaData();
			while (rsc.next()) {
				String column_name = rsc.getString("COLUMN_NAME");
				//
				Column columnName = getTableCol(table, column_name);
				LinkedHashMap<String, String> columnInfo = tablesMap.get(column_name);
				if (columnInfo == null)
					columnInfo = new LinkedHashMap<String, String>();
				
				columnInfo.put("column_comment", columnName.getRemarks()==null?"":columnName.getRemarks());

				String typeName = rsc.getString("TYPE_NAME");
				String typeDesc="";
				if ("CHAR".equals(typeName) || "VARCHAR".equals(typeName)) {
					typeDesc=typeName + "("+ rsc.getString("COLUMN_SIZE")+ ")";
				}
				else if(typeName.indexOf("INT")==0){
					typeDesc= "INT("+ rsc.getString("COLUMN_SIZE")+ ")";
				}
				else if("FLOAT".equals(typeName)){
					typeDesc=  "FLOAT("+ columnName.getSize()+ ","+columnName.getDecimalDigits()+")";
				}
				else {
					typeDesc=  typeName;
				}
				columnInfo .put("column_type", typeDesc.toLowerCase());

				String def = String.valueOf(rsc.getObject("COLUMN_DEF"));
				if ("null".equals(def)) {
					columnInfo.put("column_default", "");
				} else {
					columnInfo.put("column_default", def);
				}

				if (columnName.isPk()) {
					columnInfo.put("column_key", "PK");
				} else if(columnName.isFk()) {
					columnInfo.put("column_key", "FK");
				}
				else if(columnName.isUnique()) {
					columnInfo.put("column_key", "U");
				}
				else{
					columnInfo.put("column_key", "");
				}
				
				if (columnName.isNullable()) {
					columnInfo.put("is_nullable", "");
				} else {
					columnInfo.put("is_nullable", "Ｘ");
				}

				StringBuilder sb = new StringBuilder();
				for (int j = 1; j <= rscmd.getColumnCount(); j++) {
					sb.append(rscmd.getColumnName(j)).append("[").append(rsc.getObject(j)).append("]").append(" ");
				}
				Log.info(sb.toString());
				tablesMap.put(column_name, columnInfo);
			}
			info.put(table_name, tablesMap);
			Log.info("");
		}

		stm = conn.createStatement();

		releaseDBResource();
		return info;
	}
	
	public Map<String, String> getTableInfo() {
		return tableinfo;
	}

	public ResultSet getColumns(String tableName) {
		try {
			DatabaseMetaData db2dmd = conn.getMetaData();
			String schema = null;
			if (parameters.getSchema() != null) {
				schema = parameters.getSchema().toUpperCase();
			}
			return db2dmd.getColumns(null, schema, tableName, null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 释放资源
	 * 
	 * @throws SQLException
	 */
	public void releaseDBResource() throws SQLException {
		if (null != rs) {
			rs.close();
		}

		if (null != stm) {
			stm.close();
		}

		if (null != conn) {
			conn.close();
		}
	}

}