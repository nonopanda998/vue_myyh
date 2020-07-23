package com.myyh.system.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "t_user_avatar",
        indexes = {@Index(name = "id", columnList = "id", unique = true)})
public class UserAvatar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "real_name",columnDefinition = "varchar(255) DEFAULT NULL COMMENT '真实文件名'")
    private String realName;

    @Column(name = "path", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '路径'")
    private String path;

    @Column(name = "size", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '大小'")
    private String size;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}