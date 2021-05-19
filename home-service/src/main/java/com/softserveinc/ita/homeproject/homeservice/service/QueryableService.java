package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * QueryableService - service that provides Spring Data Page by entity specification
 * and page parameters
 * @author Oleksii Zinkevych
 * @param <T> - entity type
 * @param <D> - DTO type
 */
public interface QueryableService<T extends BaseEntity, D extends BaseDto> {
    String NOT_FOUND_MESSAGE = "%s with 'id: %s' is not found";

    String MORE_THAN_ONE_ELEMENT_MESSAGE =
        "Result of the request that require to return one element, contains more than one element";

    int DEFAULT_PAGE_NUMBER = 1;

    int DEFAULT_PAGE_SIZE = 5;

    /**
     * @param pageNumber - integer value of number og pages
     * @param pageSize - integer value of elements in page
     * @param specification - Spring Data Specification
     * @return - Spring Data Page of DTOs according to Specification
     */
    @Transactional
    Page<D> findAll(Integer pageNumber, Integer pageSize, Specification<T> specification);

    /**
     * @param id - long value of id of the element
     * @return Single DTO
     * @throws NotFoundHomeException if number of Page elements less than 1
     * @throws IllegalStateException if number of Page elements more than 1
     */
    @Transactional
    default D getOne(Long id) {
        Specification<T> specification =
            (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
        specification = specification
            .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return getOneDto(id, specification);
    }

    /**
     * @param id - long value of id of the element
     * @param specification - Spring Data Specification
     * @return Single DTO
     * @throws NotFoundHomeException if number of Page elements less than 1
     * @throws IllegalStateException if number of Page elements more than 1
     */
    @Transactional
    default D getOne(Long id, Specification<T> specification) {
        return getOneDto(id, specification);
    }

    private D getOneDto(Long id, Specification<T> specification) {
        Page<D> page = findAll(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, specification);
        if (page.getTotalElements() == 1) {
            return page.getContent().get(0);
        } else if (page.getTotalElements() == 0) {
            throw new NotFoundHomeException(getMessage(id));
        } else {
            throw new IllegalStateException(MORE_THAN_ONE_ELEMENT_MESSAGE);
        }
    }

    private String getMessage(Long id) {
        String className = getEntityClassName().orElse("Entity");
        return String.format(NOT_FOUND_MESSAGE, className, id);
    }

    private Optional<String> getEntityClassName() {
        Class<?>[] classes = GenericTypeResolver
            .resolveTypeArguments(getClass(), QueryableService.class);
        if (classes != null) {
            return Optional.of((classes[0].getSimpleName()));
        } else {
            return Optional.empty();
        }
    }
}
