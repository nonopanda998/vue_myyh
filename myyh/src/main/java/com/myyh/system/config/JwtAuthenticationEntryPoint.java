package com.myyh.system.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt身份验证入口点,解决匿名用户访问无权限资源时的异常
 */

@Component
public class JwtAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException error) throws IOException, ServletException {
        // 当用户无凭证访问REST资源，将调用此方法响应代码401
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, error ==null?"Unauthorized":error.getMessage());
//        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, error ==null?"没有权限！请联系管理员":error.getMessage());
//        httpServletResponse.setContentType("application/json;charset=UTF-8");
//        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        PrintWriter out = httpServletResponse.getWriter();
//        out.write("{msg:\"没有权限！请联系管理员\",code:\""+ ErrorCode.FAIL.getCode()+"\"}");
//        out.flush();
//        out.close();
    }
}
