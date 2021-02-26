package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HouseRepository extends PagingAndSortingRepository<House, Long> {
}
