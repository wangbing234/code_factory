<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.qk.core.ibatis.service.impl.BaseServiceImpl;
import ${basepackage}.${table.classNameLowerCase}.dao.${className}Dao;
import ${basepackage}.${table.classNameLowerCase}.service.${className}Service;
<#include "/java_imports.include">
/**
 * ${table.remarks}  Service实现类
<#include "/java_description.include">
 */
@Service
@Component
public class ${className}ServiceImpl extends BaseServiceImpl<${className}> implements ${className}Service {

	@Resource
	${className}Dao ${classNameLower}Dao;
	
}
