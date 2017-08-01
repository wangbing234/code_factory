package com.doc.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

import com.doc.utils.DateUtil;
import com.mysql.jdbc.StringUtils;

import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public final class IFWord2008 {

	//表格高度
	private final static Integer TABLE_WIDTH=9200;
	//标题文字长度
	private final static Integer TABLE_WIDTH_TEXT=2500;
	//表格行高
	private final static Integer TITLE_HEIGTH=400;
	//表格行高
	private final static Integer TABLE_COL_NUMBER=6;
	//一级大纲
	private final static  String TREE_ROOT="1";
	//二级大纲
	private final static  String TREE_ROOT2="2";
	//三级大纲
	private final static  String TREE_ROOT3="3";
	//三级大纲
	private final static  String TREE_ROOT4="6";
	//标签id
	private static  int TREE_COL_NUM=1;
	//标签id
	private static  Long MARK_ID=0l;
	
	 /** 
     * word整体样式 
     */  
    private static CTStyles wordStyles = null;  
  
    /** 
     * Word整体样式 
     */  
    static {  
        XWPFDocument template;  
        try {  
            // 读取模板文档  
        	String formatpath=IFWord2008.class.getResource("/").getPath()+"\\doc\\format.docx";
            template = new XWPFDocument(new FileInputStream(formatpath));  
            // 获得模板文档的整体样式  
            wordStyles = template.getStyle();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (XmlException e) {  
            e.printStackTrace();  
        }  
    }  
    
    
    private static void createDoc1_1(XWPFDocument xDocument){
        createDocBase1(xDocument, "1.1备注说明");
        createDocText(xDocument, "		(备注：返回值所有都为json自字符串：code：000为成功(其它都为失败)：msg：消息：data：返回的数据，一下说明只是针对data的数据)");
    }
    
    private static void createDocBase1(XWPFDocument xDocument,String title){
    	XWPFParagraph p = xDocument.createParagraph();
    	XWPFRun tr2 = p.createRun();
    	p.setStyle(TREE_ROOT2);
        tr2.setText(title);
        tr2.setFontSize(16);
        tr2.setTextPosition(10);
        tr2.getParagraph().setAlignment(ParagraphAlignment.LEFT);
    }
    
    private static void createDocBaseLevel3(XWPFDocument xDocument,String title){
    	XWPFParagraph p = xDocument.createParagraph();
    	XWPFRun tr2 = p.createRun();
    	p.setStyle(TREE_ROOT3);
        tr2.setText(title);
        tr2.setFontSize(14);
        tr2.setTextPosition(10);
        tr2.getParagraph().setAlignment(ParagraphAlignment.LEFT);
    }
    
    
    private static void createDocBaseTable4(XWPFDocument xDocument,String title){
    	XWPFParagraph p = xDocument.createParagraph();
    	XWPFRun tr2 = p.createRun();
    	p.setStyle(TREE_ROOT4);
        tr2.setText(title);
        tr2.setFontSize(13);
        tr2.getParagraph().setAlignment(ParagraphAlignment.LEFT);
    }
    
    private static void createDocText(XWPFDocument xDocument,String content){
    	XWPFRun tr2 = xDocument.createParagraph().createRun();
        tr2.setText(content);
        tr2.setFontSize(12);
    }
    
    
    /**
     * 生成word，表格
     * @param data
     * @throws Exception
     */
    public static void productWordForm(List<Table> tableList,Parameters parameters) throws Exception {
    	
    	  // 新建的word文档对象  
        XWPFDocument xDocument = new XWPFDocument();  
        // 获取新建文档对象的样式  
        XWPFStyles newStyles = xDocument.createStyles();  
        // 关键行// 修改设置文档样式为静态块中读取到的样式  
        newStyles.setStyles(wordStyles);  
        XWPFParagraph titlePar = xDocument.createParagraph();
        XWPFRun tr1 = titlePar.createRun();
        titlePar.setStyle(TREE_ROOT);
        String titleString="1，接口文档";
        tr1.setText(titleString);
        tr1.setFontSize(20);
        tr1.setTextPosition(10);
        tr1.setBold(true);
        tr1.getParagraph().setAlignment(ParagraphAlignment.LEFT);
        
        addParagraphContentBookmarkBasicStyle(titlePar,"标题");  
        createSignName(xDocument, "创建日期："+DateUtil.formatDate(new Date()));
        createSignName(xDocument, "版权：趋快科技(武汉)有限公司");
        createSignName(xDocument, "创建人：XXX");
        createDoc1_1(xDocument);
        for (Table table : tableList) {
        	createInterface(table,xDocument);
		}

        //生成文档名称
        genterDoc(parameters, xDocument);
    }
    
    /**
     * 创建接口
     * @param tableList
     * @param xDocument
     */
    private static void createInterface(Table table, XWPFDocument xDocument){
    	TREE_COL_NUM++;
    	String title="";
    	if(StringUtils.isNullOrEmpty(table.getRemarks())){
    		title=table.getClassNameLowerCase();
    	}
    	else{
    		title=table.getRemarks()+"("+table.getClassNameLowerCase()+")";
    	}
    	createDocBaseLevel3(xDocument, "1."+TREE_COL_NUM+title+"模块"); 
    	//插入记录
    	createInsert(table, xDocument);
    	//更新记录
    	createUpdate(table, xDocument);
    	//分页查询
    	createPage(table, xDocument);
    	//分页查询
    	createGetById(table, xDocument);
    	//分页查询
    	createGetDelete(table, xDocument);
    	//分页查询
    	createGetDeletes(table, xDocument);
    	
    	
    }
    
    private static final String SPACE1="	";
    private static final String SPACE12="				";
    
    
    private static void createGetDeletes(Table table, XWPFDocument xDocument) {
    	String module_domain = GeneratorProperties.getProperty("module_domain"); 
    	createDocBaseTable4(xDocument, SPACE1+"批量删除"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/deletes");
    	createDocText(xDocument, SPACE12+"2)参数说明：");
    	createDocText(xDocument, SPACE12+"		"+table.getRemarks()+"对象的id数组：ids(用逗号隔开)");
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
    	createDocText(xDocument, SPACE12+"		data:删除的记录条数");
    	createDocText(xDocument, SPACE12+"4)参数方式 :GET");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}
    
    
    private static void createGetDelete(Table table, XWPFDocument xDocument) {
    	String module_domain = GeneratorProperties.getProperty("module_domain"); 
    	createDocBaseTable4(xDocument, SPACE1+"删除"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/delete/{Id}");
    	createDocText(xDocument, SPACE12+"2)参数说明：");
    	createDocText(xDocument, SPACE12+"		"+table.getRemarks()+"对象的id：Id(rest)");
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
    	createDocText(xDocument, SPACE12+"		data:删除的记录条数");
    	createDocText(xDocument, SPACE12+"4)参数方式 :GET");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}
    
    
    private static void createGetById(Table table, XWPFDocument xDocument) {
    	String module_domain = GeneratorProperties.getProperty("module_domain"); 
    	createDocBaseTable4(xDocument, SPACE1+"根据id获取"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/getById/{Id}");
    	createDocText(xDocument, SPACE12+"2)参数说明：");
    	createDocText(xDocument, SPACE12+"		"+table.getRemarks()+"对象的id：Id(rest)");
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
	    	for (Column col : table.getColumns()) {
					createDocText(xDocument, SPACE12+"					"+col.getColumnNameLower()+":"+col.getRemarks());
			}
    	
    	createDocText(xDocument, SPACE12+"4)参数方式 :GET");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}
    
    private static void createPage(Table table, XWPFDocument xDocument) {
    	String module_domain = GeneratorProperties.getProperty("module_domain"); 
    	createDocBaseTable4(xDocument, SPACE1+"分页查询"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/page/{offset}/{pageSize}");
    	createDocText(xDocument, SPACE12+"2)参数说明(json格式)：");
    	createDocText(xDocument, SPACE12+"		page的url中 :");
    	createDocText(xDocument, SPACE12+"					offset：第几页(rest)");
    	createDocText(xDocument, SPACE12+"					pageSize：分页size大小(rest)");
    	
    	for (Column col : table.getColumns()) {
			if(!col.isPk()){
				createDocText(xDocument, SPACE12+"					"+col.getColumnNameLower()+":"+col.getRemarks());
			}
		}
    	
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
    	createDocText(xDocument, SPACE12+"		total:总记录条数");
    	createDocText(xDocument, SPACE12+"		list：数据列表");
    	createDocText(xDocument, SPACE12+"		pageSize：分页大小");
    	createDocText(xDocument, SPACE12+"		offset：第几页");
    	createDocText(xDocument, SPACE12+"4)参数方式 :GET");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}
    
    
    private static void createUpdate(Table table, XWPFDocument xDocument) {
    	String module_domain = GeneratorProperties.getProperty("module_domain"); 
    	createDocBaseTable4(xDocument, SPACE1+"更新"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/update");
    	createDocText(xDocument, SPACE12+"2)参数说明(json格式)：");
    	for (Column col : table.getColumns()) {
			if(!col.isPk()){
				createDocText(xDocument, SPACE12+"		"+col.getColumnNameLower()+":"+col.getRemarks());
			}
		}
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
    	createDocText(xDocument, SPACE12+"		data:更新的记录条数");
    	createDocText(xDocument, SPACE12+"4)参数方式 :POST");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}

	private static void createInsert(Table table, XWPFDocument xDocument) {
		String module_domain = GeneratorProperties.getProperty("module_domain"); 
		createDocBaseTable4(xDocument, SPACE1+"增加"+table.getRemarks()+"对象"); 
    	createDocText(xDocument, SPACE12+"1)URL地址:/"+module_domain+"/"+table.getClassNameLowerCase()+"/insert");
    	createDocText(xDocument, SPACE12+"2)参数说明(json格式)：");
    	for (Column col : table.getColumns()) {
			if(!col.isPk()){
				createDocText(xDocument, SPACE12+"		"+col.getColumnNameLower()+":"+col.getRemarks());
			}
		}
    	createDocText(xDocument, SPACE12+"3)返回值说明：");
    	createDocText(xDocument, SPACE12+"		data:插入记录的id");
    	createDocText(xDocument, SPACE12+"4)参数方式 :POST");
    	createDocText(xDocument, SPACE12+"5)备注:暂无");
	}
    
    
   

    /**
     * 生成文档方法
     * @param parameters
     * @param xDocument
     * @throws FileNotFoundException
     * @throws IOException
     */
	private static void genterDoc(Parameters parameters, XWPFDocument xDocument)
			throws FileNotFoundException, IOException {
		String fileRoot=parameters.getPath();
		File fileRootFile=new File(fileRoot);
		if(!fileRootFile.exists())
			fileRootFile.mkdirs();
		String documentName=fileRootFile.getAbsolutePath() + "\\接口文档("+parameters.getDatabase() + ""+DateUtil.formatDate(new Date())+").docx";
		System.out.println("生成文档路径："+documentName); 
        FileOutputStream fos = new FileOutputStream(documentName);
        xDocument.write(fos);
        fos.close();
	}

    
	 
    
    /**
     * 头部签名对象
     * @param xDocument 文档对象
     * @param value 创建值
     */
	private static void createSignName(XWPFDocument xDocument,String value){
		XWPFRun tr2 = xDocument.createParagraph().createRun();
        tr2.setText(value);
        tr2.setFontSize(12);
        tr2.setTextPosition(10);
        tr2.getParagraph().setAlignment(ParagraphAlignment.RIGHT);
	}
	
	
	
	/**
	 * 添加书签 
	 * @param p
	 * @param bookMarkName
	 */
	  public static void addParagraphContentBookmarkBasicStyle(XWPFParagraph p,String bookMarkName) {  
	    CTBookmark bookStart = p.getCTP().addNewBookmarkStart();  
	    BigInteger markId=BigInteger.valueOf(MARK_ID++);
		bookStart.setId(markId);  
	    bookStart.setName(bookMarkName);  
	    CTMarkupRange bookEnd = p.getCTP().addNewBookmarkEnd();  
	    bookEnd.setId(markId);  
	  
	  }  
    
    
}
