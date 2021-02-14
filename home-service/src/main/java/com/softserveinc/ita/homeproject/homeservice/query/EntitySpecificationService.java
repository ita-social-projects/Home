package com.softserveinc.ita.homeproject.homeservice.query;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EntitySpecificationService<T> {

    public static final String DEFAULT_SORT = "id,desc";
    public static final String RSQL_EQUAL = "==";
    public static final String RSQL_AND = ";";

    public Specification<T> getSpecification(Map<QueryParamEnum, String> filter, String search, String sort) {
        Specification<T> filterSpecification = RSQLJPASupport.toSpecification(toRQSQLString(filter));
        Specification<T> searchSpecification = RSQLJPASupport.toSpecification(search);
        Specification<T> sortSpecification = RSQLJPASupport.toSort(sort == null ? DEFAULT_SORT : sort);

        Optional<Specification<T>> optSearchSpecification = Optional.of(searchSpecification);
        Optional<Specification<T>> optFilterSpecification = Optional.of(filterSpecification);

        return sortSpecification.and(optFilterSpecification.get()).and(optSearchSpecification.get());
    }

    private static String toRQSQLString(Map<QueryParamEnum, String> filter) {
        return filter.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> entry.getKey().getParameter() + RSQL_EQUAL + entry.getValue())
                .collect(Collectors.joining(RSQL_AND));
    }

}
