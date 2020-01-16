package com.myyh.bean;

import lombok.Data;
import org.hibernate.annotations.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_user")
@Table(appliesTo = "t_sys_user",comment = "账号")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String avatar;
    @Column
    private String account;
    @Column
    private String password;
    @Column
    private String salt;
    @Column
    private String name;
    @Column
    private Date birthday;
    @Column
    private Integer sex;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String roleid;
    @Column
    private Long deptid;
    @Column
    private Integer status;
    @Column
    private Integer version;
}
