package com.myyh.system.dao;

import com.myyh.system.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RoleDao extends JpaRepository<Role,Integer>, JpaSpecificationExecutor<Role> {


    Set<Role> findByUsers_Id(Integer id);

    @Query(value = "select id,name,create_time,remark from t_role as r where r.name in ?1",nativeQuery = true)
    List<Role> findByRoles(List<String> roles);

    /**
     * 解绑角色菜单
     * @param id 菜单ID
     */
    @Modifying   //删除或更新需要加上
    @Query(value = "delete from t_roles_menus where menu_id = ?1",nativeQuery = true)
    void untiedMenu(Integer id);


    Role findByName(String name);

    @Modifying
    @Query(value = "delete from t_roles_menus where role_id = ?1",nativeQuery = true)
    void deleteRolesMenusByRoleId(Integer roleId);

    /**
     * 手动保存角色菜单对应关系
     * @param roleId
     * @param menuId
     */
    @Modifying
    @Query(value = "insert into t_roles_menus(role_id,menu_id) values(?1,?2)",nativeQuery = true)
    void saveRolesMenus(Integer roleId, Integer menuId);

    /**
     * 根据角色id 删除用户角色关联
     * @param id
     */
    @Modifying
    @Query(value = "delete from t_users_roles where role_id = ?1",nativeQuery = true)
    void deleteUsersRolesByRoleId(Integer id);


    /**
     * 保存关联的用户角色关联
     * @param roleId,userId
     */
    @Modifying
    @Query(value = "insert into t_users_roles (role_id,user_id) values(?1,?2)",nativeQuery = true)
    void saveUsersAndRoles(Integer roleId, Integer userId);
}
