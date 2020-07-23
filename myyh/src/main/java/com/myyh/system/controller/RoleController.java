package com.myyh.system.controller;

import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Role;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.PageResult;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.pojo.vo.RoleMenuParameter;
import com.myyh.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PreAuthorize("@ry.has('role.list')")
    @LogInfo("查询所有角色")
    @GetMapping(value = "/list")
    public ResultBean list(){
        return new ResultBean<List<Role>>(roleService.findAll());
    }


    @PreAuthorize("@ry.has('role.list')")
    @LogInfo("分页查询角色")
    @GetMapping(value = "/pagelist")
    public PageResult pageList(PageParameter pageRequest){
        return new PageResult<Role>(roleService.findByPage(pageRequest));
    }


    @PreAuthorize("@ry.has('role.update')")
    @LogInfo("修改角色")
    @PostMapping(value = "/update")
    public ResultBean update(@RequestBody Role role){
        return new ResultBean<>(roleService.update(role));
    }


    @PreAuthorize("@ry.has('role.add')")
    @LogInfo("添加角色")
    @PostMapping(value = "/add")
    public ResultBean add(@RequestBody Role role){
        return new ResultBean<>(roleService.save(role));
    }


    @PreAuthorize("@ry.has('role.delete')")
    @LogInfo("删除角色")
    @DeleteMapping(value = "/del/{id}")
    public ResultBean delete(@PathVariable Integer id){
        return new ResultBean<>(roleService.delete(id));
    }


    @PreAuthorize("@ry.has('role.config')")
    @LogInfo("配置角色权限")
    @RequestMapping(value = "/saveRolePermissons")
    public ResultBean saveRolePermissons(@RequestBody RoleMenuParameter roleMenuParameter){
        return new ResultBean<>(roleService.saveRolePermissons(roleMenuParameter));
    }

}
