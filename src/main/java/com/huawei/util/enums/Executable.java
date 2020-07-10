package com.huawei.util.enums;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public interface Executable {

	Predicate execute(CriteriaBuilder cb, Expression expression, Object object);
}
