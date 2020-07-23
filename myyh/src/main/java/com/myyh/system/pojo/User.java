package com.myyh.system.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity      // @Entity: 实体类, 必须
// @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
@Table(name="t_user", indexes = {
        @Index(name = "id", columnList = "id", unique = true),
        @Index(name = "username", columnList = "user_name", unique = true)
        })
public class User implements Serializable {
    //–AUTO： 主键由程序控制，是默认选项，不设置即此项。
    //–IDENTITY：主键由数据库自动生成，即采用数据库ID自增长的方式，Oracle不支持这种方式。
    //–SEQUENCE：通过数据库的序列产生主键，通过@SequenceGenerator 注解指定序列名，mysql不支持这种方式。
    //–TABLE：通过特定的数据库表产生主键，使用该策略可以使应用更易于数据库移植。
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Integer id ;

    // @Column： 对应数据库列名,可选, nullable 是否可以为空, 默认true
    @NotBlank(message="用户名不能为空")
    @Column(name = "user_name", nullable = false,columnDefinition = "varchar(100) default '' comment '用户名'")
    private String username;

    @NotBlank(message="密码不能为空")
    @Column(name = "password", nullable = false,columnDefinition = "varchar(255) default '' comment '密码'")
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private Date updateTime;

    @ManyToMany
    @JoinTable(name = "t_users_roles", joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private List<Role> roles;

    @Column(name ="enabled",nullable = false,columnDefinition = "bit(1) DEFAULT 0 COMMENT '状态1:启用,0:禁用'")
    private Boolean enabled;


    @Column(name = "last_login_ip",columnDefinition = "varchar(100) default '' comment '上次登录ip'")
    private String lastLoginIp;

    @Column(name = "oid", columnDefinition = "int(11) NOT NULL COMMENT '机构ID'")
    private Integer oid ;

    @Column(name = "nick_name",columnDefinition = "varchar(100) default '' comment '昵称'")
    private String nickName;

    @Column(name = "email",columnDefinition = "varchar(50) default '' comment '邮箱'")
    private String email;

    @Column(name = "phone", columnDefinition = "varchar(50) default '' comment '手机'")
    private String phone;


    @Transient //标记非数据库字段属性，不做数据库映射
    private String code;
    @Transient
    private String uuid;
    @Transient
    private String  key;
    @Transient

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + "****" + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", updateTime=" + updateTime +
                ", roles=" + "****" +
                ", enabled=" + "****" +
                ", code='" + "****" + '\'' +
                ", uuid='" + "****" + '\'' +
                ", key='" + "****" + '\'' +
                '}';
    }
}
