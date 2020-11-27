package io.jingwei.base.utils.model;

import java.util.ArrayList;
import java.util.List;

public class P<T> {

    private List<T> rows = new ArrayList();

    private long total;

    private long pageSize;

    private long pageIndex;

    private long pages;


    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public long getTotal() {
        return this.total;
    }

    public long getPageSize() {
        return this.pageSize;
    }

    public long getPageIndex() {
        return this.pageIndex;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public P(List<T> rows, long total, long pageSize, long pageIndex, long pages) {
        this.rows = rows;
        this.total = total;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.pages = pages;
    }

    public P() {
    }
}
