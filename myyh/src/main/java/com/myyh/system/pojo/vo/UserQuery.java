package com.myyh.system.pojo.vo;

import com.myyh.system.annotation.Query;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserQuery {


    /**
     * 完全相等
     */
    @Query(type = Query.Type.EQUAL,propName = "oid")
    private Integer oid;

    /**
     * 模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE,propName = "username")
    private String username;

    /**
     * 时间，起始-结束
     */
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
}
