package com.myyh;

import com.myyh.system.util.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.myyh.system.mapper*")
@EnableTransactionManagement
public class RyApplication {


    public static void main(String[] args) {
        SpringApplication.run(RyApplication.class, args);
    }


    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }


    /**
     * 请求参数转译支持，支持请求中的特殊符号或用base64编码
     * @return
     */
    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }

}
