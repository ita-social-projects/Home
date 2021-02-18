package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository is the interface that is needed
 * for interaction with Users in the database.
 *
 * @author Ihor Svyrydenko
 */

@Repository
public interface NewsRepository extends PagingAndSortingRepository<News, Long>, JpaSpecificationExecutor<News> {
}
