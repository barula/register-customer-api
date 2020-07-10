package com.huawei.util;

import com.huawei.util.enums.Operation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(FilterAllow.List.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterAllow {
    Operation[] availableOperations();
    String fieldName() default "";

    Realm[] realms();
    boolean disableCaseSensitive() default false;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface List {
        FilterAllow[] value();
    }
}
