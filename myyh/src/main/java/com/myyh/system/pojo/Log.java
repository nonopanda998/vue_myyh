package com.myyh.system.pojo;


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
@Table(name="t_log", indexes = {
        @Index(name = "id", columnList = "id", unique = true)})
public class Log {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(columnDefinition="bigint(20) NOT NULL")
    private  Long  id;

    @Column(name = "user_name",columnDefinition = "varchar(100) default '' comment '用户名'")
    private String username;

    @Column(name = "ip", columnDefinition = "varchar(100) default '' comment 'ip'")
    private String ip;

    @Column(name = "browser",columnDefinition = "varchar(255) DEFAULT NULL   comment '浏览器'")
    private String browser;

    @Column(name = "trade_name",columnDefinition = "varchar(255) DEFAULT NULL   comment '交易名称'")
    private String tradeName;

    @Column(name = "method", columnDefinition = "varchar(255) DEFAULT NULL   comment '请求方法'")
    private String method;

    @Column(name = "params", columnDefinition = "text DEFAULT NULL   comment '请求参数'")
    private String params;

    @Column(name = "result", columnDefinition = "varchar(25) DEFAULT NULL   comment '访问结果：成功、失败'")
    private String result;

    @Column(name = "time", columnDefinition = " bigint(20) DEFAULT NULL comment '请求耗时毫秒'")
    private Long time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "exceptionMsg", columnDefinition = " text DEFAULT NULL comment '错误信息'")
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
