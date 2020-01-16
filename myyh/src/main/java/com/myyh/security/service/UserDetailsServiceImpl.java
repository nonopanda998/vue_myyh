package com.myyh.security.service;

import com.myyh.bean.User;
import com.myyh.exception.BadRequestException;
import com.myyh.security.security.vo.JwtUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 核心认证类
 */
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userService.findByName(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new BadRequestException("账号不存在");
        } else {
            if (user.getStatus() == 0) {
                throw new BadRequestException("账号未激活");
            }
            return createJwtUser(user);
        }
    }

    private UserDetails createJwtUser(User user) {
        return new JwtUser(
                user.getAvatar(),
                user.getAccount(),
                user.getPassword(),
                user.getSalt(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getEmail(),
                user.getPhone(),
                user.getRoleid(),
                user.getDeptid(),
                user.getStatus(),
                user.getVersion()
                );
    }
}
