<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.entity;
import com.qk.core.ibatis.annotation.po.TableName;
import com.qk.core.ibatis.annotation.po.FieldName;
import javax.validation.constraints.NotNull;
import com.qk.core.ibatis.beans.Po;
import com.qk.core.ibatis.util.date.DateUtil;
import java.util.Date;

import javax.validation.constraints.NotNull;
/**
 * ${table.remarks}数据实体类
<#include "/java_description.include">
 */
@TableName(name="${table.getSqlName()}")
public class ${className}  extends Po{  
      
    <#list table.columns as column>  
	    /**  
	     * ${column.remarks}
	     */ 
    	<#if  column.getSqlName()!=column.columnNameLower>
    	@FieldName(name="${column.getSqlName()}")
    	</#if>
    	<#if  !column.isNullable() && !column.isPk()>
    	@NotNull(message = "${column.remarks}不能为空！")
    	</#if>
	    private ${column.simpleJavaType} ${column.columnNameLower};
    </#list>  
 
<@generateJavaColumns/>  
 
<#macro generateJavaColumns>
    <#list table.columns as column>  
        <#if column.isDateTimeColumn>  
	    public String get${column.columnName}String() {  
	        return DateUtil.formatDatetime(get${column.columnName}());  
	    }  
	    </#if>      
	    public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {  
	        this.${column.columnNameLower} = ${column.columnNameLower};  
	    }  
	      
	    public ${column.simpleJavaType} get${column.columnName}() {  
	        return this.${column.columnNameLower};  
	    }  
    </#list>  
</#macro> 
}