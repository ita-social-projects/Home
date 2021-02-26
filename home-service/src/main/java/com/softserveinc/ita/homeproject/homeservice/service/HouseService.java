package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import org.springframework.data.domain.Page;

public interface HouseService {

    HouseDto createHouse(Long cooperationId, HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    Page<HouseDto> getAllHouses(Long cooperationId, Integer pageNumber, Integer pageSize);

    HouseDto getHouseById(Long id);

    void deleteById(Long id);

}
