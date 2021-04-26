package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;

public interface HouseService extends QueryableService<House, HouseDto> {

    HouseDto createHouse(Long cooperationId, HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    void deactivateById(Long coopId, Long id);

}
