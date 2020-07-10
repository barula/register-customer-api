package com.huawei.util.models;

import com.huawei.util.enums.Operation;
import dev.morphia.query.CriteriaContainer;
import dev.morphia.query.FieldEnd;
import dev.morphia.query.Query;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Filter {

    private String field;
    private Operation operation;
    private Object value;
    private List<Filter> connectorFilter;

    public Filter() {

    }

    public Filter(String field, Operation operation, Object value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public static Filter create(String field, Operation operation, Object value) {
        return new Filter(field, operation, value);
    }

    public static Filter exists(String field, Boolean exists) {
        return new Filter(field, Operation.EXISTS, exists);
    }

    public static Filter equal(String field, Object value) {
        return new Filter(field, Operation.EQUAL, value);
    }

    public static Filter notEqual(String field, Object value) {
        return new Filter(field, Operation.NOT_EQUAL, value);
    }

    public static Filter greaterThan(String field, Number value) {
        return new Filter(field, Operation.GREATER_THAN, value);
    }

    public static Filter greaterThan(String field, Date value) {
        return new Filter(field, Operation.GREATER_THAN, value);
    }

    public static Filter greaterThanOrEqualTo(String field, Number value) {
        return new Filter(field, Operation.GREATER_THAN_OR_EQUAL_TO, value);
    }

    public static Filter greaterThanOrEqualTo(String field, Date value) {
        return new Filter(field, Operation.GREATER_THAN_OR_EQUAL_TO, value);
    }

    public static Filter lessThan(String field, Number value) {
        return new Filter(field, Operation.LESS_THAN, value);
    }

    public static Filter lessThan(String field, Date value) {
        return new Filter(field, Operation.LESS_THAN, value);
    }

    public static Filter lessThanOrEqualTo(String field, Number value) {
        return new Filter(field, Operation.LESS_THAN_OR_EQUAL_TO, value);
    }

    public static Filter lessThanOrEqualTo(String field, Date value) {
        return new Filter(field, Operation.LESS_THAN_OR_EQUAL_TO, value);
    }


    public static Filter contain(String field, String value) {
        Pattern regexp = Pattern.compile(".*" + value + ".*");
        return new Filter(field, Operation.CONTAINS, regexp);
    }

    public static Filter startWith(String field, String value) {
        Pattern regexp = Pattern.compile("^" + value + ".*");
        return new Filter(field, Operation.STARTS_WITH, regexp);
    }

    public static Filter endWith(String field, String value) {
        Pattern regexp = Pattern.compile(".*" + value + "$");
        return new Filter(field, Operation.ENDS_WITH, regexp);
    }

    public static Filter in(String field, Collection list) {
        return new Filter(field, Operation.IN, list);
    }

    public static Filter in(String field, List<String> list) {
        return new Filter(field, Operation.IN, list);
    }

    public static Filter notIn(String field, Collection list) {
        return new Filter(field, Operation.NOT_IN, list);
    }

    public static Filter like(String field, String value) {
        value = String.format("%%%s%%", value);
        return new Filter(field, Operation.LIKE, value);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Filter> getConnectorFilter() {
        return connectorFilter;
    }

    public void setConnectorFilter(List<Filter> connectorFilter) {
        this.connectorFilter = connectorFilter;
    }

    public void createCriteria(Query query) {
        FieldEnd<? extends CriteriaContainer> criteria = query.criteria(this.field);
        if (connectorFilter != null){
            query.and(operation.addCriteriaFilter(criteria,value));
            connectorFilter.forEach(f -> query.and(f.getOperation().addCriteriaFilter(criteria,f.getValue())) );

        }else{
            operation.addCriteriaFilter(criteria,value);

        }
    }
}

