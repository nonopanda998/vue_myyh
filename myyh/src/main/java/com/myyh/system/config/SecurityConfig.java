package com.myyh.system.config;

import com.myyh.system.annotation.Anonymous;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 安全配置类
 */

@Configuration
//核心注解，可以不添加2.0自动开启，注入WebSecurityConfiguration｛
// 1.springSecurityFilterChain.class  核心过滤器请求的认证入口
// 2.@EnableGlobalAuthentication  开启AuthenticationConfiguration.class
//    配置认证相关的核心类向spring中注入AuthenticationManagerBuilder构造AuthenticationManager｝
//@EnableWebSecurity
/**开启 Spring Security 方法级安全注解
 *prePostEnabled :决定Spring Security的前注解是否可用 [@PreAuthorize,@PostAuthorize,..]
 *secureEnabled : 决定是否Spring Security的保障注解 [@Secured] 是否可用,@Secured对应的角色必须要有ROLE_前缀
 *jsr250Enabled ：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用.开启次注解applicationcontext将不能被注入
 *
 * */
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final ApplicationContext applicationContext;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(TokenProvider tokenProvider,
                          CorsFilter corsFilter,
                          ApplicationContext applicationContext,
                          JwtAuthenticationEntryPoint authenticationErrorHandler,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.applicationContext = applicationContext;
        this.authenticationErrorHandler = authenticationErrorHandler;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(tokenProvider);
    }


    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //匿名访问 url： @Anonymous
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            Anonymous anonymousAccess = handlerMethod.getMethodAnnotation(Anonymous.class);
            if (null != anonymousAccess) {
                anonymousUrls.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        httpSecurity
                // 禁用 CSRF
                .csrf().disable()
                // 先注册跨域、用户密码认证Filter
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理自动，可使用ExceptionHandlingConfigurer自定义
                .exceptionHandling()
                // 认证入口默认AuthenticationEntryPoint
                .authenticationEntryPoint(authenticationErrorHandler)
                // 拒绝访问处理(已登录权限不足时)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable()

                // 不创建会话
                //        always：保存session状态（每次会话都保存，可能会导致内存溢出）
                //        never：不会创建HttpSession，但是会使用已经存在的HttpSession
                //        if_required：仅在需要HttpSession创建
                //        stateless：不会保存session状态
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                //.antMatchers("/user").hasRole("ADMIN")  // user接口只有ADMIN角色的可以访问
                //.anyRequest()
                //.authenticated()// 任何尚未匹配的URL只需要验证用户即可访问
                // 静态资源等等
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll()
                // swagger 文档
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/*/api-docs").permitAll()
                // 文件
                .antMatchers("/avatar/**").permitAll()
                .antMatchers("/file/**").permitAll()
                // 阿里巴巴 druid
                .antMatchers("/druid/**").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 自定义匿名访问所有url放行 ： 允许匿名和带权限以及登录用户访问
                .antMatchers(anonymousUrls.toArray(new String[0])).permitAll()
                // 所有请求都需要认证
                .anyRequest().authenticated()
                .and().apply(securityConfigurerAdapter());
    }


}
