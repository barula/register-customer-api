package com.huawei.util.datastore;

import com.google.common.base.CaseFormat;
import com.huawei.util.enums.Operation;
import com.huawei.util.enums.OrderType;
import com.huawei.util.models.Filter;
import com.huawei.util.models.SortField;
import dev.morphia.aggregation.AggregationPipeline;
import dev.morphia.query.Query;
import dev.morphia.query.Sort;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class QueryBuilder {

    public static void addFilters(QueryWrapper query, List<Filter> filters) {
        if (filters != null) {
            filters.forEach(query::createFilter);
        }
    }

    public static void addFilters(Query query, Filter... filters) {
        if (filters != null) {
            addFilters(new QueryWrapper(query), Arrays.asList(filters));
        }
    }

    public static void addSort(AggregationPipeline pipeline, List<SortField> sorts) {
        sorts.forEach(sort -> pipeline.sort(toQuerySort(sort)));
    }

    private static Sort toQuerySort(SortField sort) {
        switch (sort.getOrderType()) {
            case ASCENDING:
                return Sort.ascending(sort.getField());
            case DESCENDING:
                return Sort.descending(sort.getField());
            default:
                return null;
        }
    }

    public static boolean filter(Object obj, Filter[] filters) {
        for (Filter filter : filters) {
            String fieldName = filter.getField();

            Object value;
            try {
                Method method = obj.getClass().
                        getMethod("get" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, fieldName));

                value = method.invoke(obj);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return false;
            }

            switch (filter.getOperation()) {
                case EQUAL:
                    return value.equals(filter.getValue());
                case IN:
                    for (Object filterValue : (Iterable) filter.getValue()) {
                        if (value.equals(filterValue))
                            return true;
                    }
                    return false;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return true;
    }


}
