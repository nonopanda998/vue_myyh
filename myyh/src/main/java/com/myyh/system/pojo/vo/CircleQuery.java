package com.myyh.system.pojo.vo;

import com.myyh.system.annotation.Query;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CircleQuery {
	/**
	 * 模糊查询
	 */
	@Query(type = Query.Type.INNER_LIKE,propName = "name")
	private String name;

	/**
	 * 创建时间，起始-结束
	 */
	@Query(type = Query.Type.BETWEEN)
	private List<Date> createTime;

}
