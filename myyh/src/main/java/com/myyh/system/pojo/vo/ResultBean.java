package com.myyh.system.pojo.vo;

import com.alibaba.fastjson.JSONObject;
import com.myyh.system.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int NO_LOGIN = ErrorCode.NO_LOGIN.getCode(); //未登录

    public static final int SUCCESS = ErrorCode.SUCCESS.getCode(); //成功

    public static final int FAIL = ErrorCode.FAIL.getCode(); //失败

    public static final int NO_PERMISSION = ErrorCode.NO_PERMISSION.getCode(); //无权限

    private String msg = "success";

    private int code = SUCCESS;

    private T data;


    public ResultBean() {
        super();
    }
    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", data=" + JSONObject.toJSONString(data) +
                '}';
    }
}