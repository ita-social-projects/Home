package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface HouseService {

    HouseDto createHouse(Long cooperationId, HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    Page<HouseDto> getAllHouses(Integer pageNumber, Integer pageSize, Specification<House> specification);

    HouseDto getHouseById(Long coopId, Long id);
//    HouseDto getHouseById(Long id);

    void deleteById(Long id);



}
