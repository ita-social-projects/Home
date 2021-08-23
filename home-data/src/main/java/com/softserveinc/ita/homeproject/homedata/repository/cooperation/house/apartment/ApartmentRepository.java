package com.softserveinc.ita.homeproject.homedata.repository.cooperation.house.apartment;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.house.apartment.Apartment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long>,
        JpaSpecificationExecutor<Apartment> {
    Apartment findApartmentByApartmentNumber(String apartmentNumber);
}
