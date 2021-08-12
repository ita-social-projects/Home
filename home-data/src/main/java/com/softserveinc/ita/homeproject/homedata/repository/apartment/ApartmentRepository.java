package com.softserveinc.ita.homeproject.homedata.repository.apartment;

import com.softserveinc.ita.homeproject.homedata.entity.apartment.Apartment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long>,
        JpaSpecificationExecutor<Apartment> {
    Apartment findApartmentByApartmentNumber(String apartmentNumber);
}
