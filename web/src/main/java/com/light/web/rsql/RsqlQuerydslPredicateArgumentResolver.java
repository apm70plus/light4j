package com.light.web.rsql;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.RsqlQuerydslPredicateBuilder;
import org.springframework.data.querydsl.binding.RsqlQuerydslPredicateVisitor;
import org.springframework.data.util.CastUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.light.web.annotation.RsqlQuery;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

public class RsqlQuerydslPredicateArgumentResolver implements HandlerMethodArgumentResolver {

	private static final String QUERY_PARAM = "query";
	private RsqlQuerydslPredicateBuilderFactory factory;
	
	public RsqlQuerydslPredicateArgumentResolver(RsqlQuerydslPredicateBuilderFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RsqlQuery.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String queryStr = webRequest.getParameter(QUERY_PARAM);
		
		if (StringUtils.isBlank(queryStr)) {
			return Optional.ofNullable(null);
		}
		Node rootNode = new RSQLParser().parse(queryStr);
		Optional<RsqlQuery> annotation = Optional.ofNullable(parameter.getParameterAnnotation(RsqlQuery.class));
		Class<?> domainClass = annotation.get().root();
		Class<? extends QuerydslBinderCustomizer<?>> binderClass = CastUtils.cast(annotation.get().bindings());
		RsqlQuerydslPredicateBuilder builder = this.factory.newBuilder(domainClass, binderClass);
		RsqlQuerydslPredicateVisitor rsqlVisitor = new RsqlQuerydslPredicateVisitor(builder);
		return Optional.ofNullable(rootNode.accept(rsqlVisitor));
	}

}
