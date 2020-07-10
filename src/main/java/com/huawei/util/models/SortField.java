package com.huawei.util.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huawei.util.enums.OrderType;

public class SortField {
    private String field;

    @JsonProperty("order_type")
    private OrderType orderType;

    public SortField() {
    }

    public SortField(String field, OrderType orderType) {
        this.field = field;
        this.orderType = orderType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
