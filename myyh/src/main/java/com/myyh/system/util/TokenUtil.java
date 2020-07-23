package com.myyh.system.util;

import com.myyh.system.config.SecurityProperties;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String getToken(HttpServletRequest request) {
        SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
