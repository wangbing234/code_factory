package com.codefactory;

import java.util.List;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class TestGenerator {
	public static void genterTable() throws Exception{
		String templatePath = "C:/Users/bing.wang/Downloads/codeFactory_Maven_Webapp/src/main/resources/template";
		GeneratorFacade generatorFacade = new GeneratorFacade();
		Generator generator = generatorFacade.getGenerator();
		generator.addTemplateRootDir(templatePath);
//		 删除生成器的输出目录//
		 generatorFacade.deleteOutRootDir();
		// 通过数据库表生成文件
		List<Table> tableList = TableFactory.getInstance().getAllTables();
		GeneratorProperties.setProperty("basepackage", "com.qk.module.font");
		 // 需要移除的表名前缀,使用逗号进行分隔多个前缀,示例值 
		GeneratorProperties.setProperty("tableRemovePrefixes", "meb_,t_,ai_,biz_");
		
		for (Table table : tableList) {
			System.out.println(table.getSqlName()+"  --   "+table.getClassName());
		}
//		generatorFacade.generateByTable("biz_entry_sub");
//		Table table = TableFactory.getInstance().getTable("meb_statistics");
//		LinkedHashSet<Column> list = table.getColumns();
//		for (Column column : list) {
//			System.out.println(column.getColumnName() +" "+column.getSqlName()); 
//		}
		// 自动搜索数据库中的所有表并生成文件,template为模板的根目录
//		 g.generateByAllTable();
		
		// 按table名字删除文件
//		 g.deleteByTable("table_name", "template");
//		
//		Column s=null;
//		s.simpleJavaType
}
}
