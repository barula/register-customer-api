package com.huawei.util.datastore;


import com.google.common.collect.Lists;
import com.huawei.util.models.SearchRequest;
import com.mongodb.AggregationOptions;
import dev.morphia.Datastore;
import dev.morphia.aggregation.AggregationPipeline;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearcheableDatastoreAccess<E> implements PageableDA<E> {

    private Class<E> entity;
    private Datastore datastore;
    private boolean hasLastModifiedField;

    public SearcheableDatastoreAccess(Class<E> entity, Datastore datastore, boolean hasLastModifiedField) {
        this.entity = entity;
        this.datastore = datastore;
        this.hasLastModifiedField = hasLastModifiedField;
    }

    public List<E> search(SearchRequest searchRequest) {
        List<E> result;
        if (count(searchRequest) > 0) {
            result = findInDatabase(searchRequest);
        } else {
            result = new ArrayList<>();
        }
        return result;
    }

    private List<E> findInDatabase(SearchRequest searchRequest) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>(datastore.createQuery(entity));
        QueryBuilder.addFilters(queryWrapper, searchRequest.getFilters());
        if (hasLastModifiedField) {
            queryWrapper.setLastModified();
        }

        AggregationPipeline pipeline = datastore.createAggregation(entity);
        pipeline.match(queryWrapper.getQuery());

        QueryBuilder.addSort(pipeline, searchRequest.getSorts());

        AggregationOptions options = AggregationOptions.builder()
                .allowDiskUse(true)
                .build();

        Iterator<E> results = pipeline.
                skip(searchRequest.getLimit() * (searchRequest.getPage() - 1)).
                limit(searchRequest.getLimit()).
                aggregate(entity, options);

        if (results == null || !results.hasNext()) {
            return new ArrayList<>();
        }
        return Lists.newArrayList(results);
    }

    public int count(SearchRequest searchRequest) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>(datastore.createQuery(entity));

        QueryBuilder.addFilters(queryWrapper, searchRequest.getFilters());
        if (hasLastModifiedField) {
            queryWrapper.setLastModified();
        }

        return Math.toIntExact(queryWrapper.count());
    }

}
