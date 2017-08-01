<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${table.classNameLowerCase}.web;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qk.core.framework.BaseController;
import com.qk.core.framework.dto.ResultBean;
import com.qk.core.ibatis.beans.PagerModel;
import com.qk.core.ibatis.sql.criteria.And;
import com.qk.core.ibatis.sql.criteria.Restrictions;
import com.qk.core.ibatis.sql.order.Order;

import ${basepackage}.${table.classNameLowerCase}.service.${className}Service;
<#include "/java_imports.include">
/**
 * ${table.remarks}Controller前端接口类
<#include "/java_description.include">
 */
@Controller
@ResponseBody
@RequestMapping("/${module_domain}/${classNameLower}")//域/模块
public class ${className}Controller  extends BaseController{
	
	//日志打印类
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//注入${className}Service
	@Resource(name="${classNameLower}Service${module_domain_up}")
	${className}Service ${classNameLower}Service;
	
	/**
	 * 插入${table.remarks}(${className})对象
	 * @param ${classNameLower} ${className}对象
	 * @return
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	private ResultBean insert(@RequestBody  @Valid ${className} ${classNameLower},BindingResult bindResult) {
		//字段规则校验
		if(bindResult.hasErrors()){
			return fail(bindResult.getFieldError().getDefaultMessage());
		}
		int i = ${classNameLower}Service.add(${classNameLower});
		logger.info("插入${table.remarks}(${className})对象成功！");
		return success(i);
	}
	
	/**
	 * 查询${table.remarks}(${className})列表
	 *@param ${classNameLower} ${className}对象
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	private ResultBean update(@RequestBody  @Valid ${className} ${classNameLower},BindingResult bindResult) {
		//字段规则校验
		if(bindResult.hasErrors()){
			return fail(bindResult.getFieldError().getDefaultMessage());
		}
		int flag = ${classNameLower}Service.update(${classNameLower});
		logger.info("插入${table.remarks}(${className})对象成功！");
		return success(flag);
	}
	
	/**
	 * 分页查询${table.remarks}(${className})分页对象
	 * @param offset 第几页
	 * @param pageSize 分页大小
	 * @param ${classNameLower} ${className}对象
	 * @return
	 */
	@RequestMapping(value = "/page/{offset}/{pageSize}", method = RequestMethod.GET)
	private ResultBean page(@PathVariable("offset") Integer offset,@PathVariable("pageSize") Integer pageSize ,@Param("${classNameLower}") ${className} ${classNameLower}) {
		
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
		logger.info("分页查询${table.remarks}(${className})分页对象成功！");
		return success(pageModel);
	}

	/**
	 * 根据id获取${table.remarks}(${className})对象
	 * @param ${classNameLower}Id 主键id
	 * @return
	 */
	@RequestMapping(value = "/getById/{${classNameLower}Id}", method = RequestMethod.GET)
	private ResultBean getById(@PathVariable("${classNameLower}Id") Long ${classNameLower}Id) {
		${className} ${classNameLower} = ${classNameLower}Service.get(${classNameLower}Id);
		logger.info("根据id获取${table.remarks}(${className})对象成功！");
		return success(${classNameLower});
	}
	
	/**
	 * 根据id删除${table.remarks}(${className})对象
	 * @param ${classNameLower}Id 主键id
	 * @return
	 */
	@RequestMapping(value = "/delete/{${classNameLower}Id}", method = RequestMethod.GET)
	private ResultBean deleteById(@PathVariable("${classNameLower}Id") Long ${classNameLower}Id) {
		int flag = ${classNameLower}Service.del(${classNameLower}Id);
		logger.info("根据id删除${table.remarks}(${className})对象成功！");
		return  success(flag);
	}
	
	/**
	 * 根据id删除对象
	 * @param ids id的数组
	 * @return 修改记录的条数
	 */
	@RequestMapping(value = "/deletes", method = RequestMethod.GET)
	public ResultBean deleteByIds(@RequestParam(value = "ids") String[] ids){
		int flag = ${classNameLower}Service.deleteByIds(ids);
		logger.info("批量删除${table.remarks}(${className})对象成功！");
		return  success(flag);
	}
	
}
