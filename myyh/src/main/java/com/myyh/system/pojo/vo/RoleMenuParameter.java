package com.myyh.system.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * 角色授权请求参数
 */
@Data
public class RoleMenuParameter {
	// 角色id
	private Integer roleId;
	// 对应菜单id
	private List<Integer> menusids;
}
