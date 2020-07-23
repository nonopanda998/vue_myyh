package com.myyh.system.pojo.vo;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import lombok.Data;

/**
 * 分页请求参数
 */
@Data
@JSONType(ignores = "pageable") // 不加fastjson toJson的时候 报 StackOverflowError
public class PageParameter<T> {

    private int currentPage = 1;

    private int pageSize = 5;

    private String sortField = "";

    private String sort = "";

    private T searchKey;

    public PageParameter() {
        super();
    }

    public PageParameter(int currentPage, int pageSize, String sortfield, String sort,T searchKey
                   ) {
        super();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.sortField = sortfield;
        this.sort = sort;
        this.searchKey= searchKey;
    }

    public PageParameter getPageable() {
        return new PageParameter(currentPage, pageSize, sortField, sort,searchKey);
    }

    public Pageable toPageable() {
        // pageable里面是从第0页开始的。
        Pageable pageable = null;
        if (StringUtils.isEmpty(sortField)) {
            pageable =  PageRequest.of(currentPage - 1, pageSize);
        }
        else {
            pageable = PageRequest.of(currentPage - 1, pageSize,
                    sort.toLowerCase().startsWith("desc") ? Direction.DESC
                            : Direction.ASC,
                    sortField);
        }

        return pageable;
    }

    public T getSearchKey(Class classzz){
      T t= (T)JSONObject.parseObject(this.searchKey.toString(),classzz);
        return  t ;
    }

}