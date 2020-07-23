package com.myyh.system.pojo.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 */
@Data
public class UserPassVo {
    private Integer id;

    private String oldPass;

    private String newPass;
}
