package com.huawei.util.datastore;

import com.huawei.util.models.SearchRequest;
import java.util.List;

public interface PageableDA<T> {

    List<T> search(SearchRequest searchRequest);

    int count(SearchRequest searchRequest);

}
