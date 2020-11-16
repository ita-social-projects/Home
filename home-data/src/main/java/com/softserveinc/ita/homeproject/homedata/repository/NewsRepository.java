package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Long> {
}
