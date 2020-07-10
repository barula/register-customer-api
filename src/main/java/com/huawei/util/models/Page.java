package com.huawei.util.models;

import java.util.List;

public class Page<T> {

    private int page;
    private int limit;
    private int size;
    private List<T> rows;
    private List<SortField> sorts;
    private List<Filter> filters;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<SortField> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortField> sorts) {
        this.sorts = sorts;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
