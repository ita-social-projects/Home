package com.softserveinc.ita.homeproject.homedata.cooperation.apatment;

import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface ApartmentRepository extends PagingAndSortingRepository<Apartment, Long>,
    JpaSpecificationExecutor<Apartment> {
    Apartment findApartmentByApartmentNumber(String apartmentNumber);

    Optional<Apartment> findByApartmentNumberAndHouseId(String apartmentNumber, Long houseId);
}
