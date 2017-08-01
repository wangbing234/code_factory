package com.codefactory;


import java.util.List;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class CoreGenerator {

	public static void main(String[] args) throws Exception {
		genterTable();
	}
	
	public static List<Table> getTables() {
		return TableFactory.getInstance().getAllTables();
	}
	
	public static void genterTable() throws Exception{
				String templatePath = CoreGenerator.class.getResource("/").getPath()+"template";
				GeneratorFacade generatorFacade = new GeneratorFacade();
				Generator generator = generatorFacade.getGenerator();
				generator.addTemplateRootDir(templatePath);
//				 ɾ�������������Ŀ¼//
//				 generatorFacade.deleteOutRootDir();
				// ͨ�����ݿ�������ļ�
				GeneratorProperties.setProperty("basepackage", "com.qk.core.module");
				setModuleDoain("system");
				GeneratorProperties.setProperty("useInformationSchema","true");
				
				 // ��Ҫ�Ƴ��ı���ǰ׺,ʹ�ö��Ž��зָ����ǰ׺,ʾ��ֵ 
				GeneratorProperties.setProperty("tableRemovePrefixes", "meb_,t_,ai_,biz_");
//				GeneratorControl
				List<Table> tableList = TableFactory.getInstance().getAllTables();
				for (Table table : tableList) {
					System.out.println(table.getSqlName()+"  --   "+table.getClassName() +"  ע�ͣ� "+table.getRemarks());
				}
//				generatorFacade.generateByTable("t_account");
//				Table table = TableFactory.getInstance().getTable("meb_statistics");
//				LinkedHashSet<Column> list = table.getColumns();
//				for (Column column : list) {
//					System.out.println(column.getColumnName() +" "+column.getSqlName()); 
//				}
				// �Զ��������ݿ��е����б������ļ�,templateΪģ��ĸ�Ŀ¼
				generatorFacade.generateByAllTable();
				
				// ��table����ɾ���ļ�
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
