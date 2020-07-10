package com.huawei.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import com.huawei.util.enums.Operation;
import com.huawei.util.enums.OrderType;
import com.huawei.util.models.Filter;
import com.huawei.util.models.SearchRequest;
import com.huawei.util.models.SortField;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.text.CaseUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class SearchFactory {

    /**
     * Parsea un search request para obtener un query a utilizar en servicios que implementan filter utils.
     *
     * @param searchRequest objeto search request
     * @return String query
     */
    public static String fromSearchRequest(SearchRequest searchRequest) {
        StringBuilder query = new StringBuilder();
        query.append("?");
        appendPage(query, searchRequest);
        appendLimit(query, searchRequest);
        appendFilters(query, searchRequest);
        appendSorts(query, searchRequest);
        return query.toString();
    }

    private static void appendPage(StringBuilder query, SearchRequest searchRequest) {
        if (searchRequest.getPage() > 1) {

            addFirstAmpersand(query,"page=");
            query.append(searchRequest.getPage());
        }
    }

    private static void appendLimit(StringBuilder query, SearchRequest searchRequest) {
        if (searchRequest.getLimit() > 1) {

            addFirstAmpersand(query,"limit=");
            query.append(searchRequest.getLimit());
        }
    }

    private static void appendFilters(StringBuilder query, SearchRequest searchRequest) {
        List<Filter> filters = searchRequest.getFilters();
        if (filters != null && !filters.isEmpty()) {
            for (int i = 0; i < filters.size(); i++) {
                Filter filter = filters.get(i);

                addFirstAmpersand(query,"filters[");

                query.append(i);
                query.append("][field]=");
                query.append(filter.getField());
                query.append("&filters[");
                query.append(i);
                query.append("][operation]=");
                query.append(filter.getOperation());
                query.append("&filters[");
                query.append(i);
                query.append("][value]=");
                query.append(filter.getValue().toString()
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", ""));
            }
        }
    }

    private static void appendSorts(StringBuilder query, SearchRequest searchRequest) {
        List<SortField> sorts = searchRequest.getSorts();
        if (sorts != null && !sorts.isEmpty()) {
            for (int i = 0; i < sorts.size(); i++) {
                SortField sortField = sorts.get(i);

                addFirstAmpersand(query,"sorts[");

                query.append(i);
                query.append("][");
                query.append(sortField.getField());
                query.append("]=");
                query.append(sortField.getOrderType().toString());
            }
        }
    }

    /**
     * Parsea una URI para formar un SearchRequest.
     * lanza IllegalArgumentException si no pudo parsear.
     * Se asume que la clase annotatedModelClass sigue la convencion camelCase y
     * por queryParam se utiliza snake_case.
     *
     * @param queryString         query string para parsear
     * @param realm               realm con el que se quiere hacer la busqueda
     * @param annotatedModelClass modelo de la entidad a filtrar
     * @return SearchRequest
     */
    public static SearchRequest fromUri(String queryString, Realm realm, Class annotatedModelClass) {
        List<NameValuePair> params = URLEncodedUtils.parse(queryString, Charsets.UTF_8);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPage(getPage(params));
        searchRequest.setLimit(getLimit(params, realm));
        searchRequest.setFilters(parseFilters(params));
        searchRequest.setSorts(parseSorts(params));
        searchRequest.setRequestRealm(realm.getName());

        checkAnnotations(searchRequest.getFilters(), realm, annotatedModelClass);
        checkSorts(searchRequest.getSorts(), annotatedModelClass);
        setFiltersObject(searchRequest.getFilters(), annotatedModelClass);
        return searchRequest;
    }

    private static void checkSorts(List<SortField> sorts, Class annotatedModelClass) {
        for (SortField sortField : sorts) {
            getField(annotatedModelClass, sortField.getField());
        }
    }

    private static void setFiltersObject(List<Filter> filters, Class annotatedModelClass) {
        ObjectMapper om = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmssz");

        om.setDateFormat(df);
        for (Filter filter : filters) {
            checkCompleteFilter(filter);

            Class fieldClass = getFieldClass(annotatedModelClass, filter.getField());
            if (Operation.NOT_IN.equals(filter.getOperation()) || Operation.IN.equals(filter.getOperation())) {
                String[] split = filter.getValue().toString().split(",");
                filter.setValue(Arrays.asList(split));
            } else {
                filter.setValue(om.convertValue(filter.getValue().toString(), fieldClass));
            }

            if (filter.getConnectorFilter() != null) {
                setFiltersObject(filter.getConnectorFilter(), annotatedModelClass);
            }
        }
    }

    private static void checkCompleteFilter(Filter filter) {
        if (filter.getOperation() == null || filter.getField() == null || filter.getValue() == null) {
            throw new IllegalArgumentException("Invalid filter.");
        }
    }

    private static int getPage(List<NameValuePair> params) {
        int page;
        try {
            page = Integer
                .parseInt(params.stream().filter(nameValuePair -> nameValuePair.getName().equals("page")).findFirst().orElse(new BasicNameValuePair("page", "0")).getValue());
        } catch (NumberFormatException e) {
            page = 1;
        }
        if (page == 0) {
            page = 1;
        }
        return page;
    }

    private static int getLimit(List<NameValuePair> params, Realm realm) {
        int defaultLimit = Integer.parseInt(System.getProperties().getProperty("com.paypertic.default.page.limit", "10"));
        int maxLimit = Integer.parseInt(System.getProperties().getProperty("com.paypertic.default.page.max", "100"));
        int limit;
        try {
            limit = Integer
                .parseInt(params.stream().filter(nameValuePair -> nameValuePair.getName().equals("limit")).findFirst().orElse(new BasicNameValuePair("limit", "0")).getValue());
        } catch (NumberFormatException e) {
            limit = defaultLimit;
        }

        if (limit == 0) {
            limit = defaultLimit;
        } else if (!realm.equals(Realm.SYSTEM) && limit > maxLimit) {
            limit = maxLimit;
        }
        return limit;
    }

    private static List<Filter> parseFilters(List<NameValuePair> params) {
        List<NameValuePair>
            nameValuePairs = params.stream().filter(nameValuePair -> nameValuePair.getName().contains("filters")).map(
            SearchFactory::removeFiltersNameValue).collect(Collectors.toList());
        List<Filter> filters = new ArrayList<>();
        for (NameValuePair pair : nameValuePairs) {
            addFilter(pair, filters);
        }
        HashMap<String, List<Filter>> repeatFields = new HashMap<>();
        filters.forEach(filter -> {
            if (repeatFields.containsKey(filter.getField())) {
                repeatFields.get(filter.getField()).add(filter);

            } else {
                List<Filter> connectorList = new ArrayList<>();
                connectorList.add(filter);
                repeatFields.put(filter.getField(), connectorList);
            }
        });

        ArrayList<Filter> connectorFilters = new ArrayList<>();
        repeatFields.keySet().stream().filter(key -> repeatFields.get(key).size() > 1)
                .forEach(key -> {
                    List<Filter> filtersRep = repeatFields.get(key);
                    Filter f = filtersRep.get(0);
                    f.setConnectorFilter(filtersRep.stream().skip(1).collect(Collectors.toList()));
                    filtersRep.forEach(filters::remove);
                    connectorFilters.add(f);
                });
        filters.addAll(connectorFilters);
        return filters;
    }

    private static List<SortField> parseSorts(List<NameValuePair> params) {
        return params.stream()
                .filter(nameValuePair -> nameValuePair.getName().contains("sorts"))
                .map(SearchFactory::removeSortsNameValue)
                .map(SearchFactory::parseSort)
                .collect(Collectors.toList());
    }

    private static SortField parseSort(NameValuePair nameValuePair) {
        SortField sortField = new SortField();
        sortField.setField(parseField(nameValuePair.getName()));
        sortField.setOrderType(OrderType.fromString(nameValuePair.getValue()));
        return sortField;
    }

    private static String parseField(String name) {
        return Stream.of(name.split("\\.")).map(s -> s.replaceAll("\\W|\\d", "")).reduce((s, s2) -> s + "." + s2).orElse(name);
    }

    private static NameValuePair removeSortsNameValue(NameValuePair nameValuePair) {
        return new BasicNameValuePair(nameValuePair.getName().replaceAll("sorts", ""), nameValuePair.getValue());
    }

    private static NameValuePair removeFiltersNameValue(NameValuePair nameValuePair) {
        return new BasicNameValuePair(nameValuePair.getName().replaceAll("filters", ""), nameValuePair.getValue());
    }

    private static void addFilter(NameValuePair pair, List<Filter> filters) {
        int filterPos = parseFilterPos(pair.getName());
        if (filterPos > filters.size() - 1) {
            Filter filter = new Filter();
            filters.add(parseFilterValue(filter, pair));
        } else {
            parseFilterValue(filters.get(filterPos), pair);
        }
    }

    private static int parseFilterPos(String name) {
        return Integer.parseInt(name.replaceAll("\\D+", ""));
    }

    private static Filter parseFilterValue(Filter filter, NameValuePair pair) {
        if (pair.getName().contains("field")) {
            filter.setField(pair.getValue());
        } else if (pair.getName().contains("operation")) {
            filter.setOperation(Operation.valueOf(pair.getValue()));
        } else if (pair.getName().contains("value")) {
            filter.setValue(pair.getValue());
        } else {
            throw new IllegalArgumentException("Invalid filter.");
        }
        return filter;
    }

    private static void checkAnnotations(List<Filter> filters, Realm realm, Class clazz) {
        for (Filter filter : filters) {
            Field field = getField(clazz, filter.getField());
            List<FilterAllow> filterAllows = Arrays
                .stream(field.getAnnotationsByType(FilterAllow.class)).filter(filterAllow -> isRealmPresent(filterAllow, realm)).collect(
                    Collectors.toList());
            if (!filterAllows.isEmpty() && filterAllows.stream().noneMatch(filterAllow -> isOperationInFilterable(filter.getOperation(), filterAllow))) {
                throw new IllegalArgumentException(filter.getField() + " can not be filtered with " + filter.getOperation() + ".");
            } else if (filterAllows.isEmpty()) {
                throw new IllegalArgumentException(filter.getField() + " can not be filter.");
            }
            if (filterAllows.stream().anyMatch(FilterAllow::disableCaseSensitive)) {
                if (filter.getValue() instanceof String) {
                    filter.setValue(((String) filter.getValue()).toLowerCase());
                }
            }
        }
    }

    private static boolean isRealmPresent(FilterAllow filterAllow, Realm filterRealm) {
        return Arrays.asList(filterAllow.realms()).contains(filterRealm);
    }

    private static boolean isOperationInFilterable(Operation operation, FilterAllow filterAllow) {
        return Arrays.asList(filterAllow.availableOperations()).contains(operation);
    }

    private static Class getFieldClass(Class clazz, String fieldStr) {
        Class returnClass = getField(clazz, fieldStr).getType();
        if (Map.class.isAssignableFrom(returnClass)) {
            return String.class;
        } else {
            return returnClass;
        }
    }

    private static Field getField(Class clazz, String fieldStr) {
        try {
            Field field = null;
            String[] split = fieldStr.split("\\.");
            for (String f : split) {
                field = getDeclaredField(CaseUtils.toCamelCase(f, false, new char[]{'_'}), clazz);
                if (Collection.class.isAssignableFrom(field.getType())) {
                    clazz = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    return field;
                } else {
                    clazz = field.getType();
                }
            }
            return field;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Invalid field \"" + fieldStr + "\".");
        }
    }

    private static Field getDeclaredField(String field, Class clazz) throws NoSuchFieldException {

        while (clazz.getSuperclass() != null) {
            if (Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.getName().equals(field))) {
                return clazz.getDeclaredField(field);
            }
            for (Field fieldObj : clazz.getDeclaredFields()) {
                FilterAllow[] annotations = fieldObj.getAnnotationsByType(FilterAllow.class);
                if (Arrays.stream(annotations).anyMatch(fa -> field.equals(fa.fieldName()) ||
                        CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE).convert(field)
                                .equals(fa.fieldName()))) {
                    return fieldObj;
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new NoSuchFieldException();
    }

    private static void addFirstAmpersand(StringBuilder query, String queryParam){

        if(!query.toString().endsWith("?")){
            query.append("&");
        }

        query.append(queryParam);
    }
}
