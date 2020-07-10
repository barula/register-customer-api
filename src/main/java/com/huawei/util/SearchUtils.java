package com.huawei.util;

import com.huawei.util.datastore.PageableDA;
import com.huawei.util.models.Filter;
import com.huawei.util.models.Page;
import com.huawei.util.models.SearchRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchUtils {
    private static final Logger log = LogManager.getLogger(SearchUtils.class);

    public static <E, M> Page<M> search(SearchRequest searchRequest, PageableDA<E> datastore, Function<E, M> toModel) {
        return search(searchRequest, datastore, toModel,null);
    }

    public static <E, M> Page<M> search(SearchRequest searchRequest, PageableDA<E> datastore, Function<E, M> toModel, Realm realm) {
        Page<M> pageObject = new Page<>();

        int count = datastore.count(searchRequest);

        if (count > 0) {
            pageObject.setRows(getRows(datastore.search(searchRequest), toModel,realm));
        } else {
            pageObject.setRows(new ArrayList<>());
        }

        pageObject.setLimit(searchRequest.getLimit());
        pageObject.setPage(searchRequest.getPage());
        pageObject.setSize(count);
        pageObject.setFilters(searchRequest.getFilters());
        pageObject.setSorts(searchRequest.getSorts());
        return pageObject;
    }

    private static <M, E> List<M> getRows(List<E> rowsEntities, Function<E, M> toModel, Realm realm) {
        ArrayList<M> rowsModel = new ArrayList<>();
        for (E entity : rowsEntities) {
            try {
                M apply = toModel.apply(entity);
                rowsModel.add(apply);
            } catch (Exception e) {
                log.error("Error converting entity to model ", e);
            }
        }
        return rowsModel;
    }

    public static String getValue(SearchRequest searchRequest, String filterName) {
        Filter f = searchRequest.getFilters().stream().filter(filter ->
                filter.getField().equals(filterName)).findFirst().orElse(new Filter());
        String value = null;
        if (f.getValue() != null) {
            value = f.getValue().toString();
        }
        return value;
    }
}
