package com.huawei.util.datastore;

import com.huawei.util.models.Filter;
import dev.morphia.query.Query;

public class QueryWrapper<E> {

    private Query<E> query;

    public QueryWrapper(Query<E> query ) {
        this.query = query;
    }

    public void createFilter(Filter filter) {
        filter.createCriteria(query);

    }

    public Query getQuery() {
        return query;
    }

    public void setLastModified() {
        query.field("last_modified").equal(true);
    }

    public long count() {
        return query.count();
    }
}
