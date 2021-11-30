package com.softserveinc.ita.homeproject.homedata.general.news;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * NewsRepository is the interface that is needed
 * for interaction with News in the database.
 *
 * @author Ihor Svyrydenko
 */

@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, Long>, JpaSpecificationExecutor<News> {
}
