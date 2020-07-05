package com.light.web.rsql;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.RsqlQuerydslPredicateBuilder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.stereotype.Component;

@Component
public class RsqlQuerydslPredicateBuilderFactory {

	@Autowired @Qualifier("mvcConversionService")
	private ObjectFactory<ConversionService> conversionService;
	@Autowired
	private QuerydslBindingsFactory querydslBindingsFactory;
	
	private ConversionService cs;
	private EntityPathResolver resolver;
	
	@PostConstruct
	public void init() {
		cs = Optional.of(conversionService.getObject()).orElseGet(DefaultConversionService::new);
		resolver = querydslBindingsFactory.getEntityPathResolver();
	}
	
	public RsqlQuerydslPredicateBuilder newBuilder(Class<?> domainClass, Class<? extends QuerydslBinderCustomizer<?>> binderClass) {
		TypeInformation<?> type = ClassTypeInformation.from(domainClass);
		QuerydslBindings bindings = null;
		if (binderClass == null) {
			bindings = querydslBindingsFactory.createBindingsFor(type);
		} else {
			bindings = querydslBindingsFactory.createBindingsFor(type, binderClass);
		}
		return new RsqlQuerydslPredicateBuilder(type, bindings, resolver, cs);
	}
}
