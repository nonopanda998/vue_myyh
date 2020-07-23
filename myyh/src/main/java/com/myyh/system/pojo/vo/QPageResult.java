package com.myyh.system.pojo.vo;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class QPageResult<T> extends ResultBean {

    //当前页
    private Long currentPage;

    //每页个数
    private Long pagesize;

    //总页数
    private Long pageTotal;

    //总条数
    private Long total;

    public QPageResult(IPage ipage) {
        Page page  =(Page)ipage;
        this.setData(page.getRecords());
        this.currentPage = page.getCurrent();
        this.pagesize = page.getSize();
        this.pageTotal = page.getPages();
        this.total = page.getTotal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QPageResult<?> that = (QPageResult<?>) o;
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