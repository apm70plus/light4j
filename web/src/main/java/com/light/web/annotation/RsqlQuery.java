package com.light.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;


/**
 * Query of the RSQL (RESTful Service Query Language).
 *
 * <p>RSQL is a query language for parametrized filtering of entries in RESTful APIs. It's a
 * superset of the <a href="http://tools.ietf.org/html/draft-nottingham-atompub-fiql-00">FIQL</a>
 * (Feed Item Query Language), so it can be used for parsing FIQL as well.</p>
 *
 * <p><b>Grammar in EBNF notation:</b>
 * <pre>{@code
 * input          = or, EOF;
 * or             = and, { ( "," | " or " ) , and };
 * and            = constraint, { ( ";" | " and " ), constraint };
 * constraint     = ( group | comparison );
 * group          = "(", or, ")";
 *
 * comparison     = selector, comparator, arguments;
 * selector       = unreserved-str;
 *
 * comparator     = comp-fiql | comp-alt;
 * comp-fiql      = ( ( "=", { ALPHA } ) | "!" ), "=";
 * comp-alt       = ( ">" | "<" ), [ "=" ];
 *
 * arguments      = ( "(", value, { "," , value }, ")" ) | value;
 * value          = unreserved-str | double-quoted | single-quoted;
 *
 * unreserved-str = unreserved, { unreserved }
 * single-quoted  = "'", { all-chars - "'" }, "'";
 * double-quoted  = '"', { all-chars - '"' }, '"';
 *
 * reserved       = '"' | "'" | "(" | ")" | ";" | "," | "=" | "!" | "~" | "<" | ">" | " ";
 * unreserved     = all-chars - reserved;
 * all-chars      = ? all unicode characters ?;
 * }</pre>
 * <p><b>Examples of RSQL expressions:</b>
 * <pre>{@code
 * Grammar:
 * Logical AND              : ; or and
 * Logical OR               : , or or
 * Equal to                 : ==
 * Not equal to             : !=
 * Less than                : =lt= or <
 * Less than or equal to    : =le= or <=
 * Greater than operator    : =gt= or >
 * Greater than or equal to : =ge= or >=
 * In                       : =in=
 * Not in                   : =out=
 * 
 * Examples:
 * /movies?query=name=="Kill Bill";year=gt=2003
 * /movies?query=name=="Kill Bill";year>2003
 * /movies?query=name=="Kill Bill" and year>2003
 * /movies?query=genres=in=(sci-fi,action);(director=='Christopher Nolan',actor==*Bale);year=ge=2000
 * /movies?query=genres=in=(sci-fi,action) and (director=='Christopher Nolan' or actor==*Bale) and year>=2000
 * /movies?query=director.lastName==Nolan;year=ge=2000;year=lt=2010
 * /movies?query=director.lastName==Nolan and year>=2000 and year<2010
 * /movies?query=genres=in=(sci-fi,action);genres=out=(romance,animated,horror),director==Que*Tarantino
 * /movies?query=genres=in=(sci-fi,action) and genres=out=(romance,animated,horror) or director==Que*Tarantino
 * }</pre>
 * @author liuyg
 *
 */
@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RsqlQuery {

	/**
	 * The root type to create the {@link com.mysema.query.types.Predicate}. Specify this explicitly if the type is not
	 * contained in the controller method's return type.
	 * 
	 * @return
	 */
	Class<?> root();
	
	/**
	 * To customize the way individual properties' values should be bound to the predicate a
	 * {@link QuerydslBinderCustomizer} can be specified here. We'll try to obtain a Spring bean of this type but fall
	 * back to a plain instantiation if no bean is found in the current
	 * {@link org.springframework.beans.factory.BeanFactory}.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends QuerydslBinderCustomizer> bindings() default QuerydslBinderCustomizer.class;
}
