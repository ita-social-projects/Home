package com.softserveinc.ita.homeproject.homeservice.service.cooperation.apartment;

import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.apartment.ApartmentDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ApartmentService extends QueryableService<Apartment, ApartmentDto> {
    ApartmentDto createApartment(Long houseId, ApartmentDto createApartmentDto);
  
    ApartmentDto updateApartment(Long houseId, Long apartmentId, ApartmentDto updateApartmentDto);

    void deactivateApartment(Long houseId, Long apartmentId);
}
