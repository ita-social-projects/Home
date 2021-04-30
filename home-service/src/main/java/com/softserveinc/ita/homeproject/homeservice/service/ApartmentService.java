package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface ApartmentService extends QueryableService<Apartment, ApartmentDto> {
    ApartmentDto createApartment(Long houseId, ApartmentDto createApartmentDto);

    ApartmentDto getApartmentById(Long houseId, Long id);

    Page<ApartmentDto> findAll(Integer pageNumber, Integer pageSize, Specification<Apartment> specification);
}
