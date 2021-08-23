package com.softserveinc.ita.homeproject.homeservice.service.cooperation.house;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.house.House;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface HouseService extends QueryableService<House, HouseDto> {

    HouseDto createHouse(Long cooperationId, HouseDto createHouseDto);

    HouseDto updateHouse(Long id, HouseDto updateHouseDto);

    void deactivateById(Long coopId, Long id);

}
