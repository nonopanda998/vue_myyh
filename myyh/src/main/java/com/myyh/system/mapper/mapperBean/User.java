package com.myyh.system.mapper.mapperBean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_user")
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id ;

    private String userName;

    private String password;

    private Date createTime;

    private Date lastLoginTime;

    private Date updateTime;

    private Boolean enabled;

    private String lastLoginIp;

    private Integer oid ;

    private String nickName;

    private String email;

    private String phone;

}
