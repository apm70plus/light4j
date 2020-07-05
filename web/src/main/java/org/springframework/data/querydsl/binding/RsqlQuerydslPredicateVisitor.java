package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.Predicate;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class RsqlQuerydslPredicateVisitor implements RSQLVisitor<Predicate, Void> {

	private final RsqlQuerydslPredicateBuilder builder;
	
	public RsqlQuerydslPredicateVisitor(RsqlQuerydslPredicateBuilder builder) {
		this.builder = builder;
	}
	
	@Override
	public Predicate visit(AndNode node, Void param) {
		return builder.createPredicate(node);
	}

	@Override
	public Predicate visit(OrNode node, Void param) {
		return builder.createPredicate(node);
	}

	@Override
	public Predicate visit(ComparisonNode node, Void param) {
		return builder.createPredicate(node);
	}

}
