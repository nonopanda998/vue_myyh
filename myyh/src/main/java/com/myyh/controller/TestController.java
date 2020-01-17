package com.myyh.controller;

import com.myyh.annotation.AnonymousAccess;
import com.myyh.bean.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller只做参数格式的转换,不允许把json，map这类对象传到services去，也不允许services返回json、map
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @AnonymousAccess
    public ResultBean login() {
        return new ResultBean();
    }
}