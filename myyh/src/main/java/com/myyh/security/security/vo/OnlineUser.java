package com.myyh.security.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Zheng Jie
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    private String userName;

    private String browser;

    private String ip;

    private String address;

    private String key;

    private Date loginTime;


}
