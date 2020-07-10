package com.huawei.util.enums;

public enum OrderType {
    ASCENDING("ascending"),
    DESCENDING("descending");

    private String order;

    OrderType(String order) {
        this.order = order;
    }

    public static OrderType fromString(String string) {
        if (OrderType.ASCENDING.order.equals(string)) {
            return OrderType.ASCENDING;
        } else if (OrderType.DESCENDING.order.equals(string)) {
            return OrderType.DESCENDING;
        } else {
            throw new IllegalArgumentException("Invalid order tyoe");
        }
    }

}
