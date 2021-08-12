package com.softserveinc.ita.homeproject.homeservice.service.apartment;

import com.softserveinc.ita.homeproject.homedata.entity.apartment.Apartment;
import com.softserveinc.ita.homeproject.homeservice.dto.apartment.ApartmentDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ApartmentService extends QueryableService<Apartment, ApartmentDto> {
    ApartmentDto createApartment(Long houseId, ApartmentDto createApartmentDto);
  
    ApartmentDto updateApartment(Long houseId, Long apartmentId, ApartmentDto updateApartmentDto);

    void deactivateApartment(Long houseId, Long apartmentId);
}
