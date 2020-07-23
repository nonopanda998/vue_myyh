package com.myyh.system.dao;

import com.myyh.system.pojo.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface MenuDao extends JpaRepository<Menu,Integer>, JpaSpecificationExecutor<Menu> {

    /**
     * 根据角色ID与是否启用查询菜单
     * @param roleIds roleIDs
     * @param isEnabled
     * @return /
     */
    List<Menu> findByRoles_IdInAndEnabledAndIsMenuOrderBySortAsc(Set<Integer> roleIds, boolean isEnabled,boolean isMenu);



    List<Menu> findByPid(Integer id);

    /**
     * 根据角色ID查询启用菜单
     * @param roleIds
     * @param b
     * @return
     */
    List<Menu> findByRoles_IdInAndEnabledOrderBySortAsc(Set<Integer> roleIds, boolean b);
}
