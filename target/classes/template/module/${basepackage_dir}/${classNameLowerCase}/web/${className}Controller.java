<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.web;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qk.core.framework.BaseController;
import com.qk.core.framework.dto.ResultBean;
import com.qk.core.ibatis.beans.PagerModel;
import com.qk.core.ibatis.sql.criteria.And;
import com.qk.core.ibatis.sql.criteria.Restrictions;
import com.qk.core.ibatis.sql.order.Order;

import ${basepackage}.${table.classNameLowerCase}.service.*;
<#include "/java_imports.include">
/**
 * ${table.remarks}  Controller
<#include "/java_description.include">
 */
/**前后端/模块**/
@Controller
@RequestMapping("/font/${classNameLower}")
public class ${className}Controller  extends BaseController{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	${className}Service ${classNameLower}Service;
	
	/**
	 * 查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	private ResultBean list(@RequestBody ${className} ${classNameLower}) {
		<#list table.columns as column>  
				<#if  column_index==0>
					And and =new And("${column.columnNameLower}", ${classNameLower}.get${column.columnName}(),Restrictions.EQ);
				 </#if>
				 <#if  column_index!=0  && column.isStringColumn>
				 	and.add("${column.columnNameLower}", ${classNameLower}.get${column.columnName}(),Restrictions.EQ);
				 </#if>
		 </#list>  
		 Order order=new Order();
		 <#list table.columns as column>  
			order.add("${column.columnNameLower}");
		 </#list>
		List<${className}> list =  ${classNameLower}Service.list(and,order);
		return success(list);
	}
	
	/**
	 * 查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	@ResponseBody
	private ResultBean insert(@RequestBody ${className} ${classNameLower}) {
		int i = ${classNameLower}Service.add(${classNameLower});
		return success(i);
	}
	
	/**
	 * 查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	private ResultBean update(@RequestBody ${className} ${classNameLower}) {
		return success(${classNameLower}Service.update(${classNameLower}));
	}
	
	
	
	/**
	 * 查询分页列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/page/{offset}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	private ResultBean page(@PathVariable("offset") Integer offset,@PathVariable("pageSize") Integer pageSize ,@RequestBody ${className} ${classNameLower}) {
		
		<#list table.columns as column>  
		<#if  column_index==0>
			And and =new And("${column.columnNameLower}", ${classNameLower}.get${column.columnName}(),Restrictions.EQ);
		 </#if>
		 <#if  column_index!=0 && column.isStringColumn>
		 	and.add("${column.columnNameLower}", ${classNameLower}.get${column.columnName}(),Restrictions.EQ);
		 </#if>
		 </#list>  
		 Order order=new Order();
		 <#list table.columns as column>  
			order.add("${column.columnNameLower}");
		 </#list>
		PagerModel<${className}> pageModel = ${classNameLower}Service.page(and,order,offset,pageSize);
		return success(pageModel);
	}

	/**
	 * 查询当个数据
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getById/{${classNameLower}Id}", method = RequestMethod.GET)
	@ResponseBody
	private ResultBean getById(@PathVariable("${classNameLower}Id") Long ${classNameLower}Id) {
		${className} ${classNameLower} = ${classNameLower}Service.get(${classNameLower}Id);
		return success(${classNameLower});
	}
	
	/**
	 * 查询当个数据
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delete/{${classNameLower}Id}", method = RequestMethod.GET)
	@ResponseBody
	private ResultBean deleteById(@PathVariable("${classNameLower}Id") Long ${classNameLower}Id) {
		return  success(${classNameLower}Service.del(${classNameLower}Id));
	}
	
}
