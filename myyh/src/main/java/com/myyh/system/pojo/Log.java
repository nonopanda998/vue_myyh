package com.myyh.system.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@TableName("t_log")
@Table(name="t_log", indexes = {
        @Index(name = "id", columnList = "id", unique = true)})
public class Log {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @TableId(type = IdType.AUTO)
    private  Long  id;

    @TableField("user_name")
    private String username;

    @TableField("ip")
    private String ip;

    @TableField("browser")
    private String browser;

    @TableField("trade_name")
    private String tradeName;

    @TableField("method")
    private String method;

    @TableField("params")
    private String params;

    @TableField("result")
    private String result;

    @TableField("time")
    private Long time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

    @TableField("exception_msg")
    private String exceptionMsg;

    public Log(String username, String ip, String browser, String tradeName, String method, String params, String result, Long time, Date createTime, String exceptionMsg) {
        this.username = username;
        this.ip = ip;
        this.browser = browser;
        this.tradeName = tradeName;
        this.method = method;
        this.params = params;
        this.result = result;
        this.time = time;
        this.createTime = createTime;
        this.exceptionMsg = exceptionMsg;
    }

    public Log() {
    }
}
