package com.myyh.system.controller;
import com.myyh.system.annotation.Anonymous;
import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Menu;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.pojo.vo.MenuVo;
import com.myyh.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @Anonymous
    @LogInfo("动态获取菜单")
    @GetMapping(value = "/build")
    public ResultBean<List<MenuVo>> build() {
        return new ResultBean<List<MenuVo> >(menuService.build());
    }


    @PreAuthorize("@ry.has('menu.list')")
    @LogInfo("查询所有菜单")
    @GetMapping(value = "/pagelist")
    public ResultBean<List<Menu>> pagelist(PageParameter pageParameter) {
        return new ResultBean<List<Menu> >(menuService.getAll(pageParameter));
    }


    @PreAuthorize("@ry.has('menu.list')")
    @LogInfo("查询全部菜单")
    @GetMapping(value = "/list")
    public ResultBean<List<Menu>> list() {
        return new ResultBean<List<Menu> >(menuService.getAll());
    }


    @PreAuthorize("@ry.has('menu.add')")
    @LogInfo("新增菜单")
    @PostMapping
    public ResultBean<Boolean> add(@RequestBody Menu menu) {
        return new ResultBean<Boolean>(menuService.addMenu(menu));
    }


    @PreAuthorize("@ry.has('menu.update')")
    @LogInfo("更新菜单")
    @PutMapping
    public ResultBean<Boolean> update(@RequestBody Menu menu) {
        return new ResultBean<Boolean>(menuService.updateMenu(menu));
    }


    @PreAuthorize("@ry.has('menu.list')")
    @LogInfo("查询上级目录的菜单树")
    @GetMapping(value = "tree")
    public ResultBean<List<Map<String,Object>>> tree (){
        return new ResultBean<List<Map<String,Object>>>(menuService.tree());
    }


    @PreAuthorize("@ry.has('menu.delete')")
    @LogInfo("删除菜单")
    @DeleteMapping(value = "/del/{id}")
    public ResultBean<Boolean> del(@PathVariable Integer id) {
        return new ResultBean<Boolean>(menuService.del(id));
    }


    @PreAuthorize("@ry.has('menu.list')")
	@LogInfo("查询角色拥有的菜单")
	@GetMapping(value = "/roleMenuIdlist/{roleid}")
	public ResultBean<List<Integer>> roleMenuIdlist(@PathVariable Integer roleid) {
		return new ResultBean<List<Integer> >(menuService.roleMenuIdlist(roleid));
	}



}
