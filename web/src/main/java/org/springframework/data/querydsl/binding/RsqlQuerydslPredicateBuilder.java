package org.springframework.data.querydsl.binding;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RsqlQuerydslPredicateBuilder {
	private final TypeInformation<?> domainType;
	private final QuerydslBindings bindings;
	private final EntityPathResolver resolver;
	private final ConversionService conversionService;
	
	private final RsqlQuerydslBinding defaultBinding = new RsqlQuerydslBinding();
	private final Map<PathInformation, Path<?>> paths = new HashMap<>();
	
	public Predicate createPredicate(Node node) {
		if (node instanceof LogicalNode) {
            return createPredicate((LogicalNode) node);
        }
        if (node instanceof ComparisonNode) {
            return createPredicate((ComparisonNode) node);
        }
        return null;
	}
	
	public Predicate createPredicate(LogicalNode logicalNode) {
		List<Predicate> specs = logicalNode.getChildren()
		          .stream()
		          .map(node -> createPredicate(node))
		          .filter(Objects::nonNull)
		          .collect(Collectors.toList());
		BooleanBuilder where = new BooleanBuilder();
        if (logicalNode.getOperator() == LogicalOperator.AND) {
            for (int i = 0; i < specs.size(); i++) {
            	where.and(specs.get(i));
            }
        } else if (logicalNode.getOperator() == LogicalOperator.OR) {
            for (int i = 0; i < specs.size(); i++) {
            	where.or(specs.get(i));
            }
        }
		return where;
	}
	
    public Predicate createPredicate(ComparisonNode comparisonNode) {
    	String selector = comparisonNode.getSelector(); 
    	ComparisonOperator operator = comparisonNode.getOperator();
        List<String> args = comparisonNode.getArguments();

		if (isSingleElementCollectionWithoutText(args)) {
			return null;
		}
		if (!bindings.isPathAvailable(selector, domainType)) {
			return null;
		}
		PathInformation propertyPath = bindings.getPropertyPath(selector, domainType);

		if (propertyPath == null) {
			return null;
		}
		Collection<Object> value = convertToPropertyPathSpecificType(args, propertyPath);
		Optional<Predicate> predicate = invokeBinding(propertyPath, bindings, value, operator);
        return predicate.get();
	}
    
	private boolean isSingleElementCollectionWithoutText(List<String> source) {
		return source.size() == 1 && !StringUtils.hasText(source.get(0));
	}
	
	private Collection<Object> convertToPropertyPathSpecificType(List<String> source, PathInformation path) {

		Class<?> targetType = path.getLeafType();

		if (source.isEmpty() || isSingleElementCollectionWithoutText(source)) {
			return Collections.emptyList();
		}

		Collection<Object> target = new ArrayList<>(source.size());

		for (String value : source) {
			target.add(conversionService.canConvert(String.class, targetType)
					? conversionService.convert(value, TypeDescriptor.forObject(value), getTargetTypeDescriptor(path))
					: value);
		}

		return target;
	}
	
	private static TypeDescriptor getTargetTypeDescriptor(PathInformation path) {

		PropertyDescriptor descriptor = path.getLeafPropertyDescriptor();

		Class<?> owningType = path.getLeafParentType();
		String leafProperty = path.getLeafProperty();

		TypeDescriptor result = descriptor == null //
				? TypeDescriptor
						.nested(org.springframework.data.util.ReflectionUtils.findRequiredField(owningType, leafProperty), 0)
				: TypeDescriptor
						.nested(new Property(owningType, descriptor.getReadMethod(), descriptor.getWriteMethod(), leafProperty), 0);

		if (result == null) {
			throw new IllegalStateException(String.format("Could not obtain TypeDesciptor for PathInformation %s!", path));
		}

		return result;
	}
	
	private Optional<Predicate> invokeBinding(PathInformation dotPath, QuerydslBindings bindings,
			Collection<Object> values, ComparisonOperator operator) {

		Path<?> path = getPath(dotPath, bindings);
		Optional<MultiValueBinding<Path<? extends Object>, Object>> binding = bindings.getBindingForPath(dotPath);
		if (binding.isPresent()) {
			return binding.get().bind(path, values);
		} else {
			return defaultBinding.bind(path, values, operator);
		}
	}
	
	private Path<?> getPath(PathInformation path, QuerydslBindings bindings) {

		Optional<Path<?>> resolvedPath = bindings.getExistingPath(path);

		return resolvedPath.orElseGet(() -> paths.computeIfAbsent(path, it -> it.reifyPath(resolver)));
	}
}
