package com.myyh.system.aop;

import com.myyh.system.util.MyStringUtils;
import com.myyh.system.util.SecurityUtils;
import com.myyh.system.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将用户名放入日志中
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String username = null;
        //获取登录的用户信息
        try {
             username = SecurityUtils.getUsername();
        } catch (Exception e){
            log.info("MDC未获取到用户名！");
        }
        if (username != null) {
            UserUtil.setUser(username);
        }else{
            UserUtil.setUser(MyStringUtils.UN_LOGINUSER);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        UserUtil.clearAllUserInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
