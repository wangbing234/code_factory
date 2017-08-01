<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.qk.core.ibatis.service.impl.BaseServiceImpl;
import ${basepackage}.${table.classNameLowerCase}.service.${className}Service;
import ${basepackage}.${table.classNameLowerCase}.dao.${className}Dao;
<#include "/java_imports.include">
/**
 * ${table.remarks}Service实现类
<#include "/java_description.include">
 */
@Service
@Component("${classNameLower}Service${module_domain_up}")
public class ${className}ServiceImpl extends BaseServiceImpl<${className},${className}Dao> implements ${className}Service {

}
