package com.myyh.system.controller;

import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Circle;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.service.CircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/circle")
public class CircleController {
	@Autowired
	private CircleService circleService;


	@PreAuthorize("@ry.has('cir.list')")
	@LogInfo("查询所有圈子")
	@GetMapping(value = "/pagelist")
	public ResultBean<List<Circle>> pagelist(PageParameter pageParameter) {
		return new ResultBean<List<Circle> >(circleService.getAll(pageParameter));
	}


	@PreAuthorize("@ry.has('cir.update')")
	@LogInfo("修改用户")
	@PostMapping(value = "/update")
	public ResultBean update(@RequestBody Circle organization){
		return new ResultBean<>(circleService.update(organization));
	}


	@PreAuthorize("@ry.has('cir.list')")
	@LogInfo("查询圈子层级关系")
	@GetMapping(value = "tree")
	public ResultBean<List<Map<String,Object>>> tree (){
		return new ResultBean<List<Map<String,Object>>>(circleService.tree());
	}

	@PreAuthorize("@ry.has('cir.list')")
	@LogInfo("查询所有启用圈子")
	@GetMapping(value = "allEnabledOrgs")
	public  ResultBean<List<Map<String,Object>>> allEnabledOrgs() {
		return new ResultBean<List<Map<String,Object>>>(circleService.getallEnabledOrgs());
	}



	@PreAuthorize("@ry.has('cir.add')")
	@LogInfo("新增圈子")
	@PostMapping
	public ResultBean<Boolean> add(@RequestBody Circle organization) {
		return new ResultBean<Boolean>(circleService.addOrganization(organization));
	}


	@PreAuthorize("@ry.has('cir.delete')")
	@LogInfo("删除菜单")
	@DeleteMapping(value = "/del/{id}")
	public ResultBean<Boolean> del(@PathVariable Integer id) {
		return new ResultBean<Boolean>(circleService.del(id));
	}

}
