package com.myyh.system.config;

import com.myyh.system.pojo.vo.OnlineUser;
import com.myyh.system.util.CacheManagerUtil;
import com.myyh.system.util.SpringContextHolder;
import com.myyh.system.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class TokenFilter extends GenericFilterBean {


    private final TokenProvider tokenProvider;

    public TokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //验证token 和权限的逻辑
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //获取token
        String token = TokenUtil.getToken(request);
        String requestRri = request.getRequestURI();
        // 验证 token 是否存在
        OnlineUser onlineUser = null;
        try {
            //从缓存中获取在线用户
             CacheManagerUtil cacheManagerUtil = SpringContextHolder.getBean(CacheManagerUtil.class);
             onlineUser = (OnlineUser) cacheManagerUtil.get("ONLINEUSER",token);
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
        }
        if (onlineUser != null && StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
        	//存在在线用户
        	Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security token 认证成功请求用户名： '{}', uri: {}", authentication.getName(), requestRri);
        } else {
            log.debug("JWT token 无效或者不存在！, uri: {}", requestRri);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
