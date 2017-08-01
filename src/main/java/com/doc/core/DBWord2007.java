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

public final class DBWord2007 {

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
	//标签id
	private static  int TREE_NUM=0;
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
        	String formatpath=DBWord2007.class.getResource("/").getPath()+"\\doc\\format.docx";
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
        createDocBase1(xDocument, "1.1约束说明");
        createDocText(xDocument, "		PK	主键约束，默认使用自增字段rid作为主键");
        createDocText(xDocument, "		FK	外键约束");
        createDocText(xDocument, "		U	唯一约束");
        createDocText(xDocument, "		X	不允许为空");
        createDocText(xDocument, "	约束可能是数据库内约束，也可能是业务逻辑实现约束，不限定。");
    }
    
    private static void createDoc1_2(XWPFDocument xDocument){
        createDocBase1(xDocument, "1.2命名说明");
        createDocText(xDocument, "		数据库中实际字段命名为文档命名的大写格式，各字段命名严格遵守“大写下划线命名法”。表名亦同。");
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
    public static void productWordForm(Map<String, String> tableinfo, Map<String, LinkedHashMap<String, LinkedHashMap<String, String>>> data,
                                       Parameters parameters) throws Exception {
    	
    	  // 新建的word文档对象  
        XWPFDocument xDocument = new XWPFDocument();  
        // 获取新建文档对象的样式  
        XWPFStyles newStyles = xDocument.createStyles();  
        // 关键行// 修改设置文档样式为静态块中读取到的样式  
        newStyles.setStyles(wordStyles);  
        XWPFParagraph titlePar = xDocument.createParagraph();
        XWPFRun tr1 = titlePar.createRun();
        titlePar.setStyle(TREE_ROOT);
        String titleString="1，数据库设计文档";
        tr1.setText(titleString);
        tr1.setFontSize(20);
        tr1.setTextPosition(10);
        tr1.setBold(true);
        tr1.getParagraph().setAlignment(ParagraphAlignment.LEFT);
        addLogo(tr1);
        
        addParagraphContentBookmarkBasicStyle(titlePar,"标题");  
        createSignName(xDocument, "创建日期："+DateUtil.formatDate(new Date()));
        createSignName(xDocument, "版权：趋快科技(武汉)有限公司");
        createSignName(xDocument, "创建人：XXX");
        createDoc1_1(xDocument);
        createDoc1_2(xDocument);
        createDocBase1(xDocument, "1.3数据表");
        Iterator<String> tableNameIter = data.keySet().iterator();
        while (tableNameIter.hasNext()) {
        	//表名称
            String tableName = tableNameIter.next();
            //表注释
            String tableRemark=tableinfo.get(tableName);
            XWPFParagraph xp = xDocument.createParagraph();
            XWPFRun r1 = xp.createRun();
            String tableNameMark=null;
            ++TREE_NUM;
            if(!StringUtils.isNullOrEmpty(tableRemark)){
            	tableNameMark="1.3."+TREE_NUM+" "+tableRemark + "(" + tableName+")";
            }
            else{
            	tableNameMark="1.3."+TREE_NUM+" "+tableName;
            }
            r1.setText(tableNameMark);
            r1.setFontSize(15);
            r1.setTextPosition(10);
            r1.setBold(true);
            xp.setStyle(TREE_ROOT3);
            addParagraphContentBookmarkBasicStyle(xp,tableNameMark);  
            //创建段落空行
            XWPFParagraph p = xDocument.createParagraph();
            p.setAlignment(ParagraphAlignment.CENTER);
            p.setWordWrap(true);
            
            //创建表格
            LinkedHashMap<String, LinkedHashMap<String, String>> columns = data.get(tableName);
            int rows = columns.size();
            XWPFTable xTable = xDocument.createTable(rows + 1, TABLE_COL_NUMBER);
            //表格属性
            CTTblPr tablePr = xTable.getCTTbl().addNewTblPr();
            //表格宽度
            CTTblWidth width = tablePr.addNewTblW();
            width.setW(BigInteger.valueOf(TABLE_WIDTH));

           //表头
           String[] colTitleArray={"字段名称","字段描述","数据类型","默认值","约束","空值"};
           xTable.getRow(0).setHeight(TITLE_HEIGTH);
           for (int k = 0; k < colTitleArray.length; k++) {
			String colTitle = colTitleArray[k];
			setCellText(xDocument, xTable.getRow(0).getCell(k), colTitle, "CCCCCC", getCellWidth(k));
           }
            
            int i = 1;// 下一行
            int j = 0;// 列column索引

            Map<String, LinkedHashMap<String, String>> keyColumnMap = keyColumns(columns);
            for (Iterator<String> columnNameIter = keyColumnMap.keySet().iterator(); columnNameIter.hasNext();) {
                String column_name = columnNameIter.next();
                LinkedHashMap<String, String> columnsAtt = keyColumnMap.get(column_name);
                int cwidth = getCellWidth(j);
                setCellText(xDocument, xTable.getRow(i).getCell(j), column_name, "FFFFFF", cwidth);
                ++j;
                Iterator<String> columnTypeIter = columnsAtt.keySet().iterator();
                while (columnTypeIter.hasNext()) {
                    String colum_type = columnTypeIter.next();
                    cwidth = getCellWidth(j);
                    setCellText(xDocument, xTable.getRow(i).getCell(j),columnsAtt.get(colum_type), "FFFFFF", cwidth);
                    j++;
                }
                ++i;// 下一行
                j = 0;// 恢复第一列
            }

            Iterator<String> cloumnsNameIter = columns.keySet().iterator();

            while (cloumnsNameIter.hasNext()) {
            	xTable.getRow(i).setHeight(TITLE_HEIGTH);
                String colum_name = cloumnsNameIter.next();
                LinkedHashMap<String, String> columnsAtt = columns.get(colum_name);
                int cwidth = getCellWidth(j);
                if (xTable.getRow(i) == null) continue;
                setCellText(xDocument, xTable.getRow(i).getCell(j),colum_name, "FFFFFF", cwidth);

                j++;

                Iterator<String> columnTypeIter = columnsAtt.keySet().iterator();

                while (columnTypeIter.hasNext()) {
                    String colum_type = columnTypeIter.next();
                    cwidth = getCellWidth(j);
                    setCellText(xDocument, xTable.getRow(i).getCell(j),columnsAtt.get(colum_type), "FFFFFF", cwidth);
                    j++;
                }
                j = 0;// 恢复第一列
                ++i; //下一行
            }
            
			
            //表名称label
			setRowTitle(xTable.insertNewTableRow(0),tableName,"表名称"); 
			 //表名称label
			setRowTitle(xTable.insertNewTableRow(1),tableRemark,"表说明"); 
        }

        //生成文档名称
        genterDoc(parameters, xDocument);
    }
    
    
    private static void addLogo(XWPFRun tr1){
    	String formatpath=DBWord2007.class.getResource("/").getPath()+"\\doc\\logoback.png";
    	File formFile=new File(formatpath);
    	InputStream pictureData;
		try {
			pictureData = new FileInputStream(formFile);
			tr1.addPicture(pictureData, Document.PICTURE_TYPE_PNG, formatpath, 200, 200);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		String documentName=fileRootFile.getAbsolutePath() + "\\数据库设计("+parameters.getDatabase() + ""+DateUtil.formatDate(new Date())+").docx";
		System.out.println("生成文档路径："+documentName); 
        FileOutputStream fos = new FileOutputStream(documentName);
        xDocument.write(fos);
        fos.close();
	}

    /**
     * 设置表格上方数据
     * @param row0
     * @param tableLabel
     * @param tableRemark
     */
	private static void setRowTitle(XWPFTableRow row0,String tableLabel,String tableRemark) {
		//表名称label
		row0.addNewTableCell();
		XWPFTableCell cellLabebl = row0.getCell(0);
		setCellWidth(cellLabebl, TABLE_WIDTH_TEXT);
		cellLabebl.setColor("CCCCCC");
		cellLabebl.setText(tableRemark);
		//表名称value
		row0.addNewTableCell();
		XWPFTableCell cellValue = row0.getCell(1);
		setCellWidth(cellValue, TABLE_WIDTH-TABLE_WIDTH_TEXT);
		cellValue.setText(tableLabel);
		row0.setHeight(TITLE_HEIGTH);
	}
    
	/**
	 * 设置表格单元格数据
	 * @param xDocument
	 * @param cell
	 * @param text
	 * @param bgcolor
	 * @param width
	 */
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell, String text, String bgcolor, int width) {
    	CTTcPr cellPr = cell.getCTTc().addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
    	cell.setColor(bgcolor);
    	cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        cell.setText(text);
    }
    
    /**
     * 设置单元格宽度
     * @param cell 单元格
     * @param width 宽度
     */
    private static void setCellWidth(XWPFTableCell cell, int width) {
    	CTTcPr cellPr = cell.getCTTc().addNewTcPr();
        cellPr.addNewTcW().setW(BigInteger.valueOf(width));
    }

    /**
     * 检索出主键key相关的列
     * 
     * @param columnsMap
     * @return
     */
    private static Map<String, LinkedHashMap<String, String>> keyColumns(HashMap<String, LinkedHashMap<String, String>> columnsMap) {
        Map<String, LinkedHashMap<String, String>> keyColumnMap = new HashMap<String, LinkedHashMap<String, String>>();

        Iterator<String> cloumnsNameIter = columnsMap.keySet().iterator();
        while (cloumnsNameIter.hasNext()) {
            String colum_name = cloumnsNameIter.next();
            LinkedHashMap<String, String> columnsAtt = columnsMap.get(colum_name);
            if (columnsAtt.get("column_key").equals("是")) {
                keyColumnMap.put(colum_name, columnsAtt);
                cloumnsNameIter.remove();
            }
        }
        return keyColumnMap;
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
    
    /**
     * 设置行高
     * @param index
     * @return
     */
    private static int getCellWidth(int index) {
    	int cwidth = 1000;
        if (index==0) {
        	cwidth = 2100;
        } else if (index==1) {
        	cwidth = 2700;
        } else if (index==2) {
        	cwidth = 1900;
        } else if (index==3) {
        	cwidth = 900;
        }  else if (index==4) {
        	cwidth = 900;
        } else if (index==5) {
        	cwidth = 700;
        }
        return cwidth;
    }
}
