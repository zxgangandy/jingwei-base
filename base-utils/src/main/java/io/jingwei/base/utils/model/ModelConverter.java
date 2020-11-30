package io.jingwei.base.utils.model;


import java.util.List;

public class ModelConverter {
    public static  <T> P<T> page(long pageIndex, long pageSize, long pages, List<T> list ) {
        P result = new P<>();
        result.setPageIndex(pageIndex);
        result.setPageSize(pageSize);
        result.setPages(pages);
        result.setRows(list);
        return result;
    }
}
