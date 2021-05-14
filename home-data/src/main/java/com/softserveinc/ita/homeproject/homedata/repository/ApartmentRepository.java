package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long>,
        JpaSpecificationExecutor<Apartment> {
}
