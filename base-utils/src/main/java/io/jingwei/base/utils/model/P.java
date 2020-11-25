package io.jingwei.base.utils.model;

import java.util.ArrayList;
import java.util.List;

public class P<T> {

    private List<T> rows = new ArrayList();

    private long total;

    private int pageSize;

    private int pageIndex;

    private int pages;


    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public long getTotal() {
        return this.total;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public P(List<T> rows, long total, int pageSize, int pageIndex, int pages) {
        this.rows = rows;
        this.total = total;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.pages = pages;
    }

    public P() {
    }
}
