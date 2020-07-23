package com.myyh.system.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@Entity
@Table(name = "t_menu", indexes = {@Index(name = "id", columnList = "id", unique = true),
        @Index(name = "pid_Index", columnList = "pid")
})
public class Menu implements Serializable {
    //IDENTITY：主键由数据库自动生成，即采用数据库ID自增长的方式，Oracle不支持这种方式。
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "pid", columnDefinition = "bigint(20) NOT NULL COMMENT '上级菜单ID'")
    private Integer pid;

    @Column(name = "menu_name", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '菜单名称'")
    private String menuName;

    @Column(name = "path", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '链接地址'")
    private String path;

    @Column(name = "component", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '组件'")
    private String component;

    @Column(name = "component_name", columnDefinition = " varchar(20) DEFAULT '-' COMMENT '组件名称'")
    private String componentName;

    @Column(name = "sort", columnDefinition = "int(11) DEFAULT NULL COMMENT '排序'")
    private Integer sort;

    @Column(name = "is_menu", columnDefinition = "bit(1) DEFAULT NULL COMMENT '是否菜单 （0不是，1是）'")
    private Boolean isMenu;

    @Column(name = "icon", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '图标'")
    private String icon;

    @Column(name = "hidden", columnDefinition = "bit(1) DEFAULT b'0' COMMENT '隐藏'")
    private Boolean hidden;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time",columnDefinition = "dateTime DEFAULT NULL COMMENT '创建时间'")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time",columnDefinition = "dateTime DEFAULT NULL COMMENT '更新时间'")
    private Date updateTime;

    @Column(name = "enabled", columnDefinition = "bit(1) DEFAULT NULL COMMENT '状态0:启用，1:禁用'")
    private Boolean enabled;

    @Column(name="permission")
    private String permission;

    @ManyToMany(mappedBy = "menus")
    @JsonIgnore
    private Set<Role> roles;

    @Transient
    private List<Menu> children;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
