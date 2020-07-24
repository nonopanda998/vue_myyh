package com.myyh.system.pojo.vo;


import com.myyh.system.annotation.Query;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LogQuery {

    /**
     * 模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE,propName = "user_name")
    private String username;

    /**
     * 完全相等
     */
    @Query(type = Query.Type.EQUAL,propName = "result")
    private String result;

    /**
     * 模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE,propName = "trade_name")
    private String tradeName;

    /**
     * 时间，起始-结束
     */
    @Query(type = Query.Type.BETWEEN,propName = "create_time")
    private List<Date> createTime;
}
