package com.myyh.system.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.myyh.system.dao.MenuDao;
import com.myyh.system.exception.BadRequestException;
import com.myyh.system.pojo.Menu;
import com.myyh.system.pojo.Role;
import com.myyh.system.pojo.User;
import com.myyh.system.pojo.vo.MenuMetaVo;
import com.myyh.system.pojo.vo.MenuQuery;
import com.myyh.system.pojo.vo.MenuVo;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.util.QueryHelp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 构建菜单树
     *
     * @return
     */
    public List<MenuVo> build() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED, "登录状态过期");
        }
        String username = userDetails.getUsername();
        log.debug("username:" + username);
        User user = userService.findByName(username);
        List<Role> roleList = roleService.findByUserId(user.getId());
        List<MenuVo> menus = findByRoles(roleList);
        return menus;
    }

    /**
     * 根据角色id查询用户角色集合
     *
     * @param roleList
     * @return
     */
    private List<MenuVo> findByRoles(List<Role> roleList) {
        Set<Integer> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toSet());
        List<Menu> menus = menuDao.findByRoles_IdInAndEnabledAndIsMenuOrderBySortAsc(roleIds, true,true);
        log.debug("用户已开启菜单：" + JSONObject.toJSONString(menus));
        List<Menu> toList = menus.stream().distinct().collect(Collectors.toList());
        Map<String, Object> menuMap = buildTree(toList);
        List<Menu> menusList = (List<Menu>) menuMap.get("content");
        List<MenuVo> menuVos = buildMenus(menusList);
        log.debug("后台构建完成的菜单为：" + JSONObject.toJSONString(menuVos));
        return menuVos;

    }


    /**
     * 构建前台菜单数据
     *
     * @param menuList
     * @return
     */
    public List<MenuVo> buildMenus(List<Menu> menuList) {
        List<MenuVo> list = new LinkedList<>();
        menuList.forEach(menu -> {
                    if (menu != null) {
                        List<Menu> menuDtoList = menu.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menu.getComponentName()) ? menu.getComponentName() : menu.getMenuName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menu.getPid() == 0 ? "/" + menu.getPath() : menu.getPath());
                        menuVo.setHidden(menu.getHidden());

                        if (menu.getPid() == 0) {
                            menuVo.setComponent(StrUtil.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                        } else if (!StrUtil.isEmpty(menu.getComponent())) {
                            menuVo.setComponent(menu.getComponent());
                        }
                        menuVo.setMeta(new MenuMetaVo(menu.getMenuName(), menu.getIcon()));
                        if (!CollectionUtils.isEmpty(menuDtoList)) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menu.getPid() == 0) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());

                            menuVo1.setPath("index");
                            menuVo1.setName(menuVo.getName());
                            menuVo1.setComponent(menuVo.getComponent());

                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }


    /**
     * 构建tree
     *
     * @param menuDtos
     * @return
     */
    public Map<String, Object> buildTree(List<Menu> menuDtos) {
        List<Menu> trees = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();
        for (Menu menuDTO : menuDtos) {
            if (menuDTO.getPid() == 0) {
                trees.add(menuDTO);
            }
            for (Menu it : menuDtos) {
                if (it.getPid().equals(menuDTO.getId())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        Map<String,Object> map = new HashMap<>(2);
        if(trees.size() == 0){
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        map.put("content",trees);
        map.put("totalElements", menuDtos.size());
        return map;
    }


    /**
     * 返回获取到的菜单树
     * @return
     * @param pageParameter
     */
    public List<Menu> getAll(PageParameter pageParameter) {
        MenuQuery searchKey =(MenuQuery) pageParameter.getSearchKey(MenuQuery.class);
        List<Menu> allMenus = menuDao.findAll((root,criteriaQuery,criteriaBuilder)-> QueryHelp.getPredicate(root,searchKey,criteriaBuilder));
        Map<String, Object> menuMap = buildTree(allMenus);
        List<Menu> menusList = (List<Menu>) menuMap.get("content");
        return menusList;
    }


    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @Transactional
    public Boolean addMenu(Menu menu) {
        Assert.notNull(menu.getPid(),"上级目录ID不能为空");
        menu.setCreateTime(new Date());
        menuDao.save(menu);
        return true;
    }


    /**
     * 根据menu构建前台上级菜单列表所用的菜单树
     * @param menus
     * @return
     */
    public List<Map<String,Object>> getMenuTree(List<Menu> menus) {
        List<Map<String,Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu!=null){
                        List<Menu> menuList = menuDao.findByPid(menu.getId());
                        Map<String,Object> map = new HashMap<>(16);
                        map.put("id",menu.getId());
                        map.put("label",menu.getMenuName());
                        if(menuList!=null && menuList.size()!=0){
                            map.put("children",getMenuTree(menuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }


    /**
     * 获取前台上级菜单列表所用的菜单树
     * @return
     */
    public List<Map<String,Object>> tree() {
        List<Menu> all = menuDao.findAll();
        Map<String, Object> menuMap = buildTree(all);
        List<Menu> menusList = (List<Menu>) menuMap.get("content");
        return  getMenuTree(menusList);
    }


    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @Transactional
    public Boolean updateMenu(Menu menu) {
        Assert.notNull(menu,"参数不能为空！");
        Assert.notNull(menu.getId(),"ID不能为空");
        Assert.notNull(menu.getPid(),"上级目录不能为空");
        Assert.isFalse(menu.getId().equals(menu.getPid()),"上级目录不能是当前菜单");
        Menu findMenu = menuDao.findById(menu.getId()).get();
        Assert.notNull(findMenu,"不存在的菜单项");
        findMenu.setMenuName(menu.getMenuName());
        findMenu.setPermission(menu.getPermission());
        findMenu.setIcon(menu.getIcon());
        findMenu.setEnabled(menu.getEnabled());
        findMenu.setIsMenu(menu.getIsMenu());
        findMenu.setSort(menu.getSort());
        findMenu.setComponent(menu.getComponent());
        findMenu.setComponentName(menu.getComponentName());
        findMenu.setPath(menu.getPath());
        findMenu.setPid(menu.getPid());
        menuDao.save(findMenu);
        return true;
    }


    /**
     * 删除菜单
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean del(Integer id) {
        HashSet set = new HashSet();
        Menu menuNow = menuDao.findById(id).get();
        Assert.notNull(menuNow,"删除菜单不存在");
        List<Menu> byPidMenus = menuDao.findByPid(id);
        HashSet<Menu> delMenus = getDelMenus(byPidMenus, set);
        delMenus.add(menuNow);
        for (Menu menu : delMenus) {
            //删除角色关联
            roleService.untiedMenu(menu.getId());
            //删除菜单
            menuDao.deleteById(menu.getId());
        }
        return true ;
    }

	//删除，递归查找
	public HashSet<Menu> getDelMenus(List<Menu> menuList, HashSet<Menu> menuSet) {
		// 递归找出待删除的菜单
		for (Menu menu : menuList) {
			menuSet.add(menu);
			List<Menu> menus = menuDao.findByPid(menu.getId());
			if(!CollectionUtils.isEmpty(menus)){
				getDelMenus(menus, menuSet);
			}
		}
		return menuSet;
	}



	public List<Integer> roleMenuIdlist(Integer roleid) {
		Set<Integer> roleIds = new HashSet<>();
		roleIds.add(roleid);
		List<Menu> menus = menuDao.findByRoles_IdInAndEnabledOrderBySortAsc(roleIds, true);
		List<Integer> menusIds = new ArrayList<>();
		for (Menu menu : menus) {
            menusIds.add(menu.getId());
		}
		return menusIds;
	}

    /**
     * 返回获取到的菜单树（全部菜单）
     * @return
     * @param
     */
    public List<Menu> getAll( ) {
        List<Menu> allMenus = menuDao.findAll();
        Map<String, Object> menuMap = buildTree(allMenus);
        List<Menu> menusList = (List<Menu>) menuMap.get("content");
        return menusList;
    }
}
