package com.myyh.system.pojo.vo;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


@Data
public class PageQuery<T, Q> {

    private long currentPage = 1;

    private long pageSize = 5;

    private String sortField = "";

    private String sort = "";

    private T searchKey;

    public T getSearchKey(Class classzz) {
        return (T) JSONObject.parseObject(this.searchKey.toString(),  classzz);
    }

    public Page<Q> getPage() {
        return new Page<Q>(currentPage, pageSize);
    }

    public PageQuery() {
        super();
    }

    public PageQuery(int currentPage, int pageSize, String sortfield, String sort, T searchKey
    ) {
        super();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.sortField = sortfield;
        this.sort = sort;
        this.searchKey = searchKey;
    }
}
