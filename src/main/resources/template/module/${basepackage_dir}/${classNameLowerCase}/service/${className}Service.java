<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.service;

import com.qk.core.ibatis.service.BaseService;
import ${basepackage}.${table.classNameLowerCase}.dao.${className}Dao;
<#include "/java_imports.include">
/**
 * ${table.remarks}Service接口
<#include "/java_description.include">
 */
public interface ${className}Service  extends BaseService<${className},${className}Dao>{

}
