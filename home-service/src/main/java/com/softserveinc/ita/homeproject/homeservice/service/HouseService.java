package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import org.springframework.data.domain.Page;

public interface HouseService {

    HouseDto createHouse(HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    Page<HouseDto> getAllHouses(Integer pageNumber, Integer pageSize);

    HouseDto getHouseById(Long id);

    void deleteById(Long id);

}
