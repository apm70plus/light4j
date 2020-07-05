package org.springframework.data.querydsl.binding;

import java.util.Collection;
import java.util.Optional;

import org.springframework.util.Assert;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public class RsqlQuerydslBinding {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Optional<Predicate> bind(Path<?> path, Collection<? extends Object> value, ComparisonOperator operator) {

		Assert.notNull(path, "Path must not be null!");
		Assert.notNull(value, "Value must not be null!");
		Assert.notNull(operator, "Operator must not be null!");

		if (value.isEmpty()) {
			return Optional.empty();
		}
		// 处理集合类型
		if (path instanceof CollectionPathBase) {
			BooleanBuilder builder = new BooleanBuilder();
			for (Object element : value) {
				builder.and(((CollectionPathBase) path).contains(element));
			}
			return Optional.of(builder.getValue());
		}
		// 处理Comparable类型的“范围查询”
		if (path instanceof ComparableExpression) {
			if (RSQLOperators.GREATER_THAN.equals(operator)) {
				return Optional.of(((ComparableExpression) path).gt((Comparable<?>)value.iterator().next()));
			} else if (RSQLOperators.GREATER_THAN_OR_EQUAL.equals(operator)) {
				return Optional.of(((ComparableExpression) path).goe((Comparable<?>)value.iterator().next()));
			} else if (RSQLOperators.LESS_THAN.equals(operator)) {
				return Optional.of(((ComparableExpression) path).lt((Comparable<?>)value.iterator().next()));
			} else if (RSQLOperators.LESS_THAN_OR_EQUAL.equals(operator)) {
				return Optional.of(((ComparableExpression) path).loe((Comparable<?>)value.iterator().next()));
			}
		}
		// 处理数值型的范围查找
		if (path instanceof NumberExpression) {
			if (RSQLOperators.GREATER_THAN.equals(operator)) {
				return Optional.of(((NumberExpression) path).gt((Number)value.iterator().next()));
			} else if (RSQLOperators.GREATER_THAN_OR_EQUAL.equals(operator)) {
				return Optional.of(((NumberExpression) path).goe((Number)value.iterator().next()));
			} else if (RSQLOperators.LESS_THAN.equals(operator)) {
				return Optional.of(((NumberExpression) path).lt((Number)value.iterator().next()));
			} else if (RSQLOperators.LESS_THAN_OR_EQUAL.equals(operator)) {
				return Optional.of(((NumberExpression) path).loe((Number)value.iterator().next()));
			}
		}
		// 处理String类型的EQUAL操作（还包括左匹配、右匹配、中间匹配）
		if (path instanceof StringExpression && RSQLOperators.EQUAL.equals(operator)) {
			String str = (String) value.iterator().next();
			if (str == null) {
				return Optional.of(((SimpleExpression) path).isNull());
			}
			boolean endsWith = str.startsWith("*");
			boolean startsWith = str.endsWith("*");
			Optional.of(((StringExpression) path).like(str));
			if (startsWith && endsWith) {
				return Optional.of(((StringExpression) path).contains(str.substring(1, str.length() - 1)));
			} else if (startsWith) {
				return Optional.of(((StringExpression) path).startsWith(str.substring(0, str.length() - 1)));
			} else if (endsWith) {
				return Optional.of(((StringExpression) path).endsWith(str.substring(1)));
			} else {
				return Optional.of(((StringExpression) path).eq(str));
			}
		}
		if (path instanceof SimpleExpression) {
			if (RSQLOperators.EQUAL.equals(operator)) {
				Object v = value.iterator().next();
				if (v == null) {
					return Optional.of(((SimpleExpression) path).isNull());
				} else {
					return Optional.of(((SimpleExpression) path).eq(v));
				}
			} else if (RSQLOperators.NOT_EQUAL.equals(operator)) {
				Object v = value.iterator().next();
				if (v == null) {
					return Optional.of(((SimpleExpression) path).isNotNull());
				} else {
					return Optional.of(((SimpleExpression) path).ne(value.iterator().next()));
				}
			} else if (RSQLOperators.IN.equals(operator)) {
				if (value.size() > 1) {
					return Optional.of(((SimpleExpression) path).in(value));
				} else {
					return Optional.of(((SimpleExpression) path).eq(value.iterator().next()));
				}
			} else if (RSQLOperators.NOT_IN.equals(operator)) {
				if (value.size() > 1) {
					return Optional.of(((SimpleExpression) path).notIn(value));
				} else {
					return Optional.of(((SimpleExpression) path).ne(value.iterator().next()));
				}
			}
		}
		throw new IllegalArgumentException(
				String.format("Cannot create predicate for path '%s' with type '%s'.", path, path.getMetadata().getPathType()));
	}
}