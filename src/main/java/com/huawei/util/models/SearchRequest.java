package com.huawei.util.models;

import java.util.List;

public class SearchRequest {
    private int page;
    private int limit;
    private List<SortField> sorts;
    private List<Filter> filters;
    private String requestRealm;

    public SearchRequest() {
    }

    public SearchRequest(int page, int limit, List<Filter> filters, String requestRealm) {
        this.page = page;
        this.limit = limit;
        this.filters = filters;
        this.requestRealm = requestRealm;
    }

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

    public String getRequestRealm() {
        return requestRealm;
    }

    public void setRequestRealm(String requestRealm) {
        this.requestRealm = requestRealm;
    }
}
