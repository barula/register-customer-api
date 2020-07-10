package com.huawei.util.enums;

import dev.morphia.query.Criteria;
import dev.morphia.query.CriteriaContainer;
import dev.morphia.query.FieldEnd;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;


public enum Operation implements Executable {

    EXISTS("exists", (criteria,value) ->  {
        if (value != null && Boolean.parseBoolean(value.toString())) {
            return criteria.exists();
        }else{
            return  criteria.doesNotExist();
        }

    },(criteriaBuilder, expression, value ,operation) -> {
        return operation.notImplemented();
    }),
    EQUAL("==", FieldEnd::equal,(criteriaBuilder, expression, value, operation) -> {
        return criteriaBuilder.equal(expression, value);
    }),
    NOT_EQUAL("<>", FieldEnd::notEqual,(criteriaBuilder, expression, value, operation) -> {
        return criteriaBuilder.notEqual(expression, value);
    }),
    GREATER_THAN(">", FieldEnd::greaterThan,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Number) {
            return criteriaBuilder.gt(expression.as(Number.class), (Number) value);
        } else if (value instanceof Date) {
            return criteriaBuilder.greaterThan(expression, (Date) value);
        } else {
            throw new IllegalArgumentException(
                    "The operation 'greater than' can only be applied on number or dates");
        }
    }),
    GREATER_THAN_OR_EQUAL_TO(">=", FieldEnd::greaterThanOrEq,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Number) {
            Predicate gt = criteriaBuilder.gt(expression.as(Number.class), (Number) value);
            Predicate equal = criteriaBuilder.equal(expression, value);
            return criteriaBuilder.or(gt, equal);
        } else if (value instanceof Date) {
            return criteriaBuilder.greaterThanOrEqualTo(expression, (Date) value);
        } else {
            throw new IllegalArgumentException(
                    "The operation 'greater than or equal to' can only be applied on number or dates");
        }
    }),
    LESS_THAN("<", FieldEnd::lessThan,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Number) {
            return criteriaBuilder.lt(expression.as(Number.class), (Number) value);
        } else if (value instanceof Date) {
            return criteriaBuilder.lessThan(expression, (Date) value);
        } else {
            throw new IllegalArgumentException(
                    "The operation 'less than' can only be applied on number or dates");
        }
    }),
    LESS_THAN_OR_EQUAL_TO("<=", FieldEnd::lessThanOrEq,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Number) {
            Predicate lt = criteriaBuilder.lt(expression.as(Number.class), (Number) value);
            Predicate equal = criteriaBuilder.equal(expression, value);
            return criteriaBuilder.or(lt, equal);
        } else if (value instanceof Date) {
            return criteriaBuilder.lessThanOrEqualTo(expression, (Date) value);
        } else {
            throw new IllegalArgumentException(
                    "The operation 'less than or equal to' can only be applied on number or dates");
        }
    }),
    IN("in", (criteria,value) ->  criteria.in( (List<?>) value) ,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Collection) {
            Collection values = (Collection) value;
            return expression.in(values);
        } else {
            throw new IllegalArgumentException("The operation 'in' requires how to value a collection");
        }
    }),
    NOT_IN("nin", (criteria,value) ->  criteria.notIn( (List<?>) value) ,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Collection) {
            Collection values = (Collection) value;
            return criteriaBuilder.not(expression.in(values));
        } else throw new IllegalArgumentException("The operation 'in' requires how to value a collection");
    }),
    CONTAINS("",   (criteria,value) ->  value != null ? criteria.contains(value.toString()) : criteria.contains(""),(criteriaBuilder, expression, value, operation) -> {
        return operation.notImplemented();
    }),
    STARTS_WITH("",  (criteria,value) -> value != null ? criteria.startsWith(value.toString()) : criteria.startsWith(""),(criteriaBuilder, expression, value, operation) -> {
        return operation.notImplemented();
    }),
    ENDS_WITH("",  (criteria,value) -> value != null ? criteria.endsWith(value.toString()) : criteria.endsWith(""),(criteriaBuilder, expression, value, operation) -> {
        return operation.notImplemented();
    }),
    LIKE("", (criteria,value) ->  value != null ? criteria.contains(value.toString()) : criteria.contains(""),(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof String) {
            String string = (String) value;
            string = String.format("%%%s%%", string);
            return criteriaBuilder.like(expression, string);
        } else throw new IllegalArgumentException("The operation 'like' requires a string");
    }),
    IS_NULL("", (criteria,value) ->  criteria.equal(null) ,(criteriaBuilder, expression, value, operation) -> {
        return criteriaBuilder.isNull(expression);
    }),
    IS_NOT_NULL("",(criteria,value) ->  criteria.notEqual(null) ,(criteriaBuilder, expression, value, operation) -> {
        return criteriaBuilder.isNotNull(expression);
    }),
    BETWEEN("",(fieldEnd, o) -> null,(criteriaBuilder, expression, value, operation) -> {
        if (value instanceof Collection) {
            Collection values = (Collection) value;
            if (values.size() != 2)
                throw new IllegalArgumentException(
                        "The operation 'between' requires two values in the collection");
            Object[] arr = values.toArray();

            if (arr[0] instanceof Integer) {
                Expression<Integer> numberExpression = expression.as(Integer.class);
                return criteriaBuilder.between(numberExpression, (Integer) arr[0], (Integer) arr[1]);
            } else if (arr[0] instanceof Long) {
                Expression<Long> numberExpression = expression.as(Long.class);
                return criteriaBuilder.between(numberExpression, (Long) arr[0], (Long) arr[1]);
            } else if (arr[0] instanceof Double) {
                Expression<Double> numberExpression = expression.as(Double.class);
                return criteriaBuilder.between(numberExpression, (Double) arr[0], (Double) arr[1]);
            } else if (arr[0] instanceof Date) {
                Expression<Date> dateExpression = expression.as(Date.class);
                return criteriaBuilder.between(dateExpression, (Date) arr[0], (Date) arr[1]);
            } else {
                throw new IllegalArgumentException(
                        "The operation 'between' can only be applied on number or dates");
            }
        } else{
            throw new IllegalArgumentException(
                    "The operation 'between' requires how to value a collection");
        }
    });


	private final String condition;
    private BiFunction<FieldEnd<? extends CriteriaContainer>, Object, Object> functionFilterValue;
	private FourParameterFunction<CriteriaBuilder, Expression, Object, Operation, Predicate> functionCreatePredicate;

	Operation(String condition, BiFunction<FieldEnd<? extends CriteriaContainer>, Object, Object> functionFilterValue,
              FourParameterFunction<CriteriaBuilder, Expression, Object, Operation, Predicate> functionCreatePredicate) {

		this.condition = condition;

		this.functionFilterValue = functionFilterValue;
		this.functionCreatePredicate = functionCreatePredicate;
	}

    public Criteria addCriteriaFilter(FieldEnd<? extends CriteriaContainer> criteria, Object value) {
        if (functionFilterValue == null){
            throw new RuntimeException(this.name() + " not implemented yet");
        }
        return (Criteria) functionFilterValue.apply(criteria, value);
    }

	public String getCondition() {
		return condition;
	}

	@FunctionalInterface
	public interface FourParameterFunction<T, U, V, W, R> {
	    public R apply(T t, U u, V v, W w);
	}
	
	
	private Predicate notImplemented() {
		throw new IllegalArgumentException(String.format("The %s is not implemented.",
				this.name()));
	}

	@Override
	public Predicate execute(CriteriaBuilder cb, Expression expression, Object value) {
		return this.functionCreatePredicate.apply(cb, expression, value, this);
	}

}
