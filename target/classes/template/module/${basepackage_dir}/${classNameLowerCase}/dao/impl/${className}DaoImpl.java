<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.dao.impl;


import org.springframework.stereotype.Component;
import com.qk.core.ibatis.dao.impl.BaseDaoImpl;
import ${basepackage}.${table.classNameLowerCase}.dao.${className}Dao;

<#include "/java_imports.include">

/**
 * ${table.remarks}  dao实现类
<#include "/java_description.include">
 */
@Component
public class ${className}DaoImpl extends  BaseDaoImpl<${className}> implements ${className}Dao {

}
