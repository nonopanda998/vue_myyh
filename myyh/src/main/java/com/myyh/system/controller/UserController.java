package com.myyh.system.controller;

import com.myyh.system.annotation.Anonymous;
import com.myyh.system.annotation.LogInfo;
import com.myyh.system.pojo.vo.*;
import com.myyh.system.pojo.User;
import com.myyh.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Anonymous
    @LogInfo("登录")
    @PostMapping(value = "/login")
    public ResultBean login(@RequestBody @Validated User loginUser, HttpServletRequest request){
        return new ResultBean<>(userService.login(loginUser,request));
    }

    @Anonymous
    @LogInfo("获取验证码")
    @GetMapping(value = "/code")
    public ResultBean<Object> getCode(){
        return new ResultBean<>(userService.getCode());
    }


    @PreAuthorize("@ry.has('user.list')")
    @LogInfo("分页查询用户")
    @GetMapping("/list")
    public PageResult list(PageParameter pageRequest){
        return new PageResult<User>(userService.findAll(pageRequest));
    }


    @Anonymous
    @LogInfo("查询认证用户")
    @GetMapping("/info")
    public ResultBean info(){
        return new ResultBean<JwtUser>(userService.getJwtUser());
    }


    @PreAuthorize("@ry.has('user.update')")
    @LogInfo("修改用户")
    @PostMapping(value = "/update")
    public ResultBean update(@RequestBody User user){
        return new ResultBean<>(userService.update(user));
    }


    @PreAuthorize("@ry.has('user.add')")
    @LogInfo("添加用户")
    @PostMapping(value = "/add")
    public ResultBean add(@RequestBody User user){
        return new ResultBean<>(userService.save(user));
    }


    @PreAuthorize("@ry.has('user.delete')")
    @LogInfo("删除用户")
    @DeleteMapping(value = "/del/{id}")
    public ResultBean delete(@PathVariable Integer id){
        return new ResultBean<>(userService.delete(id));
    }


    @Anonymous
    @LogInfo("退出登录")
    @DeleteMapping(value = "/logout")
    public ResultBean logout(HttpServletRequest request){
        return new ResultBean<>(userService.logout(request));
    }



    @LogInfo("首页数据")
    @PostMapping(value = "/dashboardDate")
    public ResultBean dashboardDate(HttpServletRequest request){
        return new ResultBean<>(userService.dashboardDate(request));
    }

    @LogInfo("个人资料修改")
    @PutMapping(value = "/center")
    public ResultBean center(@RequestBody User user,HttpServletRequest request){
        return new ResultBean<>(userService.center(user,request));
    }

    @LogInfo("个人密码修改")
    @PostMapping(value = "/changPassword")
    public ResultBean changPassword(@RequestBody UserPassVo passVo){
        return new ResultBean<>(userService.updatePass(passVo));
    }

    @LogInfo("重置密码")
    @PreAuthorize("@ry.has('user.resetPassword')")
    @PostMapping(value = "/resetPassword")
    public ResultBean resetPassword(@RequestBody UserPassVo passVo){
        return new ResultBean<>(userService.resetPassword(passVo.getNewPass(),passVo.getId()));
    }
}
