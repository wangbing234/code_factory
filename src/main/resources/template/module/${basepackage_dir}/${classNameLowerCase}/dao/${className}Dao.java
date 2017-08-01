<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 

package ${basepackage}.${table.classNameLowerCase}.dao;
import com.qk.core.ibatis.dao.BaseDao;
<#include "/java_imports.include">

/**
 * ${table.remarks}dao层接口类
<#include "/java_description.include">
 */
public interface ${className}Dao extends BaseDao<${className}>{

 
}
