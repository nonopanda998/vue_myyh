package com.myyh.system.controller;


import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.Log;
import com.myyh.system.pojo.vo.PageParameter;
import com.myyh.system.pojo.vo.PageResult;
import com.myyh.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @PreAuthorize("@ry.has('log.list')")
    @LogInfo("分页查询日志")
    @GetMapping("/pagelist")
    public PageResult<Log> list(PageParameter parameter){
        return  new PageResult<Log>(logService.list(parameter));
    }



}
