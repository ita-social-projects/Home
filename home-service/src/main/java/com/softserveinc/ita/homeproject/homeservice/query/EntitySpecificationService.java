package com.softserveinc.ita.homeproject.homeservice.query;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EntitySpecificationService<T> {

    public static final String DEFAULT_SORT = "id,desc";

    public static final String RSQL_EQUAL = "==";

    public static final String RSQL_AND = ";";

    private static String toRSQLString(Map<String, String> filter) {
        return filter.entrySet().stream()
            .filter(entry -> entry.getValue() != null)
            .map(entry -> entry.getKey() + RSQL_EQUAL + entry.getValue())
            .collect(Collectors.joining(RSQL_AND));
    }

    public Specification<T> getSpecification(Map<String, String> filter, String search, String sort) {

        Specification<T> filterSpecification = RSQLJPASupport.toSpecification(toRSQLString(filter));
        Specification<T> searchSpecification = RSQLJPASupport.toSpecification(search);
        Specification<T> sortSpecification = RSQLJPASupport.toSort(sort == null ? DEFAULT_SORT : sort);

        return Optional.ofNullable(sortSpecification)
            .map(spec -> spec.and(filterSpecification))
            .map(spec -> spec.and(searchSpecification)).orElseThrow();

    }

}
