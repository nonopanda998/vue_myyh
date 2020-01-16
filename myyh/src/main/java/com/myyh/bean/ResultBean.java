package com.myyh.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultBean<T> implements Serializable {

    //未登录
    public static final int NO_LOGIN = -1;

    //成功
    public static final int SUCCESS = 0;

    //失败
    public static final int FAIL = 1;

    //无权限
    public static final int NO_PERMISSION = 2;

    private String msg = "success";

    private int code = SUCCESS;

    //数据
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
}
