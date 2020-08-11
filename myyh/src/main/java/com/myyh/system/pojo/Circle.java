package com.myyh.system.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "t_circle", indexes = {@Index(name = "id", columnList = "id", unique = true),
		@Index(name = "pid_Index", columnList = "pid")
})
public class Circle implements Serializable {

	//IDENTITY：主键由数据库自动生成，即采用数据库ID自增长的方式，Oracle不支持这种方式。
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Column(name = "pid", columnDefinition = "int(11) NOT NULL COMMENT '上级圈子ID'")
	private Integer pid;


	@Column(name = "name", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '圈子名称'")
	private String name ;

	@Column(name ="enabled",nullable = false,columnDefinition = "int(1) DEFAULT 0 COMMENT '状态1:启用,0:禁用'")
	private Boolean enabled;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "create_time",columnDefinition = "dateTime DEFAULT NULL COMMENT '创建时间'")
	private Date createTime;

	@Transient
	private List<Circle> children;

}
