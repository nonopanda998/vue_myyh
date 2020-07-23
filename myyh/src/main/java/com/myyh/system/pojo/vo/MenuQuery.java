package com.myyh.system.pojo.vo;


import com.myyh.system.annotation.Query;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuQuery {

    /**
     * 模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE,propName = "menuName")
    private String menuName;

    /**
     * 模糊查询
     */
    @Query(type = Query.Type.INNER_LIKE,propName = "component")
    private String component;



    /**
     * 创建时间，起始-结束
     */
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;

}
