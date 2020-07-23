package com.myyh.system.config;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 *提供token，JWT（JSON WEB TOKEN）跨域身份认证解决方案，JWt-JSon组成：JWT头部、有效载荷、签名三部分
 * jwt头部：{"alg": "HS256","typ": "JWT"}  alg加密算法，最后使用BASE64转化为字符串保存
 * 有效载荷：即传递的数据如下，用户数据保存在其中可自定义字段，JSON对象也使用Base64 URL算法转换为字符串保存。
 *      iss：发行人
 *      exp：到期时间
 *      sub：主题
 *      aud：用户
 *      nbf：在此之前不可用
 *      iat：发布时间
 *      jti：JWT ID用于标识该JWT
 * 签名哈希：是对上面两部分数据签名，通过指定的算法生成哈希，以确保数据不会被篡改。
 *      需要指定密码（secret）保存在服务器，
 *      加密方式：HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload),secret)
 *
 *在计算出签名哈希后，JWT头，有效载荷和签名哈希的三个部分组合成一个字符串，每个部分用"."分隔，就构成整个JWT对象。、
 *
 *
 * 缺點和解决：JWT签发，在有效期内将会一直有效，避免盗用建议设置较短的权限时间并使用HTTPS协议
 */


@Slf4j
@Component
public class TokenProvider  implements InitializingBean {


    private final SecurityProperties securityProperties;
    private static final String AUTHORITIES_KEY = "auth";
    private SecretKey key;

    public TokenProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    //bean初始化完成后属性设置
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(securityProperties.getBase64Secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    //创建token
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + securityProperties.getTokenValidityInSeconds());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    //从token中获取认证的用户信息
    Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    //认证token
    boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            log.info("JWT token 已过期");
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            e.printStackTrace();
        }
        return false;
    }

    //获取token
    public String getToken(HttpServletRequest request){
        final String requestHeader = request.getHeader(securityProperties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(securityProperties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }
}
