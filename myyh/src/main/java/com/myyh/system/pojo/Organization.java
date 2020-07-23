package com.myyh.system.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "t_organization", indexes = {@Index(name = "id", columnList = "id", unique = true),
		@Index(name = "pid_Index", columnList = "pid")
})
public class Organization implements Serializable {

	//IDENTITY：主键由数据库自动生成，即采用数据库ID自增长的方式，Oracle不支持这种方式。
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Column(name = "pid", columnDefinition = "int(11) NOT NULL COMMENT '上级机构ID'")
	private Integer pid;

	@Column(name = "bank_code ", columnDefinition = "varchar(50) DEFAULT NULL COMMENT ' 交易法人银行代码（核心）'")
	private String bankCode  ;

	@Column(name = "org_name", columnDefinition = "varchar(255) DEFAULT NULL COMMENT '机构名称'")
	private String orgName ;

	@Column(name ="enabled",nullable = false,columnDefinition = "int(1) DEFAULT 0 COMMENT '状态1:启用,0:禁用'")
	private Boolean enabled;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "create_time",columnDefinition = "dateTime DEFAULT NULL COMMENT '创建时间'")
	private Date createTime;

	@Transient
	private List<Organization> children;

}
