package com.softserveinc.ita.homeproject.homedata.specifications;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;
import static io.github.perplexhub.rsql.RSQLJPASupport.toSort;


public abstract class EntitySpecification<T> {
    public Specification<T> getEntity(String search, String sort) {
        return RSQLJPASupport.<T>toSpecification(search).and(toSort(sort));
    }

}
