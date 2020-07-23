package com.myyh.system.pojo.vo;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Getter
@Setter
public class PageResult<T> extends ResultBean {

    //当前页
    private int currentPage;

    //每页个数
    private int pagesize;

    //总页数
    private Integer pageTotal;

    //总条数
    private Long total;

    public PageResult(Page<T> page) {
        this.setData(page.getContent());
        this.currentPage = page.getNumber() + 1;
        this.pagesize = page.getSize();
        this.pageTotal = page.getTotalPages();
        this.total = page.getTotalElements();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PageResult<?> that = (PageResult<?>) o;
        return currentPage == that.currentPage &&
                pagesize == that.pagesize &&
                Objects.equals(pageTotal, that.pageTotal) &&
                Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), currentPage, pagesize, pageTotal, total);
    }
}