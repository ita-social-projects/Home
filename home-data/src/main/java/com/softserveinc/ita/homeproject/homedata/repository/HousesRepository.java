package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.Houses;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface HousesRepository extends PagingAndSortingRepository<Houses, Long> {

}
