package com.myyh.system.pojo.vo;


import com.myyh.system.annotation.Query;

import java.util.Date;
import java.util.List;

public class ArticleQuery {

    /**
     * 完全相等
     */
    @Query(type = Query.Type.EQUAL,propName = "isDelete")
    private String isDelete;
}
