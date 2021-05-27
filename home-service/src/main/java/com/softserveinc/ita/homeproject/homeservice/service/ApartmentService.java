package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentDto;


public interface ApartmentService extends QueryableService<Apartment, ApartmentDto> {
    ApartmentDto createApartment(Long houseId, ApartmentDto createApartmentDto);

    ApartmentDto getApartmentById(Long houseId, Long id);

    ApartmentDto getApartmentById(Long id);
}
