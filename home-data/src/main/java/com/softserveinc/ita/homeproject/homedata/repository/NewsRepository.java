package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * NewsRepository is the interface that is needed
 * for interaction with News in the database.
 *
 * @author Mykyta Morar
 */
@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, Long> {
}
