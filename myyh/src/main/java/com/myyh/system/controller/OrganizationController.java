package com.myyh.system.controller;

import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Organization;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org")
public class OrganizationController {
	@Autowired
	private OrganizationService organizationService;


	@PreAuthorize("@ry.has('org.list')")
	@LogInfo("查询所有机构")
	@GetMapping(value = "/pagelist")
	public ResultBean<List<Organization>> pagelist(PageParameter pageParameter) {
		return new ResultBean<List<Organization> >(organizationService.getAll(pageParameter));
	}


	@PreAuthorize("@ry.has('org.update')")
	@LogInfo("修改用户")
	@PostMapping(value = "/update")
	public ResultBean update(@RequestBody Organization organization){
		return new ResultBean<>(organizationService.update(organization));
	}


	@PreAuthorize("@ry.has('org.list')")
	@LogInfo("查询机构层级关系")
	@GetMapping(value = "tree")
	public ResultBean<List<Map<String,Object>>> tree (){
		return new ResultBean<List<Map<String,Object>>>(organizationService.tree());
	}

	@PreAuthorize("@ry.has('org.list')")
	@LogInfo("查询所有启用机构")
	@GetMapping(value = "allEnabledOrgs")
	public  ResultBean<List<Map<String,Object>>> allEnabledOrgs() {
		return new ResultBean<List<Map<String,Object>>>(organizationService.getallEnabledOrgs());
	}



	@PreAuthorize("@ry.has('org.add')")
	@LogInfo("新增机构")
	@PostMapping
	public ResultBean<Boolean> add(@RequestBody Organization organization) {
		return new ResultBean<Boolean>(organizationService.addOrganization(organization));
	}


	@PreAuthorize("@ry.has('org.delete')")
	@LogInfo("删除菜单")
	@DeleteMapping(value = "/del/{id}")
	public ResultBean<Boolean> del(@PathVariable Integer id) {
		return new ResultBean<Boolean>(organizationService.del(id));
	}

}
