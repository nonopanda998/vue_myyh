package com.myyh.system.aop;

import com.myyh.system.annotation.LogInfo;
import com.myyh.system.exception.BadRequestException;
import com.myyh.system.pojo.Log;
import com.myyh.system.pojo.vo.ResultBean;
import com.myyh.system.service.LogService;
import com.myyh.system.util.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 处理日志和包装异常
 */
@Aspect
@Slf4j
@Component
public class ControllerAop {

    @Autowired
    private LogService logService;

    private ThreadLocal<Long> currentTime = new ThreadLocal<>();


    //切点，访问所有controller方法都会被记录
    @Pointcut("execution(public * com.myyh.system.controller.*.*(..))")
    public void controllerPointcut() {
    }


    @Around("controllerPointcut()")
    public Object handlerControllerAround(ProceedingJoinPoint point) {
        currentTime.set(System.currentTimeMillis());
        Object result = null;
        String methodName = null;
        String className = null;
        String user = null;
        String annotationLog = null;
        List<Object> args = null;
        try {
            //方法名
            methodName = point.getSignature().getName();
            className = point.getTarget().getClass().getName()+"."+methodName;
            //注解内容
            annotationLog = getAnnotation(point);
            //前置通知
            final Object[] args1 = point.getArgs();
            if (!ObjectUtils.isEmpty(args1)) {
                args = Arrays.asList(point.getArgs());
            }
            user = UserUtil.getUser();
            log.info("================start=================");
            log.info(user + "访问:【" + annotationLog + "】 执行参数<" + (args == null ? MyStringUtils.NULL : args) + ">");
            //执行目标方法
            result = (ResultBean<?>) point.proceed();
            //返回通知
            log.info(user + "访问:【" + annotationLog + "】 返回结果: <" + result + ">");
        } catch (Throwable e) {
            //异常通知
            log.error("》》》》》》》》》》" + user + "访问:【" + annotationLog + "】失败！.异常信息:<" + e + ">");
            return handlerException(point, e, annotationLog,className, user, args);
        }
        //后置通知
        log.info(user + "访问:【" + annotationLog + "】 方法成功.用时："
                + (System.currentTimeMillis() - currentTime.get()) + "毫秒");

        //保存日志
        saveLog(null, className, annotationLog ,user, args);
        currentTime.remove();
        log.info("=================end==================");
        return result;
    }


    /**
     * 获取注解信息
     *
     * @param point
     * @throws NoSuchMethodException
     */
    private String getAnnotation(ProceedingJoinPoint point) throws NoSuchMethodException {
        //获取注解信息
        Class<?> aClass = point.getTarget().getClass();
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method declaredMethod = aClass.getDeclaredMethod(ms.getMethod().getName(), ms.getParameterTypes());
        LogInfo annotation = declaredMethod.getAnnotation(LogInfo.class);
        return annotation.value();
    }


    /**
     * 封装异常信息，区分已知异常（自己抛出的）和未知异常
     */
    private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e, String annotationLog, String methodName, String user, List<Object> args) {
        ResultBean<?> result = new ResultBean();
        // 已知异常，参数异常
        if (e instanceof IllegalArgumentException) {
            result.setMsg(e.getMessage());
            result.setCode(ResultBean.FAIL);
            //请求异常
        } else if (e instanceof BadRequestException) {
            result.setMsg(e.getMessage());
            result.setCode(ResultBean.FAIL);
            //认证异常处理
        } else if (e instanceof AuthenticationException) {
            result.setMsg(e.getMessage());
            result.setCode(ResultBean.FAIL);
        } else {
            log.error(pjp.getSignature() + " error ", e);
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg("未知错误，请联系管理员！");
            result.setCode(ResultBean.FAIL);
        }
        //保存日志
        saveLog(e, methodName,annotationLog, user, args);
        currentTime.remove();
        return result;
    }


    /**
     * 保存访问日志信息
     * @param e
     * @param methodName
     * @param user
     * @param args
     */
    private void saveLog(Throwable e, String methodName,String tradename, String user, List<Object> args) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        Log log = new Log();
        log.setCreateTime(new Date());
        String browser = MyStringUtils.getBrowser(request);
        String ip = MyStringUtils.getIp(request);
        Long time = System.currentTimeMillis() - currentTime.get();
        log.setBrowser(browser);
        log.setIp(ip);
        log.setMethod(methodName);
        log.setUsername(user);
        log.setParams(args == null ? MyStringUtils.NULL : args.toString());
        if (ObjectUtils.isEmpty(e)) {
            log.setResult(MyStringUtils.SUCCESS);
        } else {
            log.setResult(MyStringUtils.FAIL);
            log.setExceptionMsg(e.getMessage());
        }
        log.setTradeName(tradename);
        log.setTime(time);
        logService.save(log);
    }

}
