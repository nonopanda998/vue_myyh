package com.myyh.system.service;

import cn.hutool.core.lang.Assert;
import com.myyh.system.dao.RoleDao;
import com.myyh.system.exception.BadRequestException;
import com.myyh.system.pojo.Menu;
import com.myyh.system.pojo.Role;
import com.myyh.system.pojo.User;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.RoleMenuParameter;
import com.myyh.system.util.QueryHelp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Object mapToGrantedAuthorities(User user) {
        Set<Role> roles = roleDao.findByUsers_Id(user.getId());
        Set<String> permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getName())).map(Role::getName).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(Menu::getPermission).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据用户id查询角色集合
     * @param id
     * @return
     */
    public List<Role> findByUserId(Integer id) {
        return new ArrayList<>(roleDao.findByUsers_Id(id));
    }


    /**
     * 查询所有角色集合
     * @return
     */
    public List<Role> findAll() {
       return roleDao.findAll();
    }

	/**
	 * 分页查询
	 * @param pageRequest
	 * @return
	 */
	public Page<Role> findByPage(PageParameter pageRequest){
		Specification<Role> spec = new Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = QueryHelp.getPredicate(root, null, cb);
				return predicate;
			}
		};
		Pageable pageable = pageRequest.toPageable();
		return roleDao.findAll(spec, pageable);
	}


	/**
     * 新增角色
     * @param role
     * @return
     */
	@Transactional(rollbackFor = Exception.class)
    public boolean save(Role role) {
        Role dbRole = roleDao.findByName(role.getName());
        Assert.isNull(dbRole,"用户已存在！");
		role.setCreateTime(new Date());
        roleDao.save(role);
        return true;
    }

	/**
	 *更新角色
	 * @param role
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean update(Role role){
		//角色名称修改了
		Role dbRole = roleDao.findByName(role.getName());
		if(dbRole!=null && !dbRole.getId().equals(role.getId())){
			//dbRole不是空就抛异常
			Assert.isNull(dbRole,"角色名已存在！");
		}
        Role oldRole = roleDao.getOne(role.getId());
		oldRole.setName(role.getName());
		oldRole.setRemark(role.getRemark());
        roleDao.save(oldRole);
        return true;
    };

	/**
	 * 根据id删除角色
	 * @param id
	 * @return
	 */
	public boolean delete(Integer id){
		try {
			roleDao.deleteById(id);
		} catch (Exception e){
			throw new BadRequestException("所选角色存在用户关联，请取消关联后重试!");
		}
		return true;
	}


	/**
	 * 删除角色关联菜单
	 * @param id
	 */
	@Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Integer id) {
    	roleDao.untiedMenu(id);
	}

	/**
	 * 保存角色菜单
	 * @param roleMenuParameter 角色菜单id对应关系
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean saveRolePermissons(RoleMenuParameter roleMenuParameter) {
		// 删除该角色所有的权限
		Integer roleId = roleMenuParameter.getRoleId();
		roleDao.deleteRolesMenusByRoleId(roleId);
		// 添加新的权限
		for(Integer menuId:roleMenuParameter.getMenusids()){
			roleDao.saveRolesMenus(roleId, menuId);
		}
		return true;
	}

	/**
	 * 查询角色集合
	 * @param names
	 * @return
	 */
	public List<Role> findByRoles(List<String> names) {
	return roleDao.findByRoles(names);
	}

}
