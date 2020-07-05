package com.light.jpa.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.jdbc.core.JdbcTemplate;

import com.light.support.Searchable;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class QueryRepository<E> {

	protected EntityManager em;
	private Class<E> modelClass;
	private JPAQueryFactory queryFactory;
	private Querydsl querydsl;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	private ApplicationContext applicationContext;
	
	@SuppressWarnings("unchecked")
	public QueryRepository() {
		Type t = this.getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments(); // 取得所有泛型
			this.modelClass = (Class<E>) p[0];
		}
	}
	
	/**
	 * JPAQueryFactory实例
	 *
	 * @return
	 */
	protected JPAQueryFactory queryFactory() {
		if (this.queryFactory == null) {
			this.queryFactory = new JPAQueryFactory(this.getEntityManager());
		}
		return this.queryFactory;
	}
	
	/**
	 * 查询语句构建器实例
	 * @return
	 */
	protected BooleanBuilder newWhere() {
		return new BooleanBuilder();
	}
	
	/**
	 * 分页查询
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	protected <T> Page<T> findPage(final JPAQuery<T> query, final Pageable pageable) {
		final long count = query.fetchCount();
		List<T> listResult = Collections.emptyList();
		if (count > 0) {
			this.applyPagination(query, pageable);
			listResult = query.fetch();
		}
		final Page<T> pageResult = new PageImpl<>(listResult, pageable, count);
		return pageResult;
	}
	
	/**
	 * 获取JdbcTemplate实例，用于本地SQL查询
	 * @return
	 */
	protected JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// String 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取字符串型的比较类条件描述。</br>
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression compareStr(final Function<String, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue == null) {
				return fieldExpression.isNull();
			} else {
				return fieldExpression.eq(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串型的 Contains 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression containsStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.contains(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串型的 StartsWith 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression startsWithStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.startsWith(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串型的 EndsWith 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression endsWithStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.endsWith(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串的In条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inStrings(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				return fieldExpression.in(Arrays.asList(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取字符串 Date 型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterDateStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.gt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串 Date型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterOrEqualsDateStr(final StringExpression fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取字符串 Date型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeDateStr(final StringExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.lt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeOrEqualsDateStr(final StringExpression fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(searchValue);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Boolean 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取布尔型的条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在 或 为null，返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsBool(final BooleanExpression fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Boolean searchValue = searchable.getBooleanValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(searchValue);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Double 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取Double型的比较类条件描述。
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression compareDouble(final Function<Double, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Double型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsDouble(final NumberExpression<Double> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Double型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanDouble(final NumberExpression<Double> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.gt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Double型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanOrEqualsDouble(final NumberExpression<Double> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Double型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanOrEqualsDouble(final NumberExpression<Double> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(searchValue);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Long 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取Long型的比较类条件描述。
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression compareLong(final Function<Long, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Long型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsLong(final NumberExpression<Long> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanOrEqualsDecimal(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Double型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanDouble(final NumberExpression<Double> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Double searchValue = searchable.getDoubleValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.lt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Double的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inDoubles(final NumberExpression<Double> fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<Double> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(Double.parseDouble(value));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	/**
	 * 获取Long型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanLong(final NumberExpression<Long> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.gt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Long型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanOrEqualsLong(final NumberExpression<Long> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Long型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanOrEqualsLong(final NumberExpression<Long> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Long型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanLong(final NumberExpression<Long> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.lt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Long的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inLongs(final NumberExpression<Long> fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<Long> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(Long.parseLong(value));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Integer 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取Integer型的比较类条件描述。
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression compareInt(final Function<Integer, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsInt(final NumberExpression<Integer> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanInt(final NumberExpression<Integer> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.gt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanOrEqualsInt(final NumberExpression<Integer> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanOrEqualsInt(final NumberExpression<Integer> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanInt(final NumberExpression<Integer> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Integer searchValue = searchable.getIntValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.lt(searchValue);
			}
		}
		return null;
	}

	/**
	 * 获取Integer的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inInts(final NumberExpression<Integer> fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<Integer> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(Integer.parseInt(value));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// BigDecimal 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取BigDecimal型的比较类条件描述。
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression compareDecimal(final Function<BigDecimal, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsDecimal(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanDecimal(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.gt(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression greaterThanOrEqualsDecimal(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression lessThanDecimal(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.lt(new BigDecimal(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取BigDecimal的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inDecimals(final NumberExpression<BigDecimal> fieldExpression,
			final Searchable searchable, final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<BigDecimal> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(new BigDecimal(value));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Date 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取Date型的比较类条件描述。
	 *
	 * @param comparableFunction 字段的比较函数
	 * @param searchable         过滤值
	 * @param searchKey          条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression compareDate(final Function<Date, BooleanExpression> comparableFunction,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return comparableFunction.apply(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsDate(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterDate(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.after(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterOrEqualsDate(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeDate(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.before(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeOrEqualsDate(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inDates(final DatePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<Date> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(new Date(Long.parseLong(value)));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 Equals 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression equalsDate(final DateTimePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.eq(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 大于(>) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterDate(final DateTimePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.after(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 大于等于(>=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression afterOrEqualsDate(final DateTimePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.goe(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 小于(<) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeDate(final DateTimePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.before(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date型的 小于等于(<=) 条件描述。
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 */
	protected BooleanExpression beforeOrEqualsDate(final DateTimePath<Date> fieldExpression,
			final Searchable searchable, final String searchKey) {
		if (searchable.hasKey(searchKey)) {
			final Long searchValue = searchable.getLongValue(searchKey);
			if (searchValue != null) {
				return fieldExpression.loe(new Date(searchValue));
			}
		}
		return null;
	}

	/**
	 * 获取Date的 In 条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在返回null；否则返回QueryDSL条件描述
	 * @return
	 */
	protected BooleanExpression inDates(final DateTimePath<Date> fieldExpression, final Searchable searchable,
			final String searchKey) {

		if (searchable.hasKey(searchKey)) {
			final String[] searchValue = searchable.getStrArray(searchKey);
			if ((searchValue != null) && (searchValue.length > 0)) {
				final List<Date> values = new ArrayList<>();
				for (final String value : searchValue) {
					values.add(new Date(Long.parseLong(value)));
				}
				return fieldExpression.in(values);
			}
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////// Boolean 类型的检索条件
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取布尔型的条件描述。</br>
	 *
	 * @param fieldExpression 字段描述
	 * @param searchable      过滤值
	 * @param searchKey       条件Key
	 * @return 如果过滤值不存在 或 为null，返回null；否则返回QueryDSL条件描述
	 */
	protected <T extends Enum<T>> BooleanExpression equalsEnum(final EnumPath<T> fieldExpression,
			final Searchable searchable, final String searchKey, final Function<String, T> enumConstructor) {
		if (searchable.hasKey(searchKey)) {
			final String searchValue = searchable.getStrValue(searchKey);
			if (searchValue != null) {
				final T value = enumConstructor.apply(searchValue);
				return fieldExpression.eq(value);
			}
		}
		return null;
	}
	
	/**
	 * 分页 + 排序
	 *
	 * @param query
	 * @param pageable
	 * @return
	 */
	private <T> void applyPagination(final JPAQuery<T> query, final Pageable pageable) {
		this.querydsl().applyPagination(pageable, query);
	}
	
	private Querydsl querydsl(Class<?> modelClass) {
		if (this.querydsl == null) {
			this.querydsl = new Querydsl(this.em, new PathBuilderFactory().create(modelClass));
		}
		return this.querydsl;
	}
	
	private Querydsl querydsl() {
		return querydsl(this.modelClass);
	}
	
	/**
	 * 可以重写该方法，实现自定义的EntityManager（比如多数据源）
	 * @return
	 */
	protected EntityManager getEntityManager() {
		if (em == null) {
			em = applicationContext.getBean(EntityManager.class);
		}
 		return this.em;
	}
}
