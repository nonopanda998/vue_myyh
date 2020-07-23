package com.myyh.system.exception;


public enum ErrorCode {

    NO_LOGIN(-1,"未登录"),
    SUCCESS(0,"成功"),
    FAIL(1,"失败"),
    NO_PERMISSION(2,"无权限");


    private int code;
    private String errMsg;

    ErrorCode(int code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    private void setCode(int code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    private void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
