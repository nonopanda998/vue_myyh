package com.myyh.system.config;


import com.myyh.system.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("ry")
public class PermissionConfig {
    public Boolean has(String ...permissions){
        // 获取当前用户的所有权限
        List<String> permissionList = SecurityUtils.getUserDetails().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return permissionList.contains("admin") || Arrays.stream(permissions).anyMatch(permissionList::contains);
    }
}
