package com.softserveinc.ita.homeproject.application.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.core.MultivaluedMap;

import com.softserveinc.ita.homeproject.application.service.EntitySpecificationService;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EntitySpecificationServiceImpl<T> implements EntitySpecificationService<T> {

    public static final String RSQL_EQUAL = "==";

    public static final String RSQL_AND = ";";

    private static String toRSQLString(MultivaluedMap<String, String> filter) {
        return filter.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> entry.getValue().stream()
                        .map(valueElement -> entry.getKey() + RSQL_EQUAL + valueElement)
                        .collect(Collectors.joining(RSQL_AND)))
                .collect(Collectors.joining(RSQL_AND));
    }

    @Override
    public Specification<T> getSpecification(MultivaluedMap<String, String> filter, String search, String sort) {
        Specification<T> filterSpecification = RSQLJPASupport.toSpecification(toRSQLString(filter));
        Specification<T> searchSpecification = RSQLJPASupport.toSpecification(search);
        Specification<T> sortSpecification = RSQLJPASupport.toSort(sort);

        return Optional.of(sortSpecification)
                .map(spec -> spec.and(filterSpecification))
                .map(spec -> spec.and(searchSpecification)).orElseThrow();
    }
}
