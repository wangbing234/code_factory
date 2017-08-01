package com.codefactory;


import java.util.List;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class CodeGenerator {

	public static void main(String[] args) throws Exception {
		getTables();
	}
	
	public static List<Table> getTables() {
		return TableFactory.getInstance().getAllTables();
	}
	
	public static void genterTable() throws Exception{
				String templatePath = CodeGenerator.class.getResource("/").getPath()+"template";
				GeneratorFacade generatorFacade = new GeneratorFacade();
				Generator generator = generatorFacade.getGenerator();
				generator.addTemplateRootDir(templatePath);
//				 删锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟侥柯�//
//				 generatorFacade.deleteOutRootDir();
				// 通锟斤拷锟斤拷锟捷匡拷锟斤拷锟斤拷锟斤拷募锟�
				GeneratorProperties.setProperty("basepackage", "com.qk.module.font");
				setModuleDoain("font");
				GeneratorProperties.setProperty("useInformationSchema","true");
				 // 锟斤拷要锟狡筹拷锟侥憋拷锟斤拷前缀,使锟矫讹拷锟脚斤拷锟叫分革拷锟斤拷锟角白�,示锟斤拷值 
				GeneratorProperties.setProperty("tableRemovePrefixes", "biz_");
//				GeneratorControl
				List<Table> tableList = TableFactory.getInstance().getAllTables();
				for (Table table : tableList) {
					System.out.println(table.getSqlName()+"  --   "+table.getClassName() +"  注锟酵ｏ拷 "+table.getRemarks());
				}
//				generatorFacade.generateByTable("biz_exit_sub");
//				Table table = TableFactory.getInstance().getTable("meb_statistics");
//				LinkedHashSet<Column> list = table.getColumns();
//				for (Column column : list) {
//					System.out.println(column.getColumnName() +" "+column.getSqlName()); 
//				}
				// 锟皆讹拷锟斤拷锟斤拷锟斤拷锟捷匡拷锟叫碉拷锟斤拷锟叫憋拷锟斤拷锟斤拷锟侥硷拷,template为模锟斤拷母锟侥柯�
				generatorFacade.generateByAllTable();
				
				// 锟斤拷table锟斤拷锟斤拷删锟斤拷锟侥硷拷
//				 g.deleteByTable("table_name", "template");
//				
//				Column s=null;
//				s.simpleJavaType
	}
	
	private static void setModuleDoain(String str)
	{
		GeneratorProperties.setProperty("module_domain", str);
		GeneratorProperties.setProperty("module_domain_up", str.substring(0, 1).toUpperCase()+str.substring(1));
		GeneratorProperties.setProperty("user_name", System.getProperty("user.name"));
	}

}
