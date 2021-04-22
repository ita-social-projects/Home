package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface HouseService extends QueryableService<House, HouseDto> {

    HouseDto createHouse(Long cooperationId, HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    Page<HouseDto> findAll(Integer pageNumber, Integer pageSize, Specification<House> specification);

    void deactivateById(Long coopId, Long id);

}
